package com.equilibrium.mixin.entitymixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LightType;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HostileEntity.class)
public abstract class HostileAttributesMixin extends PathAwareEntity implements Monster {


    protected HostileAttributesMixin(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);

    }



    @Inject(method = "createHostileAttributes",at = @At(value = "HEAD"),cancellable = true)
    private static void createHostileAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.cancel();
        cir.setReturnValue(MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0));
    }
}
