package com.ultracube.init;

import java.util.function.Supplier;

import com.ultracube.Ultracube;
import com.ultracube.menu.CubeEnergyExtractorMenu;
import com.ultracube.menu.CubeMatterCreatorMenu;
import com.ultracube.menu.EnergyGeneratorMenu;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MenuInit {

        public static final DeferredRegister<MenuType<?>> MENU_TYPES =
        DeferredRegister.create(Registries.MENU, Ultracube.MODID);

        public static final Supplier<MenuType<EnergyGeneratorMenu>> ENERGY_GENERATOR_MENU = MENU_TYPES.register("energy_generator_menu",
         () -> IMenuTypeExtension.create(EnergyGeneratorMenu::new));


         public static final Supplier<MenuType<CubeEnergyExtractorMenu>> CUBE_ENERGY_EXTRACTOR_MENU = MENU_TYPES.register("cube_energy_extractor_menu",
         () -> IMenuTypeExtension.create(CubeEnergyExtractorMenu::new));
           

         public static final Supplier<MenuType<CubeMatterCreatorMenu>> CUBE_MATTER_CREATOR_MENU = MENU_TYPES.register("cube_matter_creator_menu",
         () -> IMenuTypeExtension.create(CubeMatterCreatorMenu::new));

}