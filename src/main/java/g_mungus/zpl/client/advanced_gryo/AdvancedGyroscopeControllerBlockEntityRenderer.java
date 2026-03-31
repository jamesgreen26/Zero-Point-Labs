package g_mungus.zpl.client.advanced_gryo;

import com.mojang.blaze3d.vertex.PoseStack;
import g_mungus.zpl.block.advanced_gryo.AdvancedGyroscopeController;
import g_mungus.zpl.block.advanced_gryo.AdvancedGyroscopeControllerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Quaternionf;
import org.valkyrienskies.core.api.ships.ClientShip;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import java.util.WeakHashMap;

public class AdvancedGyroscopeControllerBlockEntityRenderer implements BlockEntityRenderer<AdvancedGyroscopeControllerBlockEntity> {

    private static final double INERTIA_GAIN = 0.7;
    private static final double ANGULAR_DAMPING = 0.92;

    private static class RenderState {
        final Quaterniond blockLocalRot = new Quaterniond();
        final Quaterniond velocityStep = new Quaterniond(); // per-frame rotation step; identity = no spin
        Quaterniond prevShipRot = null;
    }

    private final WeakHashMap<AdvancedGyroscopeControllerBlockEntity, RenderState> stateMap = new WeakHashMap<>();

    public AdvancedGyroscopeControllerBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(AdvancedGyroscopeControllerBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemStack stack = blockEntity.getItemHandler().getStackInSlot(0);
        if (stack.isEmpty() || !(stack.getItem() instanceof BlockItem blockItem)) {
            return;
        }

        Direction offset = blockEntity.getBlockState().getValue(AdvancedGyroscopeController.FACING).getOpposite();
        BlockState renderState = blockItem.getBlock().defaultBlockState();

        RenderState state = stateMap.computeIfAbsent(blockEntity, k -> new RenderState());

        if (blockEntity.getLevel() instanceof ClientLevel clientLevel) {
            ClientShip ship = VSGameUtilsKt.getLoadedShipManagingPos(clientLevel, blockEntity.getBlockPos());
            if (ship != null) {
                Quaterniondc qCurr = ship.getRenderTransform().getShipToWorldRotation();

                if (state.prevShipRot != null) {
                    // dq = inv(q_prev) * q_curr: ship rotation delta in body/local frame
                    Quaterniond dq = state.prevShipRot.conjugate(new Quaterniond())
                            .mul(new Quaterniond(qCurr))
                            .normalize();

                    // Opposing impulse: slerp from identity toward inv(dq) by INERTIA_GAIN
                    Quaterniond impulse = new Quaterniond().slerp(dq.conjugate(new Quaterniond()), INERTIA_GAIN);

                    // Accumulate impulse into velocity step
                    state.velocityStep.mul(impulse).normalize();
                }

                // Damp velocity toward identity (no spin)
                state.velocityStep.slerp(new Quaterniond(), 1.0 - ANGULAR_DAMPING);

                // Integrate block rotation
                state.velocityStep.mul(state.blockLocalRot, state.blockLocalRot).normalize();

                state.prevShipRot = new Quaterniond(qCurr);
            }
        }

        poseStack.pushPose();
        try {
            poseStack.translate(offset.getStepX() + 0.5, offset.getStepY() + 0.5, offset.getStepZ() + 0.5);
            poseStack.mulPose(new Quaternionf(state.blockLocalRot));
            poseStack.translate(-0.5, -0.5, -0.5);
            Minecraft.getInstance().getBlockRenderer().renderSingleBlock(renderState, poseStack, bufferSource, packedLight, packedOverlay);
        } finally {
            poseStack.popPose();
        }
    }
}
