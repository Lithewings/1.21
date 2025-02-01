package com.equilibrium.mixin.player;

import com.equilibrium.MITEequilibrium;
import com.equilibrium.event.MoonPhaseEvent;
import com.equilibrium.item.Tools;
import com.equilibrium.persistent_state.StateSaverAndLoader;
import com.equilibrium.status.registerStatusEffect;
import com.equilibrium.tags.ModBlockTags;
import com.equilibrium.tags.ModItemTags;
import com.equilibrium.util.*;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.Unit;
import net.minecraft.util.math.*;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.tools.Tool;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import static com.equilibrium.event.MoonPhaseEvent.*;

import static com.equilibrium.item.tools_attribute.ExtraDamageFromExperienceLevel.getDamageLevel;
import static com.equilibrium.util.IsMinable.getBlockHarvertLevel;
import static com.equilibrium.util.IsMinable.getItemHarvertLevel;
import static java.lang.Math.max;
import static net.minecraft.component.DataComponentTypes.ITEM_NAME;
import static net.minecraft.component.DataComponentTypes.POTION_CONTENTS;
import static net.minecraft.entity.effect.StatusEffects.SPEED;
import static net.minecraft.potion.Potions.SWIFTNESS;
import static net.minecraft.predicate.entity.EntityPredicates.VALID_LIVING_ENTITY;
import static net.minecraft.registry.tag.EntityTypeTags.UNDEAD;
import static net.minecraft.util.math.MathHelper.nextBetween;

