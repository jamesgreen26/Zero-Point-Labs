package g_mungus.zpl.client.ponder;

import g_mungus.zpl.ZeroPointLabsMod;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ZPLPonderPlugin implements PonderPlugin {
    @Override
    public @NotNull String getModId() {
        return ZeroPointLabsMod.MOD_ID;
    }
    @Override
    public void registerScenes(@NotNull PonderSceneRegistrationHelper<ResourceLocation> helper) {
        ZPLPonders.register(helper);
    }

    public static void registerPlugin() {
        PonderIndex.addPlugin(new ZPLPonderPlugin());
    }
}
