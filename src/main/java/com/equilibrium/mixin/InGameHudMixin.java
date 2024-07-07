package com.equilibrium.mixin;

import com.equilibrium.util.PlayerMaxHealthHelper;
import com.equilibrium.util.PlayerMaxHungerHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.equilibrium.MITEequilibrium.LOGGER;

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












    @Redirect(method = "renderStatusBars",at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D"))
    public double playerMaxHealth (PlayerEntity playerEntity, RegistryEntry registryEntry){

        return PlayerMaxHealthHelper.getMaxHealthLevel();
    }






    @Inject(method = "renderFood",at = @At(value = "HEAD"), cancellable = true)
    private void renderFood(DrawContext context, PlayerEntity player, int top, int right, CallbackInfo ci) {
        ci.cancel();
        //根据等级经验动态渲染饱食度,原则上显示的饱食度上限就应该等于根据在HungerManagerMixin中设定的上限

        HungerManager hungerManager = player.getHungerManager();


        int i = hungerManager.getFoodLevel();
        //获得额外的饱食度上限渲染
        //规则:从6点饱食度开始,每增加5级,就增加2点饱食度上限,每5级增加一次


        int maxFoodLevel=PlayerMaxHungerHelper.getMaxFoodLevel();





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
