package com.equilibrium.mixin.internal_mod.hot_bar_should_not_loop;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInventory.class)
public abstract class HotBarShouldNotLoop {
    @Shadow
    public int selectedSlot;
    @Shadow
    @Final
    private static int HOTBAR_SIZE;

    @Shadow public abstract void scrollInHotbar(double scrollAmount);

    @Shadow @Final public PlayerEntity player;

    @Shadow public abstract void readNbt(NbtList nbtList);

    @Inject(method = "scrollInHotbar", at = @At("HEAD"), cancellable = true)
    public void scrollInHotbar(double scrollAmount, CallbackInfo ci) {
        if(this.selectedSlot==8||this.selectedSlot==0) {
            if(Math.abs(scrollAmount) <= 1)
                ci.cancel();
        }
        int newSlot = this.selectedSlot - (int) Math.signum(scrollAmount);
        if (newSlot < 0 || newSlot >= HOTBAR_SIZE) {
            ci.cancel();
        }
//        this.player.sendMessage(Text.of(""+scrollAmount));
    }
}
