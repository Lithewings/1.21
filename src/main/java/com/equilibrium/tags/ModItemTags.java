package com.equilibrium.tags;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModItemTags {

    public static final TagKey<Item> HARVEST_ONE = of("tool_harvest_1");
    public static final TagKey<Item> HARVEST_TWO = of("tool_harvest_2");
    public static final TagKey<Item> HARVEST_THREE = of("tool_harvest_3");
    public static final TagKey<Item> HARVEST_FOUR = of("tool_harvest_4");
    public static final TagKey<Item> HARVEST_FIVE = of("tool_harvest_5");






    public static final TagKey<Item> HATCHET = of("hatchet");
    public static final TagKey<Item> AXES = of("axe");
    public static final TagKey<Item> PICKAXES = of("pickaxe");
    public static final TagKey<Item> SWORDS = of("sword");
    public static final TagKey<Item> HOES = of("hoe");
    public static final TagKey<Item> SHOVELS = of("shovel");
    public static final TagKey<Item> DAGGERS = of("dagger");

    public static final TagKey<Item> REMOVEITEM = of("remove_item");




    public static final TagKey<Item> MINING_ENCHANTABLE = of("mining_enchantable");

    public static final TagKey<Item> UNBREAKING = of("unbreaking");
    public static final TagKey<Item> UNBREAKING_ENCHANTABLE = of("unbreaking_enchantable");

    //铁砧上可以生效的
    public static final TagKey<Item> FORTUNE = of("fortune");
    //只能在附魔台附魔的,应该是铁砧的子集
    public static final TagKey<Item> FORTUNE_ENCHANTABLE = of("fortune_enchantable");

    public static final TagKey<Item> SILKTOUCH = of("silktouch.json");


    private static TagKey<Item> of(String id) {
        return TagKey.of(RegistryKeys.ITEM, Identifier.of("miteequilibrium",id));
    }


    public static void registerModItemTags(){
    }

}
