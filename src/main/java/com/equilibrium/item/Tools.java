package com.equilibrium.item;


import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class Tools {

    //以下开始添加物品:
    public static final Item adamantium_axe= new Item(new Item.Settings());
    public static final Item adamantium_battle_axe = new Item(new Item.Settings());
    public static final Item adamantium_dagger = new Item(new Item.Settings());
    public static final Item adamantium_hatchet = new Item(new Item.Settings());






    public static void registerModItemTools() {
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","adamantium_axe"), adamantium_axe);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","adamantium_battle_axe"), adamantium_battle_axe);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","adamantium_dagger"), adamantium_dagger);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","adamantium_hatchet"), adamantium_hatchet);






    }
}
