package g_mungus.zpl.client.ponder;

import g_mungus.zpl.block.ModBlocks;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class ZPLPonders {
    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<Block> HELPER =
                helper.withKeyFunction(block -> block.builtInRegistryHolder().key().location());

        HELPER.forComponents(
                ModBlocks.LAUNCH_BUTTON.get(),
                ModBlocks.LAUNCH_PLATFORM.get()
        ).addStoryBoard("assembly", ZPLPonderScenes::assemblyTutorial);

        HELPER.forComponents(
                ModBlocks.ION_MODULATOR_BLOCK.get(),
                ModBlocks.THRUSTER_EXHAUST_BLOCK.get()
        ).addStoryBoard("thruster", ZPLPonderScenes::thrusterTutorial);
    }
}
