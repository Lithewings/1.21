package com.equilibrium.mixin.player;

import com.equilibrium.persistent_state.StateSaverAndLoader;
import com.equilibrium.util.ServerInfoRecorder;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.s2c.play.DeathMessageS2CPacket;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.scoreboard.ScoreAccess;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Unit;
import net.minecraft.util.math.*;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

import static net.minecraft.world.World.OVERWORLD;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {

    @Shadow public abstract @Nullable BlockPos getSpawnPointPosition();

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);

    }

    @Shadow private int joinInvulnerabilityTicks = 0;


    //    @Override
//    public void jump(){
//        super.jump();
//        this.sendMessage(Text.of("SpawnPoint is : "+this.getSpawnPointPosition()));
//        this.teleport(this.getServerWorld(),this.getSpawnPointPosition().getX(),this.getSpawnPointPosition().getY(),this.getSpawnPointPosition().getZ(),0,0);
//    }


    @Inject(method = "onDeath",at = @At("HEAD"))
    public void onDeath(DamageSource damageSource, CallbackInfo ci) {
        StateSaverAndLoader serverState;
        //创建一个持久状态类,传入当前服务器副本,再对这个持久状态类写入数据,当玩家退出时,自动把信息写回磁盘
        serverState = StateSaverAndLoader.getServerState(ServerInfoRecorder.getServerInstance());
        serverState.playerDeathTimes++;
    }














}
