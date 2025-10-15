package com.equilibrium.item.food;

import com.equilibrium.item.Metal;
import com.equilibrium.item.extend_item.BaseCoinItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static com.equilibrium.MITEequilibrium.MOD_ID;
import static com.equilibrium.item.food.PumpkinSoup.PUMPKIN_SOUP;

public class FoodItems {



    public static final Item PumpkinSoup= new PumpkinSoup(new Item.Settings().food(PUMPKIN_SOUP).maxCount(64));


    public static void registerFoodItems() {
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID,"pumpkin_soup"),PumpkinSoup);


    }
}
