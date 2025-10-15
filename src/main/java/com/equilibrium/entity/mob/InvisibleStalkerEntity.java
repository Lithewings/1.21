package com.equilibrium.entity.mob;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import static com.equilibrium.event.sound.SoundEventRegistry.*;
import static net.minecraft.entity.effect.StatusEffects.*;

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

    //免疫非附魔武器伤害(除此之外,还有黑色史莱姆、凋零骷髅、烈焰人免疫非附魔武器伤害)
    @Override
    public boolean damage(DamageSource source, float amount) {
        if( source.isOf(DamageTypes.PLAYER_ATTACK)) {
            PlayerEntity player = (PlayerEntity)  source.getAttacker();
            ItemStack weapon = player.getMainHandStack();
            if (!weapon.hasEnchantments())
                return false;
        }
        return super.damage(source, amount);
    }

    protected void initThisCustomGoals() {
        this.goalSelector.add(2, new ZombieAttackGoal(this, 1.0, false));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0));
        this.targetSelector.add(1, new RevengeGoal(this).setGroupRevenge(ZombifiedPiglinEntity.class));
        this.targetSelector.add(2, new ActiveTargetGoal(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal(this, IronGolemEntity.class, true));

    }
    private BlockPos findGroundPosition(World world, BlockPos start) {
        // 我们从start开始向下搜索，直到世界底部或者找到合适的地面
        for (int y = start.getY(); start.getY()-y<=32&&y>-64; y--) {
            BlockPos pos = new BlockPos(start.getX(), y, start.getZ());
            if(world.isTopSolid(pos,this))
                return pos.offset(Direction.Axis.Y,1); // 返回这个方块的位置，我们将在其上方生成生物
        }
        // 如果没有找到，返回null
        return null;
    }

    @Override
    //影子潜伏者若生成在主世界,则只能在y<=0的高度生成
    public boolean canSpawn(WorldAccess world, SpawnReason spawnReason) {
        if(this.getWorld().getRegistryKey()== World.OVERWORLD && this.getY()>=0)
            return false;
        if(!this.isOnGround()){
            BlockPos pos = findGroundPosition(this.getWorld(),this.getBlockPos());
            if(pos!=null)
                this.setPosition(pos.getX(),pos.getY(),pos.getZ());
            else
                return false;
        }
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

    @Override
    public void onAttacking(Entity target) {
        super.onAttacking(target);
        if(target instanceof PlayerEntity player){
            player.addStatusEffect(new StatusEffectInstance(BLINDNESS,100,2));
        }
    }


}
