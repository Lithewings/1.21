package com.equilibrium.mixin.potion;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

import static net.minecraft.potion.Potions.SWIFTNESS;

@Mixin(PotionContentsComponent.class)
public class PotionContentsComponentMixin {
    @Inject(method = "createStack",at = @At("HEAD"))
    private static void createStack(Item item, RegistryEntry<Potion> potion, CallbackInfoReturnable<ItemStack> cir) {
//        cir.cancel();
//
//
//
//        ItemStack itemStack = new ItemStack(item);
//        PotionContentsComponent potionContentsComponent =new PotionContentsComponent(potion);
//        if (potionContentsComponent.hasEffects()) {
//            //药水效果是一个列表,但药水只有一个效果,故只取第一个拿来
//            StatusEffectInstance statusEffect = potionContentsComponent.getEffects().iterator().next();
//            //获取到药水类型
//            RegistryEntry<StatusEffect> potionType = statusEffect.getEffectType();
//            //获取到药水持续时间
//            int duration = statusEffect.getDuration();
//            //获取药水的增益倍数
//            int amplifier = statusEffect.getAmplifier();
//            //创建新定义的增强药水效果
//            StatusEffectInstance boostStatusEffect = new StatusEffectInstance(potionType, duration * 2, amplifier + 1, true, true);
//            //创建药水效果列表
//            List<StatusEffectInstance> customEffects = List.of(boostStatusEffect);
//            //药水增强效果
//            PotionContentsComponent boostPotionContentsComponent = new PotionContentsComponent(Optional.of(SWIFTNESS), Optional.empty(), customEffects);
//
//
//            potionContentsComponent.getEffects().iterator().next();
//            itemStack.set(DataComponentTypes.POTION_CONTENTS, boostPotionContentsComponent);
//
//            cir.setReturnValue(itemStack);
//        }
//        else {
//            itemStack.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(potion));
//            cir.setReturnValue(itemStack);
//        }
    }
}

