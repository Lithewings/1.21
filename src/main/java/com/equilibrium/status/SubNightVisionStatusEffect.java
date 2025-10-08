package com.equilibrium.status;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

public class SubNightVisionStatusEffect  extends StatusEffect {
    protected SubNightVisionStatusEffect(StatusEffectCategory category, int color) {
        super(
                StatusEffectCategory.NEUTRAL, // 药水效果是有益的还是有害的
                870520);
    }
    // 这个方法在应用药水效果时会被调用，所以我们可以在这里实现自定义功能。
    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        return false;
    }






}
