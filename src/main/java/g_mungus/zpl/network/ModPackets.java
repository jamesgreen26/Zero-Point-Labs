package g_mungus.zpl.network;

import g_mungus.zpl.ZeroPointLabsMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModPackets {
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(ZeroPointLabsMod.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        CHANNEL.registerMessage(0, SetGyroMappingPacket.class,
                SetGyroMappingPacket::encode,
                SetGyroMappingPacket::decode,
                SetGyroMappingPacket::handle);
    }
}
