package g_mungus.zpl.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public class CatwalkBlock extends Block {
    private static final VoxelShape BOX_COLLIDER;
    public CatwalkBlock(Properties arg) {
        super(arg);
    }

    public VoxelShape getShape(BlockState arg, BlockGetter arg2, BlockPos arg3, CollisionContext arg4) {
        return BOX_COLLIDER;
    }

    static {
        BOX_COLLIDER = Block.box(0F, 12.0F, 0.0F, 16.0F, 16.0F, 16.0F);
    }


}
