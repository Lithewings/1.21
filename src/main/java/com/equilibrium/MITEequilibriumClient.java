package com.equilibrium;


import com.equilibrium.client.render.entity.InvisibleStalkerModel;
import com.equilibrium.client.render.entity.TestZombie;
import com.equilibrium.util.MyCommands;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

import static com.equilibrium.entity.ModEntities.INVISIBLE_STALKER;
import static com.equilibrium.entity.ModEntities.TEST_ZOMBIE;
import static com.equilibrium.event.MoonPhaseEvent.getMoonType;


public class MITEequilibriumClient implements ClientModInitializer {









    @Override
    public void onInitializeClient() {










        //将注册的实体和模型结合起来

        EntityRendererRegistry.register(TEST_ZOMBIE, TestZombie::new);
        EntityRendererRegistry.register(INVISIBLE_STALKER, InvisibleStalkerModel::new);

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            MyCommands.registerClientAllCommands(dispatcher);
        });

    }


}
