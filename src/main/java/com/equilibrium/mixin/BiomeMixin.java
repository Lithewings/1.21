package com.equilibrium.mixin;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GrassColors;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Biome.class)
public abstract class BiomeMixin {
    @Shadow
    @Final
    private BiomeEffects effects;




    @Inject(method = "getSkyColor",at =@At(value = "HEAD"),cancellable = true)
    public void getSkyColor(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(this.effects.getSkyColor());
    }








}
