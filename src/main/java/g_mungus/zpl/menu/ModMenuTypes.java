package g_mungus.zpl.menu;

import g_mungus.zpl.ZeroPointLabsMod;
import g_mungus.zpl.block.advanced_gryo.AdvancedGyroscopeControllerMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(Registries.MENU, ZeroPointLabsMod.MOD_ID);

    public static final RegistryObject<MenuType<AdvancedGyroscopeControllerMenu>> ADVANCED_GYROSCOPE_CONTROLLER =
            MENU_TYPES.register("advanced_gyroscope_controller",
                    () -> IForgeMenuType.create(AdvancedGyroscopeControllerMenu::new));
}
