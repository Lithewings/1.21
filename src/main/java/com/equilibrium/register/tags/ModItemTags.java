package com.equilibrium.register.tags;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModItemTags {

    public static final TagKey<Item> HARVEST_ONE = of("tool_harvest_1");
    public static final TagKey<Item> HARVEST_TWO = of("tool_harvest_2");
    public static final TagKey<Item> HARVEST_THREE = of("tool_harvest_3");
    public static final TagKey<Item> HARVEST_FOUR = of("tool_harvest_4");


    private static TagKey<Item> of(String id) {
        return TagKey.of(RegistryKeys.ITEM, Identifier.of("miteequilibrium",id));
    }


    public static void registerModItemTags(){
    }

}
