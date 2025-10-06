package com.equilibrium.mixin.vanilla_itemsmixin;

import com.equilibrium.item.EnchantedAppleItem;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Rarity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import static net.minecraft.item.Items.register;

@Mixin(Items.class)
public class ItemsMixin {
    private static final FoodComponent PUMPKIN_PIE_FOOD = new FoodComponent.Builder().nutrition(10).saturationModifier(12).build();


    @Shadow
    @Final
    public static Item GOLDEN_APPLE = register("golden_apple", new EnchantedAppleItem(new EnchantedAppleItem.Settings().rarity(Rarity.RARE).food(EnchantedAppleItem.GOLDEN_APPLE)));

    @Shadow
    @Final
    public static Item PUMPKIN_PIE = register("pumpkin_pie", new Item(new Item.Settings().food(PUMPKIN_PIE_FOOD)));



    @Shadow
    @Final
    public static final Item MELON_SLICE = register("melon_slice", new Item(new Item.Settings().food(new FoodComponent.Builder().nutrition(1).saturationModifier(0).build())));


    @Shadow
    @Final
    public static final Item BREAD = register("bread", new Item(new Item.Settings().food(new FoodComponent.Builder().nutrition(2).saturationModifier(8).build())));




    //    private final static Item LAPIS_LAZULI = register("lapis_lazuli", new Item(new Item.Settings()));

}
