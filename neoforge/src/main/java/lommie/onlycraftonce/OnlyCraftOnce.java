package lommie.onlycraftonce;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(Constants.MOD_ID)
public class OnlyCraftOnce {

    public OnlyCraftOnce(IEventBus eventBus) {
        CommonClass.init();
        if (FMLEnvironment.getDist().isClient()){
            CommonClientClass.init();
        }
    }
}
