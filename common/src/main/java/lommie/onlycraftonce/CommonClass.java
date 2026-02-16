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

public class CommonClass {
    public static Map<Item,Integer> maxTimesCrafted = Map.of(
            Items.MACE, 3,
            Items.IRON_NUGGET, 18
    );

    public static void init() {
        if (Services.PLATFORM.isModLoaded("only_craft_once")) {
            Constants.LOG.info("YOU WILL NEVER CRAFT AGAIN");
        }

        try {
            loadConfig();
        } catch (IOException e) {
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
                throw new RuntimeException(ex);
            }
        }
    }

    private static void loadConfig() throws IOException {
        HashMap<String,Integer> loadingConfig = new HashMap<>();
        try (JsonReader reader = new JsonReader(Files.newBufferedReader(Services.PLATFORM.configFile()))){
            reader.beginArray();
            while (reader.hasNext()){
                reader.beginObject();
                String id = reader.nextString();
                int max = reader.nextInt();
                loadingConfig.put(id,max);
                reader.endObject();
            }
            reader.endArray();
            reader.close();
        } catch (IOException e) {
            throw e;
        }

        maxTimesCrafted.clear();
        loadingConfig.forEach((sId,max) -> {
            maxTimesCrafted.put(BuiltInRegistries.ITEM.get(Identifier.parse(sId)).orElseThrow().value(),max);
                });
    }
}
