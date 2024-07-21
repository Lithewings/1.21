package com.equilibrium.mixin.tables;

import com.equilibrium.MITEequilibrium;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

//    @Inject(at = @At("RETURN"), method = "onPlayerConnect")
//    public void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci){
//        player.sendMessage(Text.of(MITEequilibrium.MOD_ID).copy().append(Text.translatable("crafttime.join_info").formatted(Formatting.YELLOW)));
//    }
}