package com.equilibrium.item;


import com.equilibrium.item.tools.FlintHatchet;
import com.equilibrium.item.tools.ModToolMaterials;
import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class Tools {

    //以下开始添加物品:
    public static final Item adamantium_axe= new Item(new Item.Settings());
    public static final Item adamantium_battle_axe = new Item(new Item.Settings());
    public static final Item adamantium_dagger = new Item(new Item.Settings());
    public static final Item adamantium_hatchet = new Item(new Item.Settings());


    public static final Item FLINT_HATCHET = new FlintHatchet(ModToolMaterials.FLINT_HATCHET,new Item.Settings().attributeModifiers(FlintHatchet.createAttributeModifiers(ModToolMaterials.FLINT_HATCHET,3,1.0f)));



    public static void registerModItemTools() {
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","adamantium_axe"), adamantium_axe);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","adamantium_battle_axe"), adamantium_battle_axe);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","adamantium_dagger"), adamantium_dagger);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","adamantium_hatchet"), adamantium_hatchet);


        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","flint_hatchet"),FLINT_HATCHET);



    }
}
