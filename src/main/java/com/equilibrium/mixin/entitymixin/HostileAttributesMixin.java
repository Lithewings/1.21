package com.equilibrium.mixin.entitymixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HostileEntity.class)
public abstract class HostileAttributesMixin extends PathAwareEntity implements Monster {


    protected HostileAttributesMixin(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);


    }

    @Inject(method = "<init>",at = @At("TAIL"))
    protected void HostileEntity(EntityType entityType, World world, CallbackInfo ci) {
        if(this.getWorld().getRegistryKey()== RegistryKey.of(RegistryKeys.WORLD, Identifier.of("miteequilibrium", "underworld"))){
            //地下世界,怪物追踪距离减少
            double range = this.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE);
            this.getAttributeInstance(EntityAttributes.GENERIC_FOLLOW_RANGE).setBaseValue(range*0.75);
    }
    }



}
