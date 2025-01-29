package com.equilibrium.mixin.structure;

import com.google.common.base.Stopwatch;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.RegistryEntryPredicateArgumentType;
import net.minecraft.command.argument.RegistryPredicateArgumentType;
import net.minecraft.item.Items;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.Structure;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.*;

public class LocateStructures {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final DynamicCommandExceptionType STRUCTURE_NOT_FOUND_EXCEPTION = new DynamicCommandExceptionType(
            id -> Text.stringifiedTranslatable("commands.locate.structure.not_found", id)
    );
    private static final DynamicCommandExceptionType STRUCTURE_INVALID_EXCEPTION = new DynamicCommandExceptionType(
            id -> Text.stringifiedTranslatable("commands.locate.structure.invalid", id)
    );
    private static final int LOCATE_STRUCTURE_RADIUS = 100;

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        // 注册指令 "locate" 和 "structure"
        dispatcher.register(
                CommandManager.literal("locate")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(
                                CommandManager.literal("structure")
                                        .then(
                                                CommandManager.argument("structure", StringArgumentType.word())
                                                        .executes(
                                                                context -> executeLocateSpecificStructure(context.getSource())
                                                        )
                                        )
                        )
        );

        // 监听右键事件，检查是否持有指南针
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                if (player.getMainHandStack().getItem() == Items.COMPASS) {
                    // 当玩家右键点击指南针时，触发 locate 指令
                    player.sendMessage(Text.literal("Right-clicked with compass! Locating fortress..."), false);
                    try {
                        executeLocateSpecificStructure(player.getCommandSource());
                    } catch (CommandSyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    // 新的硬编码方法，仅查找 "minecraft:fortress"
    public static int executeLocateSpecificStructure(ServerCommandSource source) throws CommandSyntaxException {
        Registry<Structure> registry = source.getWorld().getRegistryManager().get(RegistryKeys.STRUCTURE);

        // 这里直接指定要查找的结构 ID "minecraft:fortress"
        RegistryEntry<Structure> fortressEntry = registry.getEntry(Identifier.tryParse("minecraft:fortress"))
                .orElseThrow(() -> STRUCTURE_INVALID_EXCEPTION.create("minecraft:fortress"));

        BlockPos blockPos = BlockPos.ofFloored(source.getPosition());
        ServerWorld serverWorld = source.getWorld();
        Stopwatch stopwatch = Stopwatch.createStarted(Util.TICKER);

        // 查找结构
        Pair<BlockPos, RegistryEntry<Structure>> pair = serverWorld.getChunkManager()
                .getChunkGenerator()
                .locateStructure(serverWorld, RegistryEntryList.of(fortressEntry), blockPos, LOCATE_STRUCTURE_RADIUS, false);

        stopwatch.stop();

        if (pair == null) {
            throw STRUCTURE_NOT_FOUND_EXCEPTION.create("minecraft:fortress");
        } else {
            return returnBlockPos(pair.getFirst());
        }
    }

    // 直接返回BlockPos类并打印坐标
    private static int returnBlockPos(BlockPos blockPos) {
        // 打印BlockPos的坐标
        LOGGER.info("Found structure at coordinates: X=" + blockPos.getX() + ", Y=" + blockPos.getY() + ", Z=" + blockPos.getZ());

        // 返回BlockPos的hashCode，或者可以返回其他信息
        return blockPos.hashCode();
    }

}
