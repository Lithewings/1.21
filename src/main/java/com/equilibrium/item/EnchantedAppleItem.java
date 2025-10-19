package com.equilibrium.item;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class EnchantedAppleItem extends Item {
    public EnchantedAppleItem(Settings settings) {
        super(settings);
    }
    @Override
    public void onCraftByPlayer(ItemStack stack, World world, PlayerEntity player) {
        super.onCraft(stack, world);
        player.addExperience(-200);
//        player.sendMessage(Text.of("ItemStack is"+stack));
    }


    public static final FoodComponent GOLDEN_APPLE = new FoodComponent.Builder()
            .nutrition(4)
            .saturationModifier(1.2F)
            .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 800, 1), 1.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0), 1.0F)
            .alwaysEdible()
            .build();

}
