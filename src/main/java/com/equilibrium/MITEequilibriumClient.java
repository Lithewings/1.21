package com.equilibrium;


import com.equilibrium.block.enchanting_table.*;
import com.equilibrium.client.render.entity.renderer.*;
import com.equilibrium.util.MyCommands;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;


import static com.equilibrium.entity.ModEntities.*;


public class MITEequilibriumClient implements ClientModInitializer {




    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModScreenTypes.EMERALD_ENCHANTING_TABLE, ModEnchantmentScreen::new);

//        registerScreen();

        BlockEntityRendererFactories.register(ModBlockEntityTypes.ENCHANTING_TABLE_BLOCK_ENTITY_TYPE, ModEnchantingTableBlockEntityRenderer::new);
        //注册渲染器(渲染器中包含了实体和模型)


        EntityRendererRegistry.register(INVISIBLE_STALKER, InvisibleStalkerEntityRenderer::new);
        EntityRendererRegistry.register(GHOUL, GhoulEntityRenderer::new);
        EntityRendererRegistry.register(SHADOW, ShadowEntityRenderer::new);
        EntityRendererRegistry.register(WIGHT, WightEntityRenderer::new);

        EntityRendererRegistry.register(LONG_DEAD, LongDeadEntityRenderer::new);
        EntityRendererRegistry.register(PUDDING, PuddingSlimeEntityRenderer::new);


        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            MyCommands.registerClientAllCommands(dispatcher);
        });

    }


}
