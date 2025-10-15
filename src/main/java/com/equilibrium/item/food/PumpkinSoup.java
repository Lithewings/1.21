package com.equilibrium.item.food;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class PumpkinSoup extends Item {
    public PumpkinSoup(Settings settings) {
        super(settings);
    }
    public static final FoodComponent PUMPKIN_SOUP = new FoodComponent.Builder().nutrition(3).saturationModifier(3).build();

    @Override
    public SoundEvent getEatSound() {
        return SoundEvents.ENTITY_GENERIC_DRINK;
    }


    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        super.finishUsing(stack, world, user);
        return new ItemStack(Items.BOWL);
    }
}
