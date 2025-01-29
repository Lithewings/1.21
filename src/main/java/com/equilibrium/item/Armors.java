package com.equilibrium.item;

import com.equilibrium.item.tools_attribute.ModToolMaterials;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class Armors {


    public static final Item COPPER_HELMET =new ArmorItem(ArmorMaterials.COPPER, ArmorItem.Type.HELMET,new Item.Settings().maxDamage(5*32));
    public static final Item COPPER_CHEST_PLATE =new ArmorItem(ArmorMaterials.COPPER, ArmorItem.Type.CHESTPLATE,new Item.Settings().maxDamage(8*32));
    public static final Item COPPER_LEGGINGS =new ArmorItem(ArmorMaterials.COPPER, ArmorItem.Type.LEGGINGS,new Item.Settings().maxDamage(7*32));
    public static final Item COPPER_BOOTS =new ArmorItem(ArmorMaterials.COPPER, ArmorItem.Type.BOOTS,new Item.Settings().maxDamage(4*32));



    public static void registerArmors(Item item ,String string) {
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium",string), item);

    }
    public static void registerArmors(){
        registerArmors(COPPER_HELMET,"copper_helmet");
        registerArmors(COPPER_CHEST_PLATE,"copper_chest_plate");
        registerArmors(COPPER_LEGGINGS,"copper_leggings");
        registerArmors(COPPER_BOOTS,"copper_boots");
    }

}
