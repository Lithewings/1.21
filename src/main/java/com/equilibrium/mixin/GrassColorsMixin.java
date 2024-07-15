package com.equilibrium.mixin;

import com.equilibrium.util.ColorAdjuster;
import net.minecraft.world.biome.GrassColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GrassColors.class)
public abstract class GrassColorsMixin {
    @Shadow
    private static int[] colorMap;


    @Inject(method = "getColor",at = @At(value = "HEAD"), cancellable = true)
    private static void getColor(double temperature, double humidity, CallbackInfoReturnable<Integer> cir) {
        cir.cancel();
        humidity *= temperature;
        int i = (int)((1.0 - temperature) * 255.0);
        int j = (int)((1.0 - humidity) * 255.0);
        int k = j << 8 | i;
        int baseColor = colorMap[k] ;
        int modifiedColor = ColorAdjuster.adjustColor(baseColor,0,0,0);

        cir.setReturnValue(k >= colorMap.length ? -65281 : modifiedColor);

    }





}
