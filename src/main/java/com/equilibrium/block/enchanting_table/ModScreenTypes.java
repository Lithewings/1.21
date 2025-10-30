package com.equilibrium.block.enchanting_table;

import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.StonecutterScreenHandler;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

import java.util.Map;

import static com.equilibrium.MITEequilibrium.MOD_ID;

public class ModScreenTypes {


    private static <T extends ScreenHandler> ScreenHandlerType<T> register(String id, ScreenHandlerType.Factory<T> factory) {
        return Registry.register(Registries.SCREEN_HANDLER, id, new ScreenHandlerType<>(factory, FeatureFlags.VANILLA_FEATURES));
    }
//    private static final Logger LOGGER = LogUtils.getLogger();
//    private static final Map<ScreenHandlerType<?>, HandledScreens.Provider<?, ?>> PROVIDERS = Maps.<ScreenHandlerType<?>, HandledScreens.Provider<?, ?>>newHashMap();
//
    public static final ScreenHandlerType<ModEnchantmentScreenHandler> EMERALD_ENCHANTING_TABLE = register("miteequilibrium:emerald_enchantment", ModEnchantmentScreenHandler::new);
//
//
    public static void registerScreenHandlers() {
        // 空方法，注册已经在静态初始化中完成
    }
}