package com.equilibrium.mixin.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tab.Tab;
import net.minecraft.client.gui.tab.TabManager;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(TabManager.class)
public class TabManagerMixin {
//    @Inject(method = "setCurrentTab", at = @At("HEAD"), cancellable = true)
//    public void setCurrentTab(Tab tab, boolean clickSound, CallbackInfo ci) {
//        if (tab.getTitle().getContent().toString().contains("creative")) {
//
//            if (clickSound) {
//                MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
//            }
//            ci.cancel();
//
//        }
//
//
//    }
}
