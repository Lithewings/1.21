package com.equilibrium.util;

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

public abstract class LootTableModifier{

    public static void modifierLootTables() {

        LootTableEvents.REPLACE.register((key, original, source) -> {
            if (Blocks.GRAVEL.getLootTableKey() == key && source.isBuiltin()) {
                LootTable lootTable = LootTable.builder().build();
                return lootTable;
            }










            return original;
        });
    }
}






//                LootTable lootTable = LootTable.builder().
//                        pool(
//                                new LootPool.Builder()
//                                        .with(ItemEntry.builder(Items.DIAMOND))
//                                        .conditionally(SurvivesExplosionLootCondition.builder())
//                        ).build();
//LootTable lootTable = LootTable.CODEC.decode()
