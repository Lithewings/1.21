package com.equilibrium.mixin;
//实现注册自定义维度,后续生成自定义矿物

import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

import static net.minecraft.util.PathUtil.validatePath;

@Mixin(Identifier.class)
public abstract class IdentifierMixin implements Comparable<Identifier>{
    @Inject(method = "ofVanilla", at = @At("HEAD"))
    private static void ofVanilla(String path, CallbackInfoReturnable<Identifier> cir) {
        //向Identifier类注入了一个条件
    }}


