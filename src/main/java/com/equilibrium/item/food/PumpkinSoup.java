package com.equilibrium.item.food;

import com.google.common.collect.ImmutableList;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class PumpkinSoup extends Item {
    public PumpkinSoup(Settings settings) {
        super(settings);
    }
    public static final FoodComponent PUMPKIN_SOUP = new FoodComponent(3,3f,false,1.6F, Optional.of(new ItemStack(Items.BOWL)), List.of());

    @Override
    public SoundEvent getEatSound() {
        return SoundEvents.ENTITY_GENERIC_DRINK;
    }





}
