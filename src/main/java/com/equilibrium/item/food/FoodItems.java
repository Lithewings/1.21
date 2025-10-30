package com.equilibrium.item.food;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static com.equilibrium.MITEequilibrium.MOD_ID;

public class FoodItems {



    public static final Item PUMPKIN_SOUP= new PumpkinSoup(new Item.Settings().food(PumpkinSoup.PUMPKIN_SOUP).maxCount(16));
    public static final Item WATER_BOWL= new WaterBowl(new Item.Settings().maxCount(16));
    public static final Item BEEF_SOUP= new BeefSoup(new Item.Settings().food(BeefSoup.BEEF_SOUP).maxCount(16));
    public static final Item VEGETABLE_SOUP = new BeefSoup(new Item.Settings().food(VegetableSoup.VEGETABLE_SOUP).maxCount(16));



    public static void registerFoodItems() {
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID,"pumpkin_soup"), PUMPKIN_SOUP);
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID,"bowl_water"), WATER_BOWL);
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID,"beef_soup"), BEEF_SOUP);
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID,"vegetable_soup"), VEGETABLE_SOUP);
    }

}
