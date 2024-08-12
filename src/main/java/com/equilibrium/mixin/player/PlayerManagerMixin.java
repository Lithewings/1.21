package com.equilibrium.mixin.player;

import com.equilibrium.constant.MaxCount;
import com.equilibrium.util.PlayerMaxHealthHelper;
import com.equilibrium.util.PlayerMaxHungerHelper;
import com.equilibrium.util.WorldMoonPhasesSelector;
import com.equilibrium.util.WorldTimeHelper;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.UUID;


@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Shadow @Final private static Logger LOGGER;

    @Shadow @Final private MinecraftServer server;


    @Shadow @Final private List<ServerPlayerEntity> players;

    @Inject(method = "onPlayerConnect",at = @At(value = "TAIL"))
    public void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci) {
        LOGGER.info("When finishing connect,the player xp level is "+player.experienceLevel);

        LOGGER.info("When finishing connect,the player health level is "+player.getHealth());
        //发送时间
        WorldTimeHelper.setDay(this.players.getFirst().getWorld());




        int initializedMaxHealth = player.experienceLevel >=35 ? 20 : 6 +(int)(player.experienceLevel/5)*2;
        PlayerMaxHealthHelper.setMaxHealthLevel(initializedMaxHealth);

        int initializedFoodLevel = player.experienceLevel >=35 ? 20 : 6 +(int)(player.experienceLevel/5)*2;
        PlayerMaxHungerHelper.setMaxFoodLevel(initializedFoodLevel);

        StatusEffectInstance statusEffectInstance1 = new StatusEffectInstance(StatusEffects.BLINDNESS, 60,255, false,false,false);
        StatusEffectUtil.addEffectToPlayersWithinDistance((ServerWorld) player.getWorld(), player, player.getPos(), 4, statusEffectInstance1,100);
        StatusEffectInstance statusEffectInstance2 = new StatusEffectInstance(StatusEffects.NAUSEA,100,255, false,false,false);
        StatusEffectUtil.addEffectToPlayersWithinDistance((ServerWorld) player.getWorld(), player, player.getPos(), 4, statusEffectInstance2,100);
        StatusEffectInstance statusEffectInstance3 = new StatusEffectInstance(StatusEffects.WEAKNESS,100,255, false,false,false);
        StatusEffectUtil.addEffectToPlayersWithinDistance((ServerWorld) player.getWorld(), player, player.getPos(), 4, statusEffectInstance3,100);

        if(player.getHealth() <= 1){
            player.damage(player.getDamageSources().badRespawnPoint(player.getPos()),114514);
        }else {
            player.damage(player.getDamageSources().magic(),1f);
        }

    }
}
