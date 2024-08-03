package com.equilibrium.mixin.entitymixin;

import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HostileEntity.class)
public abstract class HostileAttributesMixin {
    @Inject(method = "createHostileAttributes",at = @At(value = "HEAD"),cancellable = true)
    private static void createHostileAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.cancel();
        cir.setReturnValue(MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64.0));
    }
}
