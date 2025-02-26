package com.equilibrium.mixin.render;

import com.equilibrium.util.BooleanStorageUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.world.WorldCreator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.file.Path;

@Mixin(WorldCreator.class
)

public class WorldCreatorMixin {

    @Inject(method = "areCheatsEnabled",at = @At("HEAD"), cancellable = true)
    public void areCheatsEnabled(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
