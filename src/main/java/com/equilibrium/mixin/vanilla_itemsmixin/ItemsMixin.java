package com.equilibrium.mixin.vanilla_itemsmixin;

import com.equilibrium.item.EnchantedAppleItem;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Rarity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Optional;

import static net.minecraft.item.Items.register;

@Mixin(Items.class)
public class ItemsMixin {

    //saturationModifier :营养值增加倍率







    @Shadow
    @Final
    public static Item GOLDEN_APPLE = register("golden_apple", new EnchantedAppleItem(new EnchantedAppleItem.Settings().rarity(Rarity.RARE).food(EnchantedAppleItem.GOLDEN_APPLE)));

    @Shadow
    @Final
    public static Item PUMPKIN_PIE = register("pumpkin_pie", new Item(new Item.Settings().food(new FoodComponent(10,12f,false,1.6F, Optional.empty(), List.of()))));



    @Shadow
    @Final
    public static final Item MELON_SLICE = register("melon_slice", new Item(new Item.Settings().food(new FoodComponent.Builder().nutrition(1).saturationModifier(0).build())));


    @Shadow
    @Final
    public static final Item BREAD = register("bread", new Item(new Item.Settings().food(new FoodComponent.Builder().nutrition(2).saturationModifier((float) 8 /4).build())));

    @Shadow
    @Final
    public static final Item BAKED_POTATO = register("baked_potato", new Item(new Item.Settings().food(new FoodComponent.Builder().nutrition(2).saturationModifier((float) 6 /4).build())));


    //    private final static Item LAPIS_LAZULI = register("lapis_lazuli", new Item(new Item.Settings()));

}
