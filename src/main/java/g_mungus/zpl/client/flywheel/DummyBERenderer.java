package g_mungus.zpl.client.flywheel;

import com.mojang.blaze3d.vertex.PoseStack;
import g_mungus.zpl.block.thruster.ThrusterExhaustBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.Vec3;

public class DummyBERenderer implements BlockEntityRenderer<ThrusterExhaustBlockEntity> {
    public DummyBERenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public int getViewDistance() {
        return 256;
    }

    @Override
    public boolean shouldRenderOffScreen(ThrusterExhaustBlockEntity arg) {
        return true;
    }

    @Override
    public boolean shouldRender(ThrusterExhaustBlockEntity arg, Vec3 arg2) {
        return true;
    }

    @Override
    public void render(ThrusterExhaustBlockEntity a, float b, PoseStack c, MultiBufferSource d, int e, int f) {}
}