package lommie.onlycraftonce.mixin;

import lommie.onlycraftonce.Constants;
import lommie.onlycraftonce.saveddata.TimesCraftedSavedData;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
    @Shadow public abstract ServerLevel level();

    @Inject(method = "awardStat", at = @At("HEAD"))
    void addTimesCrafted(Stat<?> stat, int amount, CallbackInfo ci){
        if (stat.getType().equals(
                BuiltInRegistries.STAT_TYPE.get(
                        Identifier.withDefaultNamespace("crafted")).orElseThrow().value()
        )){
            Constants.LOG.error(stat.toString());
            TimesCraftedSavedData savedData = this.level().getDataStorage().computeIfAbsent(TimesCraftedSavedData.TYPE);
            String id = stat.getName();
            savedData.map.put(id,savedData.map.getOrDefault(id,0)+amount);
            savedData.setDirty();
            Constants.LOG.error(savedData.map.toString());
        }
    }
}