@Mixin(PlayerEntity.class)
//和源码构造方式一致,继承谁这里也跟着继承
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
    @Shadow
    public void resetStat(Stat<?> stat) {
    }
    @Shadow
    public void incrementStat(Identifier stat) {
        this.incrementStat(Stats.CUSTOM.getOrCreateStat(stat));
    }
    @Shadow
    public void incrementStat(Stat<?> stat) {
        this.increaseStat(stat, 1);
    }
    @Shadow
    public void increaseStat(Stat<?> stat, int amount) {
    }
    @Shadow
    public void setLastDeathPos(Optional<GlobalPos> lastDeathPos) {
        this.lastDeathPos = lastDeathPos;
    }
    @Shadow
    private Optional<GlobalPos> lastDeathPos;
    @Shadow
    protected void vanishCursedItems() {
        for (int i = 0; i < this.inventory.size(); i++) {
            ItemStack itemStack = this.inventory.getStack(i);
            if (!itemStack.isEmpty() && EnchantmentHelper.hasAnyEnchantmentsWith(itemStack, EnchantmentEffectComponentTypes.PREVENT_EQUIPMENT_DROP)) {
                this.inventory.removeStack(i);
            }
        }
    }





    @Override
    public void dropInventory() {
        super.dropInventory();
        if ((this.experienceLevel<5)) {
            this.getWorld().getGameRules().get(GameRules.KEEP_INVENTORY).set(false,this.getServer());
            this.vanishCursedItems();
            this.inventory.dropAll();
        }else{
            this.getWorld().getGameRules().get(GameRules.KEEP_INVENTORY).set(true,this.getServer());
            this.experienceLevel = this.experienceLevel>35 ? 35 :0;
        }
    }


    @Inject(method = "attack",at = @At("HEAD"))
    public void attackStart(Entity target, CallbackInfo ci) {
        //经验攻击特效
        float experienceBonus = getDamageLevel(this.experienceLevel);
        //工具攻击特效
        float otherBonus = 1.0F;
        if(this.getMainHandStack().isIn(ModItemTags.DAGGERS) && target instanceof PassiveEntity) {
            otherBonus=1.5F;
        }
        if((this.getMainHandStack().isOf(Tools.SILVER_SWORD)||this.getMainHandStack().isOf(Tools.SILVER_DAGGER) ) && target.getType().isIn(UNDEAD)) {
            otherBonus=1.5F;
        }
        //非独立乘区
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(experienceBonus*otherBonus);

        //例子: 玩家等级为5级,铜短剑的伤害为额外伤害为5点,使用跳斩伤害猪(10点生命)再获得1.5倍率伤害增益
        //基础伤害,面板伤害=(1.25*1.5+5)=6.875
        //最终伤害 = 6.875*1.5 ~ 10.8



    }

    @Inject(method = "attack",at = @At("TAIL"))
    public void attackEnd(Entity target, CallbackInfo ci) {
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(1.0);
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

    @Shadow
    public abstract float getBlockBreakingSpeed(BlockState block);

    @Unique
    public int getPhytonutrient() {
        return (int) this.phytonutrient;
    }

    @Unique
    public void setEntityInteractBonus(float bonus) {
        this.entityInteractBonus = bonus;
    }

    @Inject(method = "canHarvest", at = @At(value = "HEAD"), cancellable = true)
    public void canHarvest(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        cir.cancel();
        cir.setReturnValue(true);
    }



    @Inject(method = "eatFood", at = @At(value = "HEAD"))
    public void eatFood(World world, ItemStack stack, FoodComponent foodComponent, CallbackInfoReturnable<ItemStack> cir) {
        if (stack.isIn(ModItemTags.HARMFOOD)){
            this.phytonutrient-=2000;
            StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.POISON, 400,0,true,true,true);
            this.setStatusEffect(statusEffectInstance,null);
        }
        if (stack.isIn(ModItemTags.PHYTONUTRIENT_LEVEL1)){
            this.phytonutrient+=6000;
        }
        if (stack.isIn(ModItemTags.PHYTONUTRIENT_LEVEL2)){
            this.phytonutrient+=48000;
        }

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


    @Shadow
    public double getEntityInteractionRange() {
        return this.getAttributeValue(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE);
    }
    @Shadow
    private final PlayerAbilities abilities = new PlayerAbilities();
    //加快饥饿速度
    @Inject(method = "addExhaustion", at = @At("HEAD"), cancellable = true)
    public void addExhaustion(float exhaustion, CallbackInfo ci) {
        ci.cancel();
        if (!this.abilities.invulnerable) {
            if (!this.getWorld().isClient) {
                this.hungerManager.addExhaustion(exhaustion*4);
            }
        }
    }

    @Inject(method = "jump", at = @At("TAIL"))
    public void jump(CallbackInfo ci) {
        //202501230630 完成了测试,直接把不可合成的药水名字换成迅捷药水之类的即可,不过最好用translate的那种
//        this.getMainHandStack().set(POTION_CONTENTS, new PotionContentsComponent(Optional.empty(),Optional.empty(),List.of(new StatusEffectInstance(SPEED, 20, 0, true, true))));
//        this.getMainHandStack().set(ITEM_NAME,Text.of("dd"));
//        if(this.getMainHandStack().isIn(ModItemTags.CRAFT_TABLE))
//            this.sendMessage(Text.of("是合成台物品"));









//        if(!this.getWorld().isClient()){
//            StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(this.getWorld().getServer());
//            boolean j =serverState.isPickAxeCrafted;
//            boolean i = ServerInfoRecorder.getDay() >= 16;
//            this.sendMessage(Text.of("Server is loaded ? "+ServerInfoRecorder.isServerInstanceSet()));
//            this.sendMessage(Text.of("Day is more than 16 ? "+i));
//            this.sendMessage(Text.of("isPickAxeCrafted ? "+j));
//        }

//        this.sendMessage(Text.of("Day is "+WorldTimeRecorder.getDay()));
//        moonType = getMoonType(this.getWorld());
//        this.sendMessage(Text.of(moonType));

//        this.sendMessage(Text.of(" the hit range is"+this.getEntityInteractionRange()));
//        this.sendMessage(Text.of(" the pitch is"+this.getPitch()));
//        this.getMainHandStack().damage((int) (this.getMainHandStack().getMaxDamage() * 0.25), this, EquipmentSlot.MAINHAND);
//        this.sendMessage(Text.of(""+this.getEntityInteractionRange()));
//        if (!this.getWorld().isClient) {
//            this.sendMessage(Text.of("" + getPhytonutrient()));
//        }
//        ZombieEntity zombieEntity = new ZombieEntity(this.getWorld());

//        int i = this.random.nextBetween(16,32);
//        BlockPos blockpos1 = BlockPos.ofFloored(this.getPos().add(i,0,i));
//        BlockPos blockpos2 = BlockPos.ofFloored(this.getPos().add(i,0,-i));
//        BlockPos blockpos3 = BlockPos.ofFloored(this.getPos().add(-i,0,i));
//        BlockPos blockpos4 = BlockPos.ofFloored(this.getPos().add(-i,0,-i));
//
//        int j =this.random.nextBetween(1,4);
//        switch (j) {
//            case 1:
//                zombieEntity.setOnGround(true, Vec3d.of(blockpos1));
//                break;
//            case 2:
//                zombieEntity.setOnGround(true, Vec3d.of(blockpos2));
//                break;
//            case 3:
//                zombieEntity.setOnGround(true, Vec3d.of(blockpos3));
//                break;
//            case 4:
//                zombieEntity.setOnGround(true, Vec3d.of(blockpos4));
//                break;
//            default:zombieEntity.setOnGround(true, Vec3d.of(blockpos1));
//        }
//        if(!this.getWorld().isClient)
//            this.phytonutrient=0;
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
            if (itemstack.isIn(ModItemTags.SHOVELS)) {
                //铲子
                setEntityInteractBonus(0.75f);
            } else if (itemstack.isIn(ModItemTags.PICKAXES)) {
                //镐子
                setEntityInteractBonus(0.75f);
            } else if (itemstack.isIn(ModItemTags.AXES)) {
                //斧子
                setEntityInteractBonus(0.75f);
            } else if (itemstack.isIn(ModItemTags.SWORDS)) {
                //剑
                setEntityInteractBonus(0.75f);
            } else if (itemstack.isIn(ModItemTags.HOES)) {
                //锄头
                setEntityInteractBonus(0.75f);
                //手斧
            } else if (itemstack.isIn(ModItemTags.HATCHET)) {
                setEntityInteractBonus(0.5f);
            } else if (itemstack.isIn(ModItemTags.DAGGERS)) {
                //小刀、匕首
                setEntityInteractBonus(0.5f);
            }

        } else if (itemstack.isOf(Items.STICK) || itemstack.isOf(Items.BONE)) {
            //木棍和骨头
            setEntityInteractBonus(0.5f);
        } else {
//            this.sendMessage(Text.of("这是一个平凡的无法附魔的东西"));
            setEntityInteractBonus(0f);
        }
        //潜行向下看时,增加生物交互距离
        if(this.isSneaking() && this.getPitch()>60)
            cir.setReturnValue( 2.5 + entityInteractBonus);
        else{
            cir.setReturnValue((1.5 + entityInteractBonus));
        }
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
                        .add(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE,1.5)
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
    @Shadow
    @Final
    PlayerInventory inventory;


    @Shadow
    @Nullable
    public FishingBobberEntity fishHook;



    @Shadow
    public void addExhaustion(float exhaustion) {
        if (!this.abilities.invulnerable) {
            if (!this.getWorld().isClient) {
                this.hungerManager.addExhaustion(exhaustion);
            }
        }
    }


    @Shadow public abstract boolean shouldCancelInteraction();

    @Shadow public abstract boolean shouldDamagePlayer(PlayerEntity player);

    @Shadow public abstract boolean shouldFilterText();

    @Shadow public abstract void tick();

    @Shadow public abstract boolean isPlayer();

    @Shadow public abstract PlayerAbilities getAbilities();

    @Shadow public abstract void attack(Entity target);


    @Shadow public abstract Either<PlayerEntity.SleepFailureReason, Unit> trySleep(BlockPos pos);

    @Shadow public abstract ItemStack eatFood(World world, ItemStack stack, FoodComponent foodComponent);

    //修改挖掘速度
    @Inject(method = "getBlockBreakingSpeed", at = @At("HEAD"), cancellable = true)
    public void getBlockBreakingSpeed(BlockState block, CallbackInfoReturnable<Float> cir) {
        cir.cancel();
        this.addExhaustion(0.0005f);
        ItemStack stack = this.getMainHandStack();
        float f = this.inventory.getBlockBreakingSpeed(block);
        if (f > 1.0F) {
            f += (float) this.getAttributeValue(EntityAttributes.PLAYER_MINING_EFFICIENCY);
        }

        if (StatusEffectUtil.hasHaste(this)) {
            f *= 1.0F + (float) (StatusEffectUtil.getHasteAmplifier(this) + 1) * 0.2F;
        }

        if (this.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {
            f *= switch (this.getStatusEffect(StatusEffects.MINING_FATIGUE).getAmplifier()) {
                case 0 -> 0.3F;
                case 1 -> 0.09F;
                case 2 -> 0.0027F;
                default -> 8.1E-4F;
            };
        }

        f *= (float) this.getAttributeValue(EntityAttributes.PLAYER_BLOCK_BREAK_SPEED);
        if (this.isSubmergedIn(FluidTags.WATER)) {
            f *= (float) this.getAttributeInstance(EntityAttributes.PLAYER_SUBMERGED_MINING_SPEED).getValue();
        }

        if (!this.isOnGround()) {
            f /= 5.0F;
        }
        if(block.isIn(ModBlockTags.CATEGORY))
            f= f * 8;

        if (stack.isSuitableFor(block)||(stack.isIn(ModItemTags.PICKAXES)&&block.isIn(ModBlockTags.ORE))) {
            f = f * 4;
        }

        this.itemHarvest = getItemHarvertLevel(stack);
        this.blockHarvest = getBlockHarvertLevel(block);
        if (this.itemHarvest >= this.blockHarvest) {

            cir.setReturnValue(f * (0.040F) * (1 + this.experienceLevel * 0.02F));
        } else {
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





    //获取时间,得到月相,决定是否触发月相事件

    private String moonType;




















    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci){
        //你也许可以用这个方法来改进生命值上限/饱食度上限
        //不要在这里使用这个代码,这会使得无时无刻玩家基础伤害固定为这个值从而打不出跳劈伤害,请到武器栏使用这个
//        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(Math.min(1.0 + (this.experienceLevel * 0.01), 1.5));


        if(this.getWorld().getTimeOfDay()%8000==0){
            addExhaustion(4f);
        }
        //刷新上限值,和Hud保持同步,都是1s20次刷新
        refreshPlayerFoodLevelAndMaxHealth();
        if (!this.isCreative()) {
            //在tick中加入生命回复任务
            int maxHealth = PlayerMaxHealthHelper.getMaxHealthLevel();
            setMalnourishedForSlowHealing(
                    this.phytonutrient < 100 ? 4 : 1
            );
            if (this.getHealth() < maxHealth && this.age % (960 * malnourishedForSlowHealing) == 0) {
                this.heal(1.0F);
//                MITEequilibrium.LOGGER.info("Natural Regeneration +1 ");
            }
        }



        moonType = getMoonType(this.getWorld());

        if(moonType!=null) {
            //需要获得最新的世界数据,把worldRegistryKey放到tick里面来
            RegistryKey<World> worldRegistryKey = this.getWorld().getRegistryKey();

            //随机刻速度检查,不是黄月或者蓝月就恢复默认的随机刻速度

            //不在主世界,一定不能获得随机刻增益
            if (worldRegistryKey!=World.OVERWORLD && !this.getWorld().isClient) {
                if (this.getWorld().getGameRules().getInt(GameRules.RANDOM_TICK_SPEED) != 3){
                    this.sendMessage(Text.of("由于不在主世界,随机刻应该修改为3"));
                    RandomTickModifier((ServerWorld) this.getWorld(), 3);
                }
            }
            //月相事件,只在主世界进行
            if (worldRegistryKey == World.OVERWORLD && !this.getWorld().isClient) {

                //普通月相且无随机刻修改逻辑
                boolean randomTickMoonType = (moonType.equals("blueMoon")) || (moonType.equals("harvestMoon")) || (moonType.equals("haloMoon"));
                if (!randomTickMoonType)
                    if(this.getWorld().getGameRules().getInt(GameRules.RANDOM_TICK_SPEED)!=3){

                        this.sendMessage(Text.of("由于处在普通月相,随机刻应该修改为3"));
                        RandomTickModifier((ServerWorld) this.getWorld(), 3);}


                //不是特殊月相,以下不执行
                //改进,月相用枚举类




                if (moonType.equals("bloodMoon")) {
                    //this.getWorld()一定是服务器世界
                    if (this.age % 100 == 0) {
                        //执行间隔事件
                        spawnMobNearPlayer((ServerWorld)this.getWorld());

                    }
                    if (this.age % this.random.nextBetween(50,64) == 0) {
                        //执行间隔事件
                        controlWeather((ServerWorld) this.getWorld());
//                        this.sendMessage(Text.of("雷电事件"));
                    }
                }






                if (moonType.equals("harvestMoon") || (moonType.equals("haloMoon"))) {
                    if(this.getWorld().getGameRules().getInt(GameRules.RANDOM_TICK_SPEED)!=4)
                        RandomTickModifier((ServerWorld) this.getWorld(), 4);
//                    if (this.age % 100 == 0) {
//                        //执行间隔事件
//                        this.sendMessage(Text.of("黄月/幻月升起,触发事件"));

//                    }
                }
                if (moonType.equals("fullMoon")) {
                    if (this.age % 100 == 0) {
//                        this.sendMessage(Text.of("满月升起,触发事件"));
                        applyStrengthToHostileMobs((ServerWorld) this.getWorld());
                    }
                }
                if (moonType.equals("newMoon")) {
                    if (this.age % 100 == 0){
                        applyWeaknessToHostileMobs((ServerWorld) this.getWorld());
//                        this.sendMessage(Text.of("新月升起,触发事件"));
                    }
                }

                //第一次蓝月,不改变随机刻速度
                if (moonType.equals("blueMoon")) {
                    if(this.getWorld().getTimeOfDay()>24000){
                        if(this.getWorld().getGameRules().getInt(GameRules.RANDOM_TICK_SPEED)!=5)
                            RandomTickModifier((ServerWorld) this.getWorld(), 5);
                        if (this.age % 1200 == 0) {
                            this.sendMessage(Text.of("蓝月升起,触发事件"));
                            ServerWorld serverWorld = (ServerWorld) this.getWorld();
                            //执行间隔事件
                            spawnAnimalNearPlayer(serverWorld);
                        }
                    }else{
                        if(this.getWorld().getGameRules().getInt(GameRules.RANDOM_TICK_SPEED)!=3){
                            this.sendMessage(Text.of("由于第一天的蓝月并没有随机刻增益,随机刻应该修改为3"));
                            RandomTickModifier((ServerWorld) this.getWorld(), 3);}
                    }
                    //应该是用world.找到所有玩家,这里无非就是避免客户端世界直接转服务器世界造成崩溃
                    //待改进:应该是this.getWorld,如果不是客户端世界再执行spawnAnimal方法

                }


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
            if (this.phytonutrient < 100) {
                if (!this.hasStatusEffect(registerStatusEffect.PHYTONUTRIENT)) {
                    StatusEffectInstance statusEffectInstance1 = new StatusEffectInstance(registerStatusEffect.PHYTONUTRIENT, -1, 1, false, false, false);
                    StatusEffectUtil.addEffectToPlayersWithinDistance((ServerWorld) this.getWorld(), this, this.getPos(), 16, statusEffectInstance1, -1);

                }
            } else {
                if (this.hasStatusEffect(registerStatusEffect.PHYTONUTRIENT)) {
                    this.removeStatusEffect(registerStatusEffect.PHYTONUTRIENT);
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



