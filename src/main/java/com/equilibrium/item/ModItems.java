package com.equilibrium.item;


import net.minecraft.component.type.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static com.equilibrium.MITEequilibrium.MOD_ID;

public class ModItems {
    //Add a “test” Item
    public static final Item test = new Item(new Item.Settings());
    //以下开始添加物品:


    //黑色史莱姆粘液球

    public static final Item PUDDING_SLIME_BALL = new Item(new Item.Settings().maxCount(16));































    public static void registerModItemTest() {
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID,"test"), test);
        //give @s miteequilibrium:test以获取该物品
        //注册名必须小写
        //Identifier.of("miteequilibrium","test")中，第一个是modid，第二个是物品注册id
        //寻找纹理时，"layer0": "miteequilibrium:item/test"，也就是要一一对应

        Registry.register(Registries.ITEM, Identifier.of(MOD_ID,"pudding_slime_ball"), PUDDING_SLIME_BALL);






    }
}

