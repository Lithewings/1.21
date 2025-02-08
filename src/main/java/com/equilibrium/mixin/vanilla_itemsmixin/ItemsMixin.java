package com.equilibrium.mixin.vanilla_itemsmixin;

import com.equilibrium.item.EnchantedAppleItem;
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
    @Shadow
    @Final
    public static Item GOLDEN_APPLE = register("golden_apple", new EnchantedAppleItem(new EnchantedAppleItem.Settings().rarity(Rarity.RARE).food(EnchantedAppleItem.GOLDEN_APPLE)));









//    private final static Item LAPIS_LAZULI = register("lapis_lazuli", new Item(new Item.Settings()));

}
