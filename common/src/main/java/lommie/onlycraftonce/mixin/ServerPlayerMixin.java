package lommie.onlycraftonce.mixin;

import com.mojang.authlib.GameProfile;
import lommie.onlycraftonce.Constants;
import lommie.onlycraftonce.saveddata.TimesCraftedSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    // remove this
    public ServerPlayerMixin(Level level, BlockPos pos, float yRot, GameProfile gameProfile) {
        super(level, pos, yRot, gameProfile);
    }

    @Shadow public abstract ServerLevel serverLevel();

    @Shadow
    public abstract ServerStatsCounter getStats();

    @Inject(method = "awardStat", at = @At("HEAD"))
    void addTimesCrafted(Stat<?> stat, int amount, CallbackInfo ci){
        if (stat.getType().equals(Stats.ITEM_CRAFTED)){
            TimesCraftedSavedData savedData = TimesCraftedSavedData.get(this.serverLevel());
            String id = stat.getName();
            if (id.endsWith("minecraft.air")){
                Constants.LOG.error("mixin of awardStat found air as crafted item (issue #3)");
            }
            savedData.map.put(id,savedData.map.getOrDefault(id,0)+amount);
            savedData.setDirty();
        }
    }

    @Inject(method = "resetStat", at = @At("HEAD"))
    void removeResetedTimesCrafted(Stat<?> stat, CallbackInfo ci){
        if (stat.getType().equals(Stats.ITEM_CRAFTED)){
            int amount = this.getStats().getValue(stat);
            TimesCraftedSavedData savedData = TimesCraftedSavedData.get(this.serverLevel());
            String id = stat.getName();
            if (id.endsWith("minecraft.air")){
                Constants.LOG.error("mixin of resetStat found air as crafted item (issue #3)");
            }
            int savedAmount = savedData.map.getOrDefault(id,0);
            if (savedAmount < amount) {
                Constants.LOG.warn("When resetting player stat, the player {} had crafted {} more times than recorded. Global times crafted won't be reduced.",this.getDisplayName().getString(),id);
            } else {
                savedData.map.put(id,savedAmount-amount);
                savedData.setDirty();
            }
        }
    }
}
