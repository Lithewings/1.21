package com.equilibrium.mixin.player;

import com.equilibrium.register.tags.ModBlockTags;
import com.equilibrium.register.tags.ModItemTags;
import com.equilibrium.util.PlayerMaxHealthHelper;
import com.equilibrium.util.PlayerMaxHungerHelper;
import com.equilibrium.util.ShouldSentText;
import net.minecraft.block.BlockState;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.equilibrium.util.IsMinable.getBlockHarvertLevel;
import static com.equilibrium.util.IsMinable.getItemHarvertLevel;

@Mixin(PlayerEntity.class)
//和源码构造方式一致,继承谁这里也跟着继承
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    //植物营养素
    @Unique
    public long phytonutrient = 192000;
    //生物交互距离增益
    @Unique
    public float entityInteractBonus = 0;


    //营养不良造成的缓慢回血倍率
    @Unique
    public float malnourishedForSlowHealing;

    public float getMalnourishedForSlowHealing() {
        return this.malnourishedForSlowHealing;
    }

    public void setMalnourishedForSlowHealing(float factor) {
        this.malnourishedForSlowHealing = factor;
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

    @Shadow
    public abstract void sendMessage(Text message, boolean overlay);

    @Shadow
    public abstract void tickMovement();

    @Shadow
    public abstract boolean isCreative();

    @Shadow
    public abstract boolean damage(DamageSource source, float amount);

    @Shadow public abstract float getBlockBreakingSpeed(BlockState block);

    @Unique
    public int getPhytonutrient() {
        return (int) this.phytonutrient;
    }

    @Unique
    public void setEntityInteractBonus(float bonus) {
        this.entityInteractBonus = bonus;
    }


    @Inject(method = "eatFood", at = @At(value = "HEAD"))
    public void eatFood(World world, ItemStack stack, FoodComponent foodComponent, CallbackInfoReturnable<ItemStack> cir) {
        if (stack.isOf(Items.APPLE))
            this.phytonutrient += 1000;
    }


    //服务端调用
    @Inject(method = "readCustomDataFromNbt", at = @At(value = "TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        super.readCustomDataFromNbt(nbt);
        this.phytonutrient = nbt.getInt("Phytonutrient");
    }

    @Inject(method = "writeCustomDataToNbt", at = @At(value = "TAIL"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Phytonutrient", (int) this.phytonutrient);
    }

    @Inject(method = "jump", at = @At("TAIL"))
    public void jump(CallbackInfo ci) {
        this.getMainHandStack().damage((int) (this.getMainHandStack().getMaxDamage()*0.25),this, EquipmentSlot.MAINHAND);
//        if (!this.getWorld().isClient) {
//            this.sendMessage(Text.of("" + getPhytonutrient()));
//        }

    }

    //以下是修改方块交互距离
    @Inject(method = "getBlockInteractionRange", at = @At("HEAD"), cancellable = true)
    public void getBlockInteractionRange(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(3.0);

    }

    //以下修改实体交互距离
    @Inject(method = "getEntityInteractionRange", at = @At("HEAD"), cancellable = true)
    public void getEntityInteractionRange(CallbackInfoReturnable<Double> cir) {
        ItemStack itemstack = this.getMainHandStack();
        if (itemstack.isEnchantable()) {

            if (itemstack.isIn(ItemTags.SHOVELS)) {
                //铲子
                setEntityInteractBonus(0.75f);
            } else if (itemstack.isIn(ItemTags.PICKAXES)) {
                //镐子
                setEntityInteractBonus(0.75f);
            } else if (itemstack.isIn(ItemTags.AXES)) {
                //斧子
                setEntityInteractBonus(0.75f);
            } else if (itemstack.isIn(ItemTags.SWORDS)) {
                //剑
                setEntityInteractBonus(0.75f);
            } else if (itemstack.isIn(ItemTags.HOES)) {
                //锄头
                setEntityInteractBonus(0.75f);
            }

        } else if (itemstack.isOf(Items.STICK) || itemstack.isOf(Items.BONE)) {
            //木棍和骨头
            setEntityInteractBonus(0.5f);
        } else {
//            this.sendMessage(Text.of("这是一个平凡的无法附魔的东西"));
            setEntityInteractBonus(0f);
        }
        cir.setReturnValue(1.0 + entityInteractBonus);
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
                        .add(EntityAttributes.GENERIC_MAX_HEALTH, 20)
                        .add(EntityAttributes.PLAYER_BLOCK_INTERACTION_RANGE)
                        .add(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE)
                        .add(EntityAttributes.PLAYER_BLOCK_BREAK_SPEED)
                        .add(EntityAttributes.PLAYER_SUBMERGED_MINING_SPEED)
                        .add(EntityAttributes.PLAYER_SNEAKING_SPEED)
                        .add(EntityAttributes.PLAYER_MINING_EFFICIENCY)
                        .add(EntityAttributes.PLAYER_SWEEPING_DAMAGE_RATIO)


        );


    }





    @Unique
    private int itemHarvest;
    @Unique
    private int blockHarvest;




    //修改挖掘速度
    @Inject(method = "getBlockBreakingSpeed", at = @At("RETURN"), cancellable = true)
    public void getBlockBreakingSpeed(BlockState block, CallbackInfoReturnable<Float> cir) {

        ItemStack stack = this.getMainHandStack();
        this.itemHarvest=getItemHarvertLevel(stack);
        this.blockHarvest=getBlockHarvertLevel(block);
        if(this.itemHarvest>=this.blockHarvest){
            cir.setReturnValue(speed * (0.025F) * (1 + this.experienceLevel * 0.02F));
        }else{
            cir.setReturnValue(0f);
        }

//        if (!this.getWorld().isClient) {
//            this.sendMessage(Text.of("" + getPhytonutrient()));
//        }

    }

    @Inject(method = "getNextLevelExperience", at = @At("HEAD"), cancellable = true)
    //经验曲线
    public void getNextLevelExperience(CallbackInfoReturnable<Integer> cir) {
        int level = this.experienceLevel;
        int nextLevel = 10 * (level + 1);
        cir.setReturnValue(nextLevel);

    }


    public void refreshPlayerFoodLevelAndMaxHealth() {
        int maxFoodLevel = this.experienceLevel >= 35 ? 20 : 6 + (int) (this.experienceLevel / 5) * 2;
//        LOGGER.info("setMaxFoodLevel is already triggered,and the Level is "+this.experienceLevel+"Finally the maxFoodLevel is"+maxFoodLevel);
        PlayerMaxHungerHelper.setMaxFoodLevel(maxFoodLevel);

        int maxHealthLevel = this.experienceLevel >= 35 ? 20 : 6 + (int) (this.experienceLevel / 5) * 2;
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
        if (levels > 0 && this.experienceLevel % 5 == 0) {
            refreshPlayerFoodLevelAndMaxHealth();

        }

        if (levels > 0 && this.experienceLevel % 5 == 0 && (float) this.lastPlayedLevelUpSoundTime < (float) this.age - 100.0F) {
            float f = this.experienceLevel > 30 ? 1.0F : (float) this.experienceLevel / 30.0F;
            this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PLAYER_LEVELUP, this.getSoundCategory(), f * 0.75F, 1.0F);
            this.lastPlayedLevelUpSoundTime = this.age;
        }
    }


    @Inject(method = "canFoodHeal", at = @At("HEAD"), cancellable = true)
    public void canFoodHeal(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }


    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        //刷新上限值,和Hud保持同步,都是1s20次刷新
        refreshPlayerFoodLevelAndMaxHealth();

        if (!this.isCreative()) {
            //在tick中加入生命回复任务
            int maxHealth = PlayerMaxHealthHelper.getMaxHealthLevel();
            setMalnourishedForSlowHealing(
                    this.phytonutrient < 100 ? 4 : 1
            );
            if (this.getHealth() < maxHealth && this.age % (40 * malnourishedForSlowHealing) == 0) {
                this.heal(1.0F);
//                MITEequilibrium.LOGGER.info("Natural Regeneration +1 ");
            }
        }
        ShouldSentText.count++;
        if (!this.getWorld().isClient) {
            this.phytonutrient--;
            //小于0就赋值为0,大于0不动
            this.phytonutrient = this.phytonutrient < 0 ? 0 : this.phytonutrient;
            //溢出判断,大于192000就为192000,否则不动
            this.phytonutrient = this.phytonutrient > 192000 ? 192000 : this.phytonutrient;
            //施加饥饿效果
            if (this.phytonutrient<100) {
                if(!this.hasStatusEffect(StatusEffects.HUNGER)){
                    StatusEffectInstance statusEffectInstance1 = new StatusEffectInstance(StatusEffects.HUNGER, -1,1, false,false,false);
                    StatusEffectUtil.addEffectToPlayersWithinDistance((ServerWorld) this.getWorld(), this, this.getPos(), 4, statusEffectInstance1,-1);

                }
            }else{
                if(this.hasStatusEffect(StatusEffects.HUNGER)){
                    this.removeStatusEffect(StatusEffects.HUNGER);
                }
            }


        }
    }


    @Inject(method = "tickMovement", at = @At("HEAD"))
    public void tickMovement(CallbackInfo ci) {
    }


    @Override
    //自然回复设定
    public void setHealth(float health) {
//        MITEequilibrium.LOGGER.info("Before setHealth, the xp level is "+this.experienceLevel);
//        MITEequilibrium.LOGGER.info("Set health : "+health);
        int maxHealth = PlayerMaxHealthHelper.getMaxHealthLevel();
        this.dataTracker.set(HEALTH, MathHelper.clamp(health, 0.0F, maxHealth));
    }







}



