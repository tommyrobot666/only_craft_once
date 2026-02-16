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
    public static Codec<@NotNull TimesCraftedSavedData> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.unboundedMap(
                            Codec.STRING,
                            Codec.INT
                    ).stable().fieldOf("map").forGetter(TimesCraftedSavedData::getMap)
            ).apply(instance, TimesCraftedSavedData::new)
    );
    public static SavedData.Factory<TimesCraftedSavedData> FACTORY = new Factory<>(
            TimesCraftedSavedData::new,
            TimesCraftedSavedData::load,
            null
    );

    private static TimesCraftedSavedData load(CompoundTag tag, HolderLookup.Provider provider) {
        return CODEC.decode(NbtOps.INSTANCE, tag).getOrThrow().getFirst();
    }

    public TimesCraftedSavedData() {

    }

    private static Map<String, Integer> getMap(TimesCraftedSavedData o) {
        return o.map;
    }

    //    Object2IntOpenHashMap<Identifier> map;
    public HashMap<String,Integer> map = new HashMap<>();

    TimesCraftedSavedData(Map<String,Integer> map){
        this.map = new HashMap<>(map);
    }

    public static TimesCraftedSavedData getFromLevel(ServerLevel level){
        return level.getServer().overworld().getDataStorage().computeIfAbsent(
                FACTORY
                , Constants.MOD_ID+"_times_crafted");
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        return (CompoundTag) CODEC.encode(this, NbtOps.INSTANCE, compoundTag).getOrThrow();
    }
}
