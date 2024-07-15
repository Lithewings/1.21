package com.equilibrium.item;


import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class Metal {

    //以下开始添加物品:
    public static final Item adamantium= new Item(new Item.Settings());
    public static final Item ancient_metal = new Item(new Item.Settings());
    public static final Item copper = new Item(new Item.Settings());
    public static final Item gold = new Item(new Item.Settings());
    public static final Item mithril = new Item(new Item.Settings());
    public static final Item silver = new Item(new Item.Settings());


    public static final Item adamantium_nugget= new Item(new Item.Settings());
    public static final Item ancient_metal_nugget= new Item(new Item.Settings());
    public static final Item copper_nugget = new Item(new Item.Settings());
    public static final Item gold_nugget = new Item(new Item.Settings());
    public static final Item mithril_nugget = new Item(new Item.Settings());
    public static final Item silver_nugget = new Item(new Item.Settings());

    public static void registerModItemIngots() {
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","adamantium"), adamantium);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","ancient_metal"), ancient_metal);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","copper"), copper);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","gold"), gold);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","mithril"), mithril);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","silver"), silver);
    }
    public static void registerModItemNuggets(){
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","adamantium_nugget"), adamantium_nugget);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","ancient_metal_nugget"), ancient_metal_nugget);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","copper_nugget"), copper_nugget);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","gold_nugget"), gold_nugget);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","mithril_nugget"), mithril_nugget);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","silver_nugget"), silver_nugget);

    }





}
