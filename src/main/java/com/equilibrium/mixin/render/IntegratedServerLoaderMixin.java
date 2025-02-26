package com.equilibrium.mixin.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.server.integrated.IntegratedServerLoader;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IntegratedServerLoader.class)
public abstract class IntegratedServerLoaderMixin {
    @Inject(at = @At("HEAD"), method = "showBackupPromptScreen", cancellable = true)
    public void showBackupPrompt(LevelStorage.Session session, boolean customized, Runnable callback, Runnable onCancel, CallbackInfo ci) {
        if (customized) return;
        ci.cancel();
        MinecraftClient.getInstance().execute(callback);
    }
}