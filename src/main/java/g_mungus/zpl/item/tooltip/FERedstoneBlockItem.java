package g_mungus.zpl.item.tooltip;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FERedstoneBlockItem extends BlockItem {
    public FERedstoneBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        components.add(Component.empty()
                .append(Component.literal("Needs ").withStyle(ChatFormatting.GRAY))
                .append(Component.literal("FE").withStyle(ChatFormatting.GREEN))
                .append(Component.literal(" and ").withStyle(ChatFormatting.GRAY))
                .append(Component.literal("Redstone").withStyle(ChatFormatting.RED)));
        super.appendHoverText(itemStack, level, components, flag);
    }
}
