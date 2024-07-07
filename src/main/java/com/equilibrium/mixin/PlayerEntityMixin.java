package com.equilibrium.mixin;

import com.equilibrium.util.PlayerMaxHealthHelper;
import com.equilibrium.util.PlayerMaxHungerHelper;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)

//和源码构造方式一致,继承谁这里也跟着继承
public abstract class PlayerEntityMixin extends LivingEntity {


    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "<init>",at=@At("TAIL"))
    public void PlayerEntity(World world, BlockPos pos, float yaw, GameProfile gameProfile, CallbackInfo ci){

    }

    @Inject(method = "jump", at = @At("TAIL"))
    public void jump(CallbackInfo ci) {


    }


    @Shadow
    public int totalExperience;
    @Shadow
    private int lastPlayedLevelUpSoundTime;
    @Shadow
    public float experienceProgress;

    @Shadow
    public int experienceLevel;

    @Shadow
    protected HungerManager hungerManager;

    @Shadow public abstract void remove(RemovalReason reason);

    @Shadow public abstract boolean isPlayer();

    @Shadow @Final private static Logger LOGGER;

    @Shadow public abstract void tick();



    @Shadow @Final private PlayerAbilities abilities;


    @Shadow public abstract boolean isCreative();

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

    //玩家基础属性
    @Inject(method = "createPlayerAttributes", at = @At("HEAD"), cancellable = true)
    private static void createPlayerAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.setReturnValue(
                LivingEntity.createLivingAttributes()
                        .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0)
                        .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1F)
                        .add(EntityAttributes.GENERIC_ATTACK_SPEED)
                        .add(EntityAttributes.GENERIC_LUCK)
                        .add(EntityAttributes.GENERIC_MAX_HEALTH,20)
                        .add(EntityAttributes.PLAYER_BLOCK_INTERACTION_RANGE)
                        .add(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE)
                        .add(EntityAttributes.PLAYER_BLOCK_BREAK_SPEED)
                        .add(EntityAttributes.PLAYER_SUBMERGED_MINING_SPEED)
                        .add(EntityAttributes.PLAYER_SNEAKING_SPEED)
                        .add(EntityAttributes.PLAYER_MINING_EFFICIENCY)
                        .add(EntityAttributes.PLAYER_SWEEPING_DAMAGE_RATIO)



        );

    }





    //修改挖掘速度
    @Inject(method = "getBlockBreakingSpeed", at = @At("RETURN"), cancellable = true)
    public void getBlockBreakingSpeed(BlockState block, CallbackInfoReturnable<Float> cir) {
        hungerManager.setFoodLevel(0);
        hungerManager.setSaturationLevel(0);

        int level = this.experienceLevel;
        float speed = cir.getReturnValue();
        System.out.println(speed);
        float finalSpeed = speed * (0.025F) * (1 + level * 0.02F);
        System.out.println(finalSpeed);
        cir.setReturnValue(finalSpeed);
    }

    @Inject(method = "getNextLevelExperience", at = @At("HEAD"), cancellable = true)
    //经验曲线
    public void getNextLevelExperience(CallbackInfoReturnable<Integer> cir) {
        int level = this.experienceLevel;
        int nextLevel = 10*(level + 1 );
        cir.setReturnValue(nextLevel);

    }


    public void refreshPlayerFoodLevelAndMaxHealth(){
        int maxFoodLevel=this.experienceLevel >=35 ? 20 : 6 +(int)(this.experienceLevel/5)*2;
//        LOGGER.info("setMaxFoodLevel is already triggered,and the Level is "+this.experienceLevel+"Finally the maxFoodLevel is"+maxFoodLevel);
        PlayerMaxHungerHelper.setMaxFoodLevel(maxFoodLevel);

        int maxHealthLevel=this.experienceLevel >=35 ? 20 : 6 +(int)(this.experienceLevel/5)*2;
//        LOGGER.info("setMaxHealthLevel is already triggered,and the Level is "+this.experienceLevel+"Finally the maxHealthLevel is"+maxFoodLevel);
        PlayerMaxHealthHelper.setMaxHealthLevel(maxHealthLevel);
    }

    @Inject(method = "addExperienceLevels", at = @At("HEAD"), cancellable = true)
    public void addExperienceLevels(int levels, CallbackInfo ci) {
        //触发器,用作增加经验值的后续处理,比如增加生命值和饥饿度等
        //除了初始化会增加上限之外,只有该方法才能增加上限值
        ci.cancel();
        this.experienceLevel += levels;
        if (this.experienceLevel < 0) {
            this.experienceLevel = 0;
            this.experienceProgress = 0.0F;
            this.totalExperience = 0;
        }
        if (levels > 0 && this.experienceLevel % 5 == 0){
            refreshPlayerFoodLevelAndMaxHealth();

        }

        if (levels > 0 && this.experienceLevel % 5 == 0 && (float)this.lastPlayedLevelUpSoundTime < (float)this.age - 100.0F) {
            float f = this.experienceLevel > 30 ? 1.0F : (float)this.experienceLevel / 30.0F;
            this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PLAYER_LEVELUP, this.getSoundCategory(), f * 0.75F, 1.0F);
            this.lastPlayedLevelUpSoundTime = this.age;
        }
    }


    @Inject(method = "canFoodHeal", at = @At("HEAD"), cancellable = true)
    public void canFoodHeal(CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(false);
    }
    @Inject(method = "tickMovement",at = @At("HEAD"))
    public void tickMovement(CallbackInfo ci){
        //刷新上限值,和Hud保持同步,都是1s20次刷新
        refreshPlayerFoodLevelAndMaxHealth();

        if (this.getWorld().getDifficulty() != Difficulty.PEACEFUL){
            //在tick中加入生命回复任务
            int maxHealth= PlayerMaxHealthHelper.getMaxHealthLevel();
            if (this.getHealth() < maxHealth && this.age % 20 == 0) {
                this.heal(1.0F);
                LOGGER.info("Natural Regeneration +1 ");
            }
        }


    }




    @Override
    //自然回复设定
    public void setHealth(float health) {
        LOGGER.info("Before setHealth, the xp level is "+this.experienceLevel);
        LOGGER.info("Set health : "+health);
        int maxHealth=PlayerMaxHealthHelper.getMaxHealthLevel();
        this.dataTracker.set(HEALTH, MathHelper.clamp(health, 0.0F, maxHealth));
    }

}



