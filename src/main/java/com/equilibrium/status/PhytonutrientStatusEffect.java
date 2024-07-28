package com.equilibrium.status;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

public class PhytonutrientStatusEffect extends StatusEffect {
    protected PhytonutrientStatusEffect(StatusEffectCategory category, int color) {
        super(
        StatusEffectCategory.HARMFUL, // 药水效果是有益的还是有害的
                5797459);
    }
    private int applyTime = 0;



    // 这个方法在每个 tick 都会调用，以检查是否应应用药水效果
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        if (applyTime>100){
            applyTime=0;
            return true;
        }else{
            applyTime++;
            return false;
        }
    }

    // 这个方法在应用药水效果时会被调用，所以我们可以在这里实现自定义功能。
    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof PlayerEntity) {
            entity.damage(entity.getDamageSources().wither(),1);
            return true;
        }
        return false;
    }
}





