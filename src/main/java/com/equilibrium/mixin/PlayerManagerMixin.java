package com.equilibrium.mixin;

import com.equilibrium.util.PlayerMaxHealthHelper;
import com.equilibrium.util.PlayerMaxHungerHelper;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Shadow @Final private static Logger LOGGER;

    @Inject(method = "onPlayerConnect",at = @At(value = "TAIL"))
    public void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci) {
        LOGGER.info("When finishing connect,the player xp level is "+player.experienceLevel);

        LOGGER.info("When finishing connect,the player health level is "+player.getHealth());

        int initializedMaxHealth = player.experienceLevel >=35 ? 20 : 6 +(int)(player.experienceLevel/5)*2;
        PlayerMaxHealthHelper.setMaxHealthLevel(initializedMaxHealth);

        int initializedFoodLevel = player.experienceLevel >=35 ? 20 : 6 +(int)(player.experienceLevel/5)*2;
        PlayerMaxHungerHelper.setMaxFoodLevel(initializedFoodLevel);




    }
}
