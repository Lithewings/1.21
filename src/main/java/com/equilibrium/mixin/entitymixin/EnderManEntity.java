package com.equilibrium.mixin.entitymixin;

import com.equilibrium.entity.goal.EndermanAlwaysAngryAtPlayerGoal;
import com.equilibrium.persistent_state.StateSaverAndLoader;
import com.equilibrium.util.ServerInfoRecorder;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.equilibrium.entity.goal.EndermanAlwaysAngryAtPlayerGoal.shouldAlwaysAngryAtPlayer;

@Mixin(EndermanEntity.class)
public abstract class EnderManEntity extends HostileEntity implements Angerable {
    @Shadow public abstract boolean hurtByWater();

    @Shadow public abstract void setTarget(@Nullable LivingEntity target);

    @Shadow public abstract boolean isAngry();

    protected EnderManEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initGoals", at = @At("HEAD"))
    protected void initGoals(CallbackInfo ci) {
        //玩家死亡超过一定次数,末影人会无缘无故对其发动攻击
        this.targetSelector.add(3, new EndermanAlwaysAngryAtPlayerGoal<>(this, PlayerEntity.class, true, false));
    }


    @Inject(method = "mobTick", at = @At("HEAD"))
    protected void mobTick(CallbackInfo ci) {
        //末影人被盯着时不会再引起仇恨了
        if (this.isAngry() && this.getTarget() instanceof PlayerEntity player && !player.getInventory().contains(Items.ENDER_PEARL.getDefaultStack()))
            this.setTarget(null);
    }



}
