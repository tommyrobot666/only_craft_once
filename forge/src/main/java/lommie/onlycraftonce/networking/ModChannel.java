package lommie.onlycraftonce.networking;

import lommie.onlycraftonce.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.SimpleChannel;

public class ModChannel {
    public static final SimpleChannel CLIENTBOUND = ChannelBuilder
            .named(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID,"clientbound"))
            .optional().simpleChannel().play().clientbound().build();
    public static final SimpleChannel SERVERBOUND = ChannelBuilder
            .named(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID,"serverbound"))
            .optional().simpleChannel().play().serverbound().build();
}
