package net.jerika.furmutage.menu;

import net.jerika.furmutage.furmutage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, furmutage.MOD_ID);
    
    public static final RegistryObject<MenuType<EugenicsCraftingMenu>> EUGENICS_CRAFTING_MENU =
            MENUS.register("eugenics_crafting_menu",
                    () -> IForgeMenuType.create((int containerId, Inventory inv, FriendlyByteBuf data) -> {
                        return new EugenicsCraftingMenu(containerId, inv);
                    }));

    public static final RegistryObject<MenuType<EugenicsSmelteryMenu>> EUGENICS_SMELTERY_MENU =
            MENUS.register("eugenics_smeltery_menu",
                    () -> IForgeMenuType.create((int containerId, Inventory inv, FriendlyByteBuf data) -> {
                        net.minecraft.core.BlockPos pos = data.readBlockPos();
                        if (inv.player.level().getBlockEntity(pos) instanceof net.jerika.furmutage.block.entity.EugenicsSmelteryBlockEntity be) {
                            return new EugenicsSmelteryMenu(containerId, inv, be, be.getDataAccess());
                        }
                        return new EugenicsSmelteryMenu(containerId, inv, new net.minecraft.world.SimpleContainer(3), new net.minecraft.world.inventory.SimpleContainerData(4));
                    }));
    
    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
