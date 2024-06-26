package com.equilibrium.item;


import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class Ingots {

    //以下开始添加物品:
    public static final Item adamantium= new Item(new Item.Settings());
    public static final Item ancient_metal = new Item(new Item.Settings());
    public static final Item copper = new Item(new Item.Settings());
    public static final Item gold = new Item(new Item.Settings());
    public static final Item mithril = new Item(new Item.Settings());
    public static final Item silver = new Item(new Item.Settings());




    public static void registerModItemIngots() {
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","adamantium"), adamantium);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","ancient_metal"), ancient_metal);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","copper"), copper);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","gold"), gold);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","mithril"), mithril);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","silver"), silver);




    }
}
