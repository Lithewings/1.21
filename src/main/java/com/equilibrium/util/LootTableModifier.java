package com.equilibrium.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.util.Identifier;

import java.io.FileReader;
import java.io.IOException;

import static com.equilibrium.MITEequilibrium.LOGGER;

public abstract class LootTableModifier {

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
