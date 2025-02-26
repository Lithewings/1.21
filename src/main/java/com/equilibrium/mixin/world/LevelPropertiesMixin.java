package com.equilibrium.mixin.world;

import com.equilibrium.persistent_state.StateSaverAndLoader;
import com.equilibrium.util.ServerInfoRecorder;
import com.mojang.serialization.DynamicOps;
import net.minecraft.SharedConstants;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.resource.DataConfiguration;
import net.minecraft.util.Util;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.WorldGenSettings;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelProperties.class)
public abstract class LevelPropertiesMixin implements ServerWorldProperties, SaveProperties {
    @Shadow
    private LevelInfo levelInfo;
    @Inject(method = "updateProperties",at = @At("TAIL"))
    private void updateProperties(DynamicRegistryManager registryManager, NbtCompound levelNbt, NbtCompound playerNbt, CallbackInfo ci) {
//        StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(ServerInfoRecorder.getServerInstance());
//        if(serverState.keepHardcore)
//            levelNbt.putBoolean("hardcore",true);
    }

}
