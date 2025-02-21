package com.equilibrium.entity.mob;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import static com.equilibrium.event.sound.SoundEventRegistry.*;

public class InvisibleStalkerEntity extends ZombieEntity {
    public InvisibleStalkerEntity(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }
    @Override
    protected void initGoals() {
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.initThisCustomGoals();
    }
    @Override
    protected boolean canConvertInWater() {
        return false;
    }

    protected void initThisCustomGoals() {
        this.goalSelector.add(2, new ZombieAttackGoal(this, 1.0, false));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0));
        this.targetSelector.add(1, new RevengeGoal(this).setGroupRevenge(ZombifiedPiglinEntity.class));
        this.targetSelector.add(2, new ActiveTargetGoal(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal(this, IronGolemEntity.class, true));

    }

    @Override
    //影子潜伏者若生成在主世界,则只能在y<=0的高度生成
    public boolean canSpawn(WorldAccess world, SpawnReason spawnReason) {
        if(this.getWorld().getRegistryKey()== World.OVERWORLD && this.getY()>=0)
            return false;
        return super.canSpawn(world, spawnReason);
    }


    @Override
    protected void initEquipment(Random random, LocalDifficulty localDifficulty) {
    }

    @Override
    public boolean canPickupItem(ItemStack stack) {
        return false;
    }

    @Override
    public void setBaby(boolean baby) {}

    protected SoundEvent getStepSound() {
        //无声
        return SoundEvents.INTENTIONALLY_EMPTY;
    }
    @Override
    protected SoundEvent getAmbientSound() {
        return switch (1 + this.getRandom().nextInt(2)) {
            case 1 -> ENTITY_INVISIBLE_STALKER_AMBIENT1;
            case 2 -> ENTITY_INVISIBLE_STALKER_AMBIENT2;
            case 3 -> ENTITY_INVISIBLE_STALKER_AMBIENT3;
            default -> SoundEvents.INTENTIONALLY_EMPTY;
        };
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return switch (1 + this.getRandom().nextInt(1)) {
            case 1 -> ENTITY_INVISIBLE_STALKER_HURT1;
            case 2 -> ENTITY_INVISIBLE_STALKER_HURT2;
            default -> SoundEvents.INTENTIONALLY_EMPTY;
        };
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ENTITY_INVISIBLE_STALKER_DEATH;
    }




}
