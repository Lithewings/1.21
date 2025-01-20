package com.equilibrium.util;

import com.equilibrium.persistent_state.StateSaverAndLoader;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.loader.impl.launch.server.FabricServerLauncher;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static com.equilibrium.event.MoonPhaseEvent.getMoonType;

public class MyCommands {






    public static void registerClientAllCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
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
// 注册 locate 命令
//        dispatcher.register(ClientCommandManager.literal("locate")
//                .requires(source -> source.hasPermissionLevel(2))  // 设置权限
//                .then(ClientCommandManager.literal("structure")
//                        .then(ClientCommandManager.argument("structure", StringArgumentType.word())  // 接受结构名称
//                                .executes(context -> {
//                                    // 获取玩家输入的结构名称
//                                    String structure = StringArgumentType.getString(context, "structure");
//                                    // 这里你可以使用结构名称来处理命令逻辑
//                                    // 举个例子，打印结构名称
//                                    context.getSource().sendFeedback(Text.literal("Locating structure: " + structure));
//                                    return 1;
//                                })
//                        )
//                )
//        );


    }
}


    // 继续注册更多客户端命令...


