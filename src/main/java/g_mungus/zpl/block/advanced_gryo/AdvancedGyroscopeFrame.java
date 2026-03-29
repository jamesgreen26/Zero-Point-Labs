package g_mungus.zpl.block.advanced_gryo;

import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class AdvancedGyroscopeFrame extends AdvancedGyroscopeComponent {

    public static final EnumProperty<GyroFrameState> FRAME_STATE = EnumProperty.create("frame_state", GyroFrameState.class);

    public AdvancedGyroscopeFrame(Properties arg) {
        super(arg);
        registerDefaultState(stateDefinition.any().setValue(FRAME_STATE, GyroFrameState.DISASSEMBLED));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FRAME_STATE);
    }
}
