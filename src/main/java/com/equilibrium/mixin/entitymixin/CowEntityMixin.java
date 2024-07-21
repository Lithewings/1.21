package com.equilibrium.mixin.entitymixin;

import com.equilibrium.entity.goal.AdvanceEscapeDangerGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Mixin(CowEntity.class)
public abstract class CowEntityMixin extends AnimalEntity {

    protected CowEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }


    @Inject(method = "initGoals",at = @At(value = "HEAD"),cancellable = true)
    protected void initGoals(CallbackInfo ci) {
        ci.cancel();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new AdvanceEscapeDangerGoal(this, 2.0));
        this.goalSelector.add(2, new AnimalMateGoal(this, 1.0));
        this.goalSelector.add(3, new TemptGoal(this, 1.25, stack -> stack.isIn(ItemTags.COW_FOOD), false));
        this.goalSelector.add(4, new FollowParentGoal(this, 1.25));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
    }










    public boolean isBaby() {
        return this.getBreedingAge() < 0;
    }




    @Override
    public void tick() {
        super.tick();
        milkAmount++;
        if (breedColdDown > 0) {
            breedColdDown--;
        }
    }


    @Unique
    private int milkAmount;



    @Unique
    private int breedColdDown=1000;


    @Unique
    @Override

    public DamageSource getRecentDamageSource() {
        if (this.getWorld().getTime() -this.lastDamageTime > 1600L) {
            this.lastDamageSource = null;
        }
        return this.lastDamageSource;
    }







    @Inject(method = "interactMob",at = @At(value = "HEAD"),cancellable = true)
    public void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) throws InterruptedException {
        cir.cancel();
        //这份代码会被执行两次,一次在客户端一次在服务端
        ItemStack itemStack = player.getStackInHand(hand);


        //以下是拿桶对牛交互的逻辑
        if (itemStack.isOf(Items.BUCKET) && !this.isBaby()) {
            //先执行产奶判断
            //在游戏中完整度过半天,奶牛才会产一桶奶
            //一开始,没有奶,奶量最大为两桶
            //先获取奶量,上限两桶
            milkAmount = min(milkAmount,24000);
            if(milkAmount>=12000) {
                milkAmount=milkAmount-12000;
                player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
                ItemStack itemStack2 = ItemUsage.exchangeStack(itemStack, player, Items.MILK_BUCKET.getDefaultStack());
                player.setStackInHand(hand, itemStack2);
                cir.setReturnValue(ActionResult.success(this.getWorld().isClient));
            }
            else{
                if(this.getWorld().isClient){
                    player.sendMessage(Text.of("The cow is not ready for milking"));
                    player.sendMessage(Text.of(String.valueOf(milkAmount)));
                    player.sendMessage(Text.of("The next bucket of milk is "+(int)(100*(milkAmount/12000F))+"%"));
                }

                cir.setReturnValue(ActionResult.PASS);

            }
        }
        else if (itemStack.isOf(Items.WHEAT) && !this.isBaby()){
            if(breedColdDown==0){
                this.eat(player, hand, itemStack);
                this.lovePlayer(player);
                cir.setReturnValue(ActionResult.SUCCESS);
                milkAmount=24000;
                breedColdDown=1000;
            }else{
                if(breedColdDown>0){
                    if(this.getWorld().isClient){
                        player.sendMessage(Text.of("The cow is full"));
                        cir.setReturnValue(ActionResult.PASS);
                    }
                    cir.setReturnValue(ActionResult.PASS);
                }
                cir.setReturnValue(ActionResult.PASS);
            }


        }else{
            cir.setReturnValue(super.interactMob(player, hand));
        }

    }
}

