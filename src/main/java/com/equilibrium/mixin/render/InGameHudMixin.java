package com.equilibrium.mixin.render;

import com.equilibrium.util.PlayerMaxHealthOrFoodLevelHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow
    private static final Identifier FOOD_EMPTY_HUNGER_TEXTURE = Identifier.ofVanilla("hud/food_empty_hunger");
    @Shadow
    private static final Identifier FOOD_HALF_HUNGER_TEXTURE = Identifier.ofVanilla("hud/food_half_hunger");
    @Shadow
    private static final Identifier FOOD_FULL_HUNGER_TEXTURE = Identifier.ofVanilla("hud/food_full_hunger");
    @Shadow
    private static final Identifier FOOD_EMPTY_TEXTURE = Identifier.ofVanilla("hud/food_empty");
    @Shadow
    private static final Identifier FOOD_HALF_TEXTURE = Identifier.ofVanilla("hud/food_half");
    @Shadow
    private static final Identifier FOOD_FULL_TEXTURE = Identifier.ofVanilla("hud/food_full");
    @Shadow
    private int ticks;
    @Shadow
    private final Random random = Random.create();










    //渲染多少生命值心? 已经弃用,直接修改最大生命值上限即可
//    @ModifyArg(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(FF)F"),index = 0)
//    public float playerMaxHealth (float a){
//        return PlayerMaxHealthOrFoodLevelHelper.getMaxHealthOrFoodLevel();
//    }
//





    @Inject(method = "renderFood",at = @At(value = "HEAD"), cancellable = true)
    private void renderFood(DrawContext context, PlayerEntity player, int top, int right, CallbackInfo ci) {
        ci.cancel();
        HungerManager hungerManager = player.getHungerManager();

        int i = hungerManager.getFoodLevel();
        //获得额外的饱食度上限渲染
        //规则:从6点饱食度开始,每增加5级,就增加2点饱食度上限,每5级增加一次

        //仅仅是渲染,实际上限在hungerManager里面设置

        int maxFoodLevel=PlayerMaxHealthOrFoodLevelHelper.getMaxHealthOrFoodLevel();





//        LOGGER.info("maxFoodLevel is "+maxFoodLevel);
        RenderSystem.enableBlend();
        //遍历10个鸡腿
        for (int j = 0; j < maxFoodLevel/2; j++) {
            int k = top;
            Identifier identifier;
            Identifier identifier2;
            Identifier identifier3;
            if (player.hasStatusEffect(StatusEffects.HUNGER)) {
                identifier = FOOD_EMPTY_HUNGER_TEXTURE;
                identifier2 = FOOD_HALF_HUNGER_TEXTURE;
                identifier3 = FOOD_FULL_HUNGER_TEXTURE;
            } else {
                identifier = FOOD_EMPTY_TEXTURE;
                identifier2 = FOOD_HALF_TEXTURE;
                identifier3 = FOOD_FULL_TEXTURE;
            }

            if (player.getHungerManager().getSaturationLevel() <= 0.0F && this.ticks % (i * 3 + 1) == 0) {
                k = top + (this.random.nextInt(3) - 1);
            }

            int l = right - j * 8 - 9;
            context.drawGuiTexture(identifier, l, k, 9, 9);
            if (j * 2 + 1 < i) {
                context.drawGuiTexture(identifier3, l, k, 9, 9);
            }

            if (j * 2 + 1 == i) {
                context.drawGuiTexture(identifier2, l, k, 9, 9);
            }
        }

        RenderSystem.disableBlend();
    }











}
