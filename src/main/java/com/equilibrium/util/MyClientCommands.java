package com.equilibrium.util;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import static com.equilibrium.event.MoonPhaseEvent.getMoonType;

public class MyClientCommands {

    public static void registerAllCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        /*
         * 统一注册所有客户端命令
         */
        // 注册 moonType 命令
        dispatcher.register(ClientCommandManager.literal("moonType")
                .executes(context -> {
                    // 从客户端获取当前世界
                    var world = MinecraftClient.getInstance().world;

                    if (world == null) {
                        context.getSource().sendFeedback(Text.literal("无法获取当前世界。"));
                    } else {
                        // 调用你写好的 getMoonType 方法获取月相
                        String moon = getMoonType(world);
                        context.getSource().sendFeedback(Text.literal("当前月相是: " + moon));
                    }
                    return 1;
                })
        );

        // 注册 day 命令
        dispatcher.register(ClientCommandManager.literal("day")
                .executes(context -> {
                    // 从客户端获取当前世界
                    var world = MinecraftClient.getInstance().world;

                    if (world == null) {
                        context.getSource().sendFeedback(Text.literal("无法获取当前世界。"));
                    } else {
                        long time = world.getTimeOfDay();
                        int day = (int) (time / 24000L);
                        context.getSource().sendFeedback(Text.literal("It is day " + day));
                    }
                    return 1;
                })
        );
    }

    // 继续注册更多客户端命令...
}

