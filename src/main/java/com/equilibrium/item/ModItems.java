package com.equilibrium.item;


import com.equilibrium.MITEequilibrium;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    //Add a “test” Item
    public static final Item test = new Item(new Item.Settings());
    //以下开始添加物品:



































    public static void registerModItemTest() {
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","test"), test);
        //give @s miteequilibrium:test以获取该物品
        //注册名必须小写
        //Identifier.of("miteequilibrium","test")中，第一个是modid，第二个是物品注册id
        //寻找纹理时，"layer0": "miteequilibrium:item/test"，也就是要一一对应
    }
}

