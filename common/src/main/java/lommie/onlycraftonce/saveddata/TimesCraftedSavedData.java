package lommie.onlycraftonce.saveddata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class TimesCraftedSavedData extends SavedData {
    public static Codec<@NotNull TimesCraftedSavedData> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.unboundedMap(
                            Identifier.CODEC,
                            Codec.INT
                    ).stable().fieldOf("map").forGetter(TimesCraftedSavedData::getMap)
            ).apply(instance, TimesCraftedSavedData::new)
    );

    private static Map<Identifier, Integer> getMap(TimesCraftedSavedData o) {
        return o.map;
    }

    public static SavedDataType<@NotNull TimesCraftedSavedData> TYPE = new SavedDataType<>(
            "",
            TimesCraftedSavedData::new,
            CODEC,
            null
    );

    //    Object2IntOpenHashMap<Identifier> map;
    public HashMap<Identifier,Integer> map = new HashMap<>();

    TimesCraftedSavedData(Map<Identifier,Integer> map){
        this.map = new HashMap<>(map);
    }

    TimesCraftedSavedData(){}
}
