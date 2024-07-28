package com.equilibrium.mixin.oreGenerator;

import net.minecraft.registry.Registerable;
import net.minecraft.world.gen.feature.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlacedFeatures.class)
public abstract class PlacedFeaturesMixin {
    @Inject(method = "bootstrap",at = @At(value = "HEAD"),cancellable = true)
    private static void bootstrap(Registerable<PlacedFeature> featureRegisterable, CallbackInfo ci) {
        ci.cancel();
//        OceanPlacedFeatures.bootstrap(featureRegisterable);
//        UndergroundPlacedFeatures.bootstrap(featureRegisterable);
//        EndPlacedFeatures.bootstrap(featureRegisterable);
//        MiscPlacedFeatures.bootstrap(featureRegisterable);
//        NetherPlacedFeatures.bootstrap(featureRegisterable);
//        OrePlacedFeatures.bootstrap(featureRegisterable);
//        TreePlacedFeatures.bootstrap(featureRegisterable);
//        VegetationPlacedFeatures.bootstrap(featureRegisterable);
//        VillagePlacedFeatures.bootstrap(featureRegisterable);
    }
}
