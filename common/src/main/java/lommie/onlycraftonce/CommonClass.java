package lommie.onlycraftonce;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lommie.onlycraftonce.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CommonClass {
    public static HashMap<Item,Integer> maxTimesCrafted = new HashMap<>(Map.of(
            Items.MACE, 3,
            Items.IRON_NUGGET, 18
    ));

    public static void init() {
        if (Services.PLATFORM.isModLoaded("only_craft_once")) {
            Constants.LOG.info("YOU WILL NEVER CRAFT AGAIN");
        }

        tryToLoadConfigAndHandleErrors();
    }

    private static void tryToLoadConfigAndHandleErrors() {
        if (!Files.exists(Services.PLATFORM.configFile())){
            writeDefaultConfig();
            return;
        }

        try {
            loadConfig();
        } catch (IOException e) {
            Constants.LOG.error("Error while loading config:"+e.getMessage());
            e.printStackTrace();

            Constants.LOG.info("Moving broken config");
            try {
                long uniqueNumber = Files.list(Services.PLATFORM.configFile().getParent()).count();
                Files.move(Services.PLATFORM.configFile(),
                        Services.PLATFORM.configFile().resolve(String.valueOf(uniqueNumber)));
            } catch (IOException ex) {
                Constants.LOG.info("Error while moving config");
                ex.printStackTrace();
            }

            writeDefaultConfig();
        }
    }

    private static void writeDefaultConfig() {
        Constants.LOG.info("Writing new default config");
        try (JsonWriter writer = new JsonWriter(Files.newBufferedWriter(Services.PLATFORM.configFile()))) {
            writer.beginArray();
            writer.beginObject();
            writer.name("id");
            writer.value("minecraft:mace");
            writer.name("max");
            writer.value(3);
            writer.endObject();
            writer.endArray();
            writer.close();
        } catch (IOException ex) {
            Constants.LOG.error("Error while writing config");
            ex.printStackTrace();
        }

        maxTimesCrafted.clear();
        maxTimesCrafted.put(Items.MACE,3);
    }

    private static void loadConfig() throws IOException {
        Constants.LOG.info("Loading config");
        HashMap<String,Integer> loadingConfig = new HashMap<>();
        try (JsonReader reader = new JsonReader(Files.newBufferedReader(Services.PLATFORM.configFile()))){
            int count = 0;
            reader.beginArray();
            while (reader.hasNext()){
                reader.beginObject();
                try {
                    reader.nextName();
                } catch (IOException e) {
                    Constants.LOG.error("Config has empty json object (index {})",count);
                    reader.endObject();
                    continue;
                }

                String id = "";
                try {
                    id = reader.nextString();
                } catch (IOException e) {
                    Constants.LOG.error("Next value in config is not string (index {})",count);
                    reader.endObject();
                    continue;
                }

                try {
                    reader.nextName();
                } catch (IOException e) {
                    Constants.LOG.error("Json object ends with only one value (index {})",count);
                    reader.endObject();
                    continue;
                }

                int max = 0;
                try {
                    max = reader.nextInt();
                } catch (IOException e) {
                    Constants.LOG.error("Next value in config is not integer (index {})",count);
                    reader.endObject();
                    continue;
                }

                reader.endObject();


                loadingConfig.put(id,max);
                count++;
            }
            reader.endArray();
            reader.close();
        }

        maxTimesCrafted.clear();
        loadingConfig.forEach((sId,max) -> {
            Optional<Item> item = BuiltInRegistries.ITEM.getOptional(Identifier.parse(sId));
            if (item.isEmpty()){
                Constants.LOG.error("Id {} was not found in registry",sId);
            } else {
                maxTimesCrafted.put(item.get(),max);
            }
        });

        if (maxTimesCrafted.containsKey(Items.AIR)){
            Constants.LOG.error("Config loading error: An item id either failed to parse or wasn't found");
        }
        maxTimesCrafted.remove(Items.AIR);
    }
}
