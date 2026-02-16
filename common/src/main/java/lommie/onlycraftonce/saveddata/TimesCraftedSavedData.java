package lommie.onlycraftonce.saveddata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lommie.onlycraftonce.Constants;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class TimesCraftedSavedData extends SavedData {
    public static Codec<@NotNull TimesCraftedSavedData> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.unboundedMap(
                            Codec.STRING,
                            Codec.INT
                    ).stable().fieldOf("map").forGetter(TimesCraftedSavedData::getMap)
            ).apply(instance, TimesCraftedSavedData::new)
    );

    private static Map<String, Integer> getMap(TimesCraftedSavedData o) {
        return o.map;
    }

    public static SavedDataType<@NotNull TimesCraftedSavedData> TYPE = new SavedDataType<>(
            Constants.MOD_ID+"_times_crafted",
            TimesCraftedSavedData::new,
            CODEC,
            null
    );

    //    Object2IntOpenHashMap<Identifier> map;
    public HashMap<String,Integer> map = new HashMap<>();

    TimesCraftedSavedData(Map<String,Integer> map){
        this.map = new HashMap<>(map);
    }

    TimesCraftedSavedData(){}
}
