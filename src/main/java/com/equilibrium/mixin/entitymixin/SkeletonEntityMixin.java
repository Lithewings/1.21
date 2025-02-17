package com.equilibrium.mixin.entitymixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSkeletonEntity.class)
public abstract class SkeletonEntityMixin extends HostileEntity implements RangedAttackMob {

    protected SkeletonEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "<init>",at = @At("TAIL"))
    protected void AbstractSkeletonEntity(EntityType entityType, World world, CallbackInfo ci) {
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(4);
        this.getAttributeInstance(EntityAttributes.GENERIC_FALL_DAMAGE_MULTIPLIER).setBaseValue(16);
//        this.getAttributeInstance(EntityAttributes.GENERIC_FOLLOW_RANGE).setBaseValue(64);
//        if(this.getWorld().getRegistryKey()==RegistryKey.of(RegistryKeys.WORLD, Identifier.of("miteequilibrium", "underworld"))){
//        //地下世界追踪距离修正,原本所有怪物追踪距离砍半,但骷髅可以比别的怪物看得更远一点
//            double range = this.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE);
//            this.getAttributeInstance(EntityAttributes.GENERIC_FOLLOW_RANGE).setBaseValue(range+16);
//    }
    }




    @Shadow
    protected PersistentProjectileEntity createArrowProjectile(ItemStack arrow, float damageModifier, @Nullable ItemStack shotFrom) {
        return ProjectileUtil.createArrowProjectile(this, arrow, damageModifier, shotFrom);
    }

    @Shadow public abstract void tickMovement();

    @Inject(method = "getHardAttackInterval",at = @At(value = "HEAD"),cancellable = true)
    protected void getHardAttackInterval(CallbackInfoReturnable<Integer> cir) {
        cir.cancel();
        cir.setReturnValue(20);
    }

    @Inject(method = "getRegularAttackInterval",at = @At(value = "HEAD"),cancellable = true)
    protected void getRegularAttackInterval(CallbackInfoReturnable<Integer> cir) {
        cir.cancel();
        cir.setReturnValue(40);
    }

//    @Inject(method = "shootAt",at = @At(value = "HEAD"),cancellable = true)
//    public void shootAt(LivingEntity target, float pullProgress, CallbackInfo ci) {
//        ci.cancel();
//        ItemStack itemStack = this.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this, Items.BOW));
//        ItemStack itemStack2 = this.getProjectileType(itemStack);
//
//
//        PersistentProjectileEntity persistentProjectileEntity = this.createArrowProjectile(itemStack2, pullProgress, itemStack);
//
//
//        double x = target.getX()-this.getX();
//        double y = target.getZ()-this.getZ();
//        double distance = Math.sqrt(x * x + y * y);
//        float preTime = (float) (distance/32);
//
//
//
//        double d = target.getX()+(target.getVelocity().getX()*8*preTime) -this.getX();
//        double e = target.getBodyY(0.3333333333333333) - persistentProjectileEntity.getY();
//        double f = target.getZ()+(target.getVelocity().getZ()*8*preTime) - this.getZ();
//        double g = Math.sqrt(d * d + f * f);
//        g=distance>16?g*1.2:g;
//        persistentProjectileEntity.setVelocity(d, e + g * 0.2F, f, 1.6F, 0);
//
//        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
//        this.getWorld().spawnEntity(persistentProjectileEntity);
//
//    }
}
