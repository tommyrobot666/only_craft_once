package lommie.onlycraftonce.saveddata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lommie.onlycraftonce.Constants;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class TimesCraftedSavedData extends SavedData {
    public static final Codec<@NotNull TimesCraftedSavedData> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.unboundedMap(
                            Codec.STRING,
                            Codec.INT
                    ).stable().fieldOf("map").forGetter(TimesCraftedSavedData::getMap)
            ).apply(instance, TimesCraftedSavedData::new)
    );
    public static final Factory<TimesCraftedSavedData> FACTORY = new Factory<>(
            TimesCraftedSavedData::new,
            (tag,reg) -> CODEC.decode(NbtOps.INSTANCE,tag).getOrThrow().getFirst(),
            null
    );
    public static final String NAME = Constants.MOD_ID+"_times_crafted";

    private static Map<String, Integer> getMap(TimesCraftedSavedData o) {
        return o.map;
    }

    public HashMap<String,Integer> map = new HashMap<>();

    TimesCraftedSavedData(Map<String,Integer> map){
        this.map = new HashMap<>(map);
    }

    TimesCraftedSavedData(){}

    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        return ((CompoundTag) CODEC.encode(this, NbtOps.INSTANCE, compoundTag).getOrThrow());
    }

    public static TimesCraftedSavedData get(ServerLevel level){
        return level.getServer().overworld().getDataStorage().computeIfAbsent(FACTORY,NAME);
    }
}
