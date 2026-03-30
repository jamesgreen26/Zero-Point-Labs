package g_mungus.zpl.client.advanced_gryo;

import com.mojang.blaze3d.vertex.PoseStack;
import g_mungus.zpl.block.advanced_gryo.AdvancedGyroscopeController;
import g_mungus.zpl.block.advanced_gryo.AdvancedGyroscopeControllerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class AdvancedGyroscopeControllerBlockEntityRenderer implements BlockEntityRenderer<AdvancedGyroscopeControllerBlockEntity> {

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

        poseStack.pushPose();
        try {
            poseStack.translate(offset.getStepX(), offset.getStepY(), offset.getStepZ());
            Minecraft.getInstance().getBlockRenderer().renderSingleBlock(renderState, poseStack, bufferSource, packedLight, packedOverlay);
        } finally {
            poseStack.popPose();
        }
    }
}
