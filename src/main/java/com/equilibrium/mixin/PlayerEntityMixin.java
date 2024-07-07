package com.equilibrium.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)

//和源码构造方式一致,继承谁这里也跟着继承
public abstract class PlayerEntityMixin extends LivingEntity {


    @Shadow public abstract int getNextLevelExperience();

    @Shadow public int experienceLevel;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
//调用CallbackInfo类,修改返回值
//以下是修改方块交互距离
    @Inject(method = "getBlockInteractionRange", at = @At("HEAD"), cancellable = true)
    public void getBlockInteractionRange(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(3.0);
    }
//以下修改实体交互距离
    @Inject(method = "getEntityInteractionRange", at = @At("HEAD"), cancellable = true)
    public void getEntityInteractionRange(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(1.0);
    }


    @Inject(method = "getBlockBreakingSpeed", at = @At("RETURN"), cancellable = true)
    public void getBlockBreakingSpeed(BlockState block, CallbackInfoReturnable<Float> cir) {
      int level = this.experienceLevel;
      float speed = cir.getReturnValue();
        System.out.println(speed);
      float finalSpeed = speed*(0.025F)*(1+level*0.02F);
        System.out.println(finalSpeed);
      cir.setReturnValue(finalSpeed);
    }

};
