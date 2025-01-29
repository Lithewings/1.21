package com.equilibrium.mixin.entitymixin;

import com.equilibrium.MITEequilibrium;
import com.equilibrium.item.Tools;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndCrystalEntity.class)
public class EndCrystalEntityMixin {
    @Inject(method = "damage",at = @At("HEAD"),cancellable = true)
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if(source.getAttacker() instanceof PlayerEntity player && player.getMainHandStack().isOf(Tools.ADMANTIUM_PICKAXE))
            return;
        else{
            cir.setReturnValue(false);
        }

    }
}
