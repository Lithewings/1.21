package com.equilibrium.mixin.player;

import com.equilibrium.util.PlayerMaxHealthHelper;
import com.equilibrium.util.PlayerMaxHungerHelper;
import com.equilibrium.util.ServerInfoRecorder;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;


@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Shadow @Final private static Logger LOGGER;

    @Shadow @Final private MinecraftServer server;


    @Shadow @Final private List<ServerPlayerEntity> players;



    @Unique
    private static boolean gotcha;


    @Inject(method = "onPlayerConnect",at = @At(value = "TAIL"))
    public void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci) {
        LOGGER.info("When finishing connect,the player xp level is "+player.experienceLevel);
        LOGGER.info("When finishing connect,the player health level is "+player.getHealth());
        if(gotcha && !player.getWorld().isClient && player.getWorld().getTimeOfDay()<300 ) {
            gotcha =true;
            World world = player.getWorld();
            BlockPos spawnPos = player.getWorld().getSpawnPos();
            StatusEffectInstance boost = new StatusEffectInstance(StatusEffects.SATURATION, 24000, 0, false, false, true);
            // 在出生点放置箱子
            world.setBlockState(spawnPos, Blocks.CHEST.getDefaultState());
            // 在箱子内放入一个望远镜
            if (world.getBlockEntity(spawnPos) instanceof ChestBlockEntity chest) {
                chest.setStack(0, new ItemStack(Items.SPYGLASS, 1));
            }
            // 在箱子旁边放置火把
            world.setBlockState(spawnPos.add(1, 0, 0), Blocks.TORCH.getDefaultState());
            world.setBlockState(spawnPos.add(0, 0, 1), Blocks.TORCH.getDefaultState());
            world.setBlockState(spawnPos.add(-1, 0, 0), Blocks.TORCH.getDefaultState());
            world.setBlockState(spawnPos.add(0, 0, -1), Blocks.TORCH.getDefaultState());

            StatusEffectUtil.addEffectToPlayersWithinDistance((ServerWorld) player.getWorld(), player, player.getPos(), 4, boost,24000);




        }




        //发送时间
        if(!player.getWorld().isClient) {
            long time = player.getWorld().getTimeOfDay();
            ServerInfoRecorder.setDay((int) time);
        }
        //记录服务器实例
        if(!ServerInfoRecorder.isServerInstanceSet())
            ServerInfoRecorder.setServerInstance(server);


        if(!player.getWorld().isClient){
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
}
