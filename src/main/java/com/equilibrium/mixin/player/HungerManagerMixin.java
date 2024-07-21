package com.equilibrium.mixin.player;

import com.equilibrium.util.PlayerMaxHungerHelper;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.equilibrium.MITEequilibrium.LOGGER;

@Mixin(HungerManager.class)







public abstract class HungerManagerMixin {






    //营养值没满,就能一直吃下去
    @Inject(method = "isNotFull",at = @At(value = "HEAD"),cancellable = true)
    public void isNotFull(CallbackInfoReturnable<Boolean> cir) {

        int maxFoodLevel= PlayerMaxHungerHelper.getMaxFoodLevel();

        cir.setReturnValue(this.saturationLevel < maxFoodLevel);

    }

    @Shadow
    private int foodLevel;
    @Shadow
    private float saturationLevel;
    @Shadow
    private float exhaustion;
    @Shadow
    private int foodTickTimer;
    @Shadow
    private int prevFoodLevel ;

    @Inject(method = "addInternal",at = @At("HEAD"), cancellable = true)
    private void addInternal(int nutrition, float saturation, CallbackInfo ci) {
        ci.cancel();
        //获取饱食度上限
        int maxFoodLevel = PlayerMaxHungerHelper.getMaxFoodLevel();
        //即便饱食度已满,也可以继续吃东西,将饱食度加在饱和度上
        LOGGER.info("The food attributes: "+nutrition+" "+saturation);

        LOGGER.info("The foodLevel before: "+this.foodLevel);
        LOGGER.info("The saturationLevel before: "+this.saturationLevel);

        //先临时增加16倍的饱食度上限
        this.foodLevel = MathHelper.clamp(nutrition + this.foodLevel, 0, 16*maxFoodLevel);
        //若发生溢出
        if(this.foodLevel>maxFoodLevel){
            //获取溢出值
            int overflowFoodLevel = foodLevel-maxFoodLevel;
            LOGGER.info("overflowFoodLevel = "+overflowFoodLevel);
            //加到饱和度上
            this.saturationLevel = MathHelper.clamp(overflowFoodLevel + this.saturationLevel, 0.0F,maxFoodLevel);
            //还原到溢出前的最大值
            this.foodLevel=maxFoodLevel;
        }
        this.saturationLevel = MathHelper.clamp(saturation + this.saturationLevel, 0.0F, maxFoodLevel);


        LOGGER.info("The foodLevel after: "+this.foodLevel);
        LOGGER.info("The saturationLevel after: "+this.saturationLevel);
    }
}
