package com.equilibrium.status;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class PhytonutrientStatusEffect extends StatusEffect {
    protected PhytonutrientStatusEffect() {
        super(
        StatusEffectCategory.HARMFUL, // 药水效果是有益的还是有害的
                5797459);
    }


    // 这个方法在每个 tick 都会调用，以检查是否应应用药水效果
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }



    // 这个方法在应用药水效果时会被调用，所以我们可以在这里实现自定义功能。
    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof PlayerEntity playerEntity) {

            //玩家在复杂移动时施加饥饿度
            if(playerEntity.isSprinting()||playerEntity.isClimbing()||playerEntity.isSwimming()||playerEntity.speed>0){
                playerEntity.addExhaustion(0.0010f * (float)(amplifier + 1));
                return true;
            }
            else{
                playerEntity.addExhaustion(0.0003f * (float)(amplifier + 1));
                return true;
            }
        }
        return false;
    }

}





