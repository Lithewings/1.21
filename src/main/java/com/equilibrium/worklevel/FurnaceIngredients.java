package com.equilibrium.worklevel;

import java.util.HashMap;

public class FurnaceIngredients {

    public static HashMap<String, FurnaceIngredient> modFuel_ingredients = new HashMap<>();
    public static HashMap<String, FurnaceIngredient> modItem_ingredients = new HashMap<>();

    //不在该集合内的所有物品都为0级燃料，任何熔炉都可以烧
    public static void initFuel() {
        //一级熔炉：石熔炉。
//        register("minecraft:coal", FurnaceIngredient.COBBLESTONE_FURNACE);//煤

        //二级熔炉：黑曜石熔炉
        register("minecraft:lava_bucket", FurnaceIngredient.OBSIDIAN_FURNACE);//岩浆桶

        //三级熔炉：地狱岩熔炉
        register("minecraft:blaze_rod", FurnaceIngredient.NETHERRACK_FURNACE);//烈焰棒

    }

    //不在该集合内的所有物品都为0级物品，可以被任何燃料烧炼
    public static void initItem() {
        //一级物品
        register1("minecraft:copper_ore", FurnaceIngredient.COBBLESTONE_FURNACE);//铜矿
        register1("minecraft:iron_ore", FurnaceIngredient.COBBLESTONE_FURNACE);//
        register1("minecraft:gold_ore", FurnaceIngredient.COBBLESTONE_FURNACE);//
        register1("minecraft:raw_copper", FurnaceIngredient.COBBLESTONE_FURNACE);//铜矿
        register1("minecraft:raw_iron", FurnaceIngredient.COBBLESTONE_FURNACE);//
        register1("minecraft:raw_gold", FurnaceIngredient.COBBLESTONE_FURNACE);//

        //二级物品
        register1("minecraft:deepslate_iron_ore", FurnaceIngredient.OBSIDIAN_FURNACE);//铁矿
        register1("minecraft:deepslate_copper_ore", FurnaceIngredient.OBSIDIAN_FURNACE);//
        register1("minecraft:deepslate_gold_ore", FurnaceIngredient.OBSIDIAN_FURNACE);//
        register1("minecraft:nether_gold_ore", FurnaceIngredient.OBSIDIAN_FURNACE);//
//        register1("tablesandfurnaces:deep_raw_copper", FurnaceIngredient.OBSIDIAN_FURNACE);//
//        register1("tablesandfurnaces:deep_raw_iron", FurnaceIngredient.OBSIDIAN_FURNACE);//
//        register1("tablesandfurnaces:deep_raw_gold", FurnaceIngredient.OBSIDIAN_FURNACE);//

        //三级物品
        register1("minecraft:ancient_debris", FurnaceIngredient.NETHERRACK_FURNACE);//远古残骸

    }

    public static void register(String name, int workbench) {
        modFuel_ingredients.put(name, new FurnaceIngredient(workbench));
    }

    public static void register1(String name, int workbench) {
        modItem_ingredients.put(name, new FurnaceIngredient(workbench));
    }
}
