package g_mungus.zpl.network;

import g_mungus.zpl.block.advanced_gryo.AdvancedGyroscopeControllerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SetGyroMappingPacket {
    private final BlockPos pos;
    private final int[] mapping;

    public SetGyroMappingPacket(BlockPos pos, int[] mapping) {
        this.pos = pos;
        this.mapping = mapping.clone();
    }

    public static void encode(SetGyroMappingPacket packet, FriendlyByteBuf buf) {
        buf.writeBlockPos(packet.pos);
        for (int i = 0; i < 8; i++) {
            buf.writeInt(packet.mapping[i]);
        }
    }

    public static SetGyroMappingPacket decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        int[] mapping = new int[8];
        for (int i = 0; i < 8; i++) {
            mapping[i] = buf.readInt();
        }
        return new SetGyroMappingPacket(pos, mapping);
    }

    public static void handle(SetGyroMappingPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerLevel level = ctx.get().getSender().serverLevel();
            BlockEntity be = level.getBlockEntity(packet.pos);
            if (be instanceof AdvancedGyroscopeControllerBlockEntity controller) {
                controller.setInputFunctionMapping(packet.mapping);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
