package com.equilibrium.mixin.render;

import com.equilibrium.util.OnServerInitializeMethod;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

@Mixin(CreateWorldScreen.WorldTab.class)
public abstract class CreativeWorldScreenWorldTabMixin  {
    //奖励箱关闭,何种情况均关闭
    @ModifyArg(method = "<init>",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/world/WorldScreenOptionGrid$OptionBuilder;toggleable(Ljava/util/function/BooleanSupplier;)Lnet/minecraft/client/gui/screen/world/WorldScreenOptionGrid$OptionBuilder;",ordinal = 1))
    public BooleanSupplier setValue2(BooleanSupplier getter) {
        return OnServerInitializeMethod::alwaysFalse;

    }









}