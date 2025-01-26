package com.equilibrium.mixin.player;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)








public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }



    @Inject(method = "canSprint",at = @At("HEAD"),cancellable = true)
    private void canSprint(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.hasVehicle() || (float)this.getHungerManager().getFoodLevel() > 0.0F || this.getAbilities().allowFlying);

    }


}
