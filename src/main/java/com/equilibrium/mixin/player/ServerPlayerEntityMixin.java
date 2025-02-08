package com.equilibrium.mixin.player;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.world.World.OVERWORLD;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {

    @Shadow public abstract @Nullable BlockPos getSpawnPointPosition();

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);

    }
    @Shadow private int joinInvulnerabilityTicks = 0;

    @Shadow public abstract void sendMessage(Text message, boolean overlay);

    @Shadow public abstract void teleport(ServerWorld targetWorld, double x, double y, double z, float yaw, float pitch);

    @Shadow public abstract float getSpawnAngle();

    @Shadow public abstract ServerWorld getServerWorld();

//    @Override
//    public void jump(){
//        super.jump();
//        this.sendMessage(Text.of("SpawnPoint is : "+this.getSpawnPointPosition()));
//        this.teleport(this.getServerWorld(),this.getSpawnPointPosition().getX(),this.getSpawnPointPosition().getY(),this.getSpawnPointPosition().getZ(),0,0);
//    }


}
