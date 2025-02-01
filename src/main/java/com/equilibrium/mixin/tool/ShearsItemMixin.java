package com.equilibrium.mixin.tool;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ShearsItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ShearsItem.class)
public class ShearsItemMixin extends Item {
    public ShearsItemMixin(Settings settings) {
        super(settings);
    }

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;<init>(Lnet/minecraft/item/Item$Settings;)V"))
    private static Settings ShearsItem(Settings settings) {
        return new Item.Settings().maxDamage(6400).component(DataComponentTypes.TOOL, ShearsItem.createToolComponent());

    }




}
