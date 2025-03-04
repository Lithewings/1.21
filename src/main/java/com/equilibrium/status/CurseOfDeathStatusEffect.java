package com.equilibrium.status;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Random;

public class CurseOfDeathStatusEffect extends StatusEffect {
    private int effect;

    protected CurseOfDeathStatusEffect() {
        super(StatusEffectCategory.HARMFUL, // 药水效果是有益的还是有害的
                5797459);

        Random random = new Random();
        this.effect= random.nextInt(10);;
    }


    // 这个方法在每个 tick 都会调用，以检查是否应应用药水效果
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void onEntityRemoval(LivingEntity entity, int amplifier, Entity.RemovalReason reason) {
    }

    // 这个方法在应用药水效果时会被调用，所以我们可以在这里实现自定义功能。
    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        return false;
    }








}
