package com.equilibrium.util;

import com.equilibrium.entity.goal.AStarPathfinder;
import com.equilibrium.persistent_state.StateSaverAndLoader;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.loader.impl.launch.server.FabricServerLauncher;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.List;

import static com.equilibrium.MITEequilibrium.updatePlayerArmor;
import static com.equilibrium.event.MoonPhaseEvent.getMoonType;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;

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
        // 注册 protection命令
        dispatcher.register(ClientCommandManager.literal("protection")
                .executes(context -> {
                    PlayerEntity player = context.getSource().getPlayer();
                    Text text = updatePlayerArmor(player);
                    player.sendMessage(text);

                    return 1;
                })
        );


        // 注册 tickSpeed 命令
        dispatcher.register(ClientCommandManager.literal("randomTickSpeed")
                .executes(context -> {
                    PlayerEntity player = context.getSource().getPlayer();
                    int speed = context.getSource().getWorld().getGameRules().getInt(GameRules.RANDOM_TICK_SPEED);

                    player.sendMessage(Text.of("Random tick speed is"+speed));

                    return 1;
                })
        );

        // 注册 A星算法 命令
        dispatcher.register(ClientCommandManager.literal("findPath")
                .then(argument("x1", IntegerArgumentType.integer())
                        .then(argument("y1", IntegerArgumentType.integer())
                                .then(argument("z1", IntegerArgumentType.integer())
                                        .then(argument("x2", IntegerArgumentType.integer())
                                                .then(argument("y2", IntegerArgumentType.integer())
                                                        .then(argument("z2", IntegerArgumentType.integer())
                .executes(context -> {
                    int x1 = IntegerArgumentType.getInteger(context, "x1");
                    int y1 = IntegerArgumentType.getInteger(context, "y1");
                    int z1 = IntegerArgumentType.getInteger(context, "z1");

                    int x2 = IntegerArgumentType.getInteger(context, "x2");
                    int y2 = IntegerArgumentType.getInteger(context, "y2");
                    int z2 = IntegerArgumentType.getInteger(context, "z2");
                    BlockPos start = new BlockPos(x1,y1,z1);
                    BlockPos goal = new BlockPos(x2,y2,z2);
                    List<BlockPos> path = AStarPathfinder.findPath(context.getSource().getWorld(),start,goal);

                    if (path != null) {
                        // 找到可通行路径 => 屋顶到床连通 => 房屋不封闭
                        context.getSource().getPlayer().sendMessage(Text.of("找到路径"));
                    } else {
                        // 未找到路径 => 屋顶与床被阻隔 => 房屋真正封闭
                        context.getSource().getPlayer().sendMessage(Text.of("没有找到路径"));
                    }
                    return 1;
                }))))))));
    }





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



    // 继续注册更多客户端命令...


