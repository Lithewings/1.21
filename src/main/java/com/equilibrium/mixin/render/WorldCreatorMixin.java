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
    @Unique
    private final String fileName = "Finish The Game Once.dat";
    @Unique
    private final Path configPath = FabricLoader.getInstance().getConfigDir().normalize().resolve(fileName);

//    @Inject(method = "areCheatsEnabled",at = @At("HEAD"), cancellable = true)
//    public void areCheatsEnabled(CallbackInfoReturnable<Boolean> cir) {
//        if(!BooleanStorageUtil.load(configPath.toString(), false)) {
//            cir.setReturnValue(false);
//        }
//
//    }
}
