package com.equilibrium.util;

import com.equilibrium.tags.ModBlockTags;
import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.fabricmc.fabric.api.loot.v2.FabricLootTableBuilder;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.fabricmc.fabric.impl.loot.LootUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.List;

public abstract class LootTableModifier {

    //鱼竿钓到的宝藏,附魔等级由30级变为1级(见原版数据包的loot table的部分)

    //以下方块不再掉落物品,不掉落的逻辑在数据包的loot table里面




    public static void modifierLootTables() {
//
//        LootTableEvents.REPLACE.register((key, original, source) -> {
//            //O(n)
//            if (Blocks.GRAVEL.getLootTableKey() == key && source.isBuiltin()) {
//                LootTable lootTable = LootTable.builder().build();
//                return lootTable;
//            }
//
//            if (Blocks.IRON_ORE.getLootTableKey() == key && source.isBuiltin()) {
//                return LootTable.builder().build();
//            }
//            if (Blocks.DEEPSLATE_IRON_ORE.getLootTableKey() == key && source.isBuiltin()) {
//                return LootTable.builder().build();
//            }
//
//            if (Blocks.COPPER_ORE.getLootTableKey() == key && source.isBuiltin()) {
//                return LootTable.builder().build();
//            }
//
//            if (Blocks.DEEPSLATE_COPPER_ORE.getLootTableKey() == key && source.isBuiltin()) {
//                return LootTable.builder().build();
//            }
//
//            if (Blocks.IRON_ORE.getLootTableKey() == key && source.isBuiltin()) {
//                return LootTable.builder().build();
//            }
//            if (Blocks.DEEPSLATE_IRON_ORE.getLootTableKey() == key && source.isBuiltin()) {
//                return LootTable.builder().build();
//            }
//
//            if (Blocks.DIAMOND_ORE.getLootTableKey() == key && source.isBuiltin()) {
//                return LootTable.builder().build();
//            }
//            if (Blocks.DEEPSLATE_DIAMOND_ORE.getLootTableKey() == key && source.isBuiltin()) {
//                return LootTable.builder().build();
//            }
//
//
//            if (Blocks.COAL_ORE.getLootTableKey() == key && source.isBuiltin()) {
//                return LootTable.builder().build();
//            }
//            if (Blocks.DEEPSLATE_COAL_ORE.getLootTableKey() == key && source.isBuiltin()) {
//                return LootTable.builder().build();
//            }
//
//
//            if (Blocks.NETHER_QUARTZ_ORE.getLootTableKey() == key && source.isBuiltin()) {
//                return LootTable.builder().build();
//            }
//
//            if (Blocks.NETHER_GOLD_ORE.getLootTableKey() == key && source.isBuiltin()) {
//                return LootTable.builder().build();
//            }
//
//
//            if (Blocks.EMERALD_ORE.getLootTableKey() == key && source.isBuiltin()) {
//                return LootTable.builder().build();
//            }
//            if (Blocks.DEEPSLATE_EMERALD_ORE.getLootTableKey() == key && source.isBuiltin()) {
//                return LootTable.builder().build();
//            }
//
//            if (Blocks.LAPIS_ORE.getLootTableKey() == key && source.isBuiltin()) {
//                return LootTable.builder().build();
//            }
//            if (Blocks.DEEPSLATE_LAPIS_ORE.getLootTableKey() == key && source.isBuiltin()) {
//                return LootTable.builder().build();
//            }
//
//            if (Blocks.GOLD_ORE.getLootTableKey() == key && source.isBuiltin()) {
//                return LootTable.builder().build();
//            }
//            if (Blocks.DEEPSLATE_GOLD_ORE.getLootTableKey() == key && source.isBuiltin()) {
//                return LootTable.builder().build();
//            }
//
//            if (Blocks.REDSTONE_ORE.getLootTableKey() == key && source.isBuiltin()) {
//                return LootTable.builder().build();
//            }
//            if (Blocks.DEEPSLATE_REDSTONE_ORE.getLootTableKey() == key && source.isBuiltin()) {
//                return LootTable.builder().build();
//            }
//
//            if (Blocks.GILDED_BLACKSTONE.getLootTableKey() == key && source.isBuiltin()) {
//                return LootTable.builder().build();
//            }
//            return original;
//        });
    }
}


//                LootTable lootTable = LootTable.builder().
//                        pool(
//                                new LootPool.Builder()
//                                        .with(ItemEntry.builder(Items.DIAMOND))
//                                        .conditionally(SurvivesExplosionLootCondition.builder())
//                        ).build();
//LootTable lootTable = LootTable.CODEC.decode()
