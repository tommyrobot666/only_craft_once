package lommie.onlycraftonce;


import lommie.onlycraftonce.yacl.YACLScreen;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(Constants.MOD_ID)
public class OnlyCraftOnce {

    public OnlyCraftOnce(IEventBus eventBus) {
        CommonClass.init();
        if (FMLEnvironment.getDist().isClient()){
            CommonClientClass.init();
        }

        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (m,s) -> YACLScreen.generateScreen(s)
        );
    }
}
