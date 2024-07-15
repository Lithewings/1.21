package com.equilibrium.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.block.Portal;
import net.minecraft.enchantment.effect.AllOfEnchantmentEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(NetherPortalBlock.class)
public abstract class NetherPortalBlockMixin extends Block implements Portal {


    @Shadow @Nullable protected abstract TeleportTarget getOrCreateExitPortalTarget(ServerWorld world, Entity entity, BlockPos pos, BlockPos scaledPos, boolean inNether, WorldBorder worldBorder);

    @Shadow @Final private static Logger LOGGER;

    public NetherPortalBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "createTeleportTarget",at=@At(value = "HEAD"),cancellable = true)
    public void createTeleportTarget(ServerWorld world, Entity entity, BlockPos pos, CallbackInfoReturnable<TeleportTarget> cir) {
        cir.cancel();
        RegistryKey<World> overworld = World.OVERWORLD;
        RegistryKey<World> nether = World.NETHER;
        RegistryKey<World> underworld = RegistryKey.of(RegistryKeys.WORLD, Identifier.of("miteequilibrium", "underworld"));
        //获取目前的世界类型(访问注册方法)
        RegistryKey<World> registryKey = world.getRegistryKey();
        //传送后的世界类型
        RegistryKey<World> teleport;

        if (registryKey == null) {
            cir.setReturnValue(null);
        } else {
            ServerWorld serverWorld;
            //缩放条件
            boolean inNether = world.getRegistryKey() == World.NETHER;
            //世界边界限制
            WorldBorder worldBorder = world.getWorldBorder();
            //缩放倍率
            double d = DimensionType.getCoordinateScaleFactor(world.getDimension(), world.getDimension());
            BlockPos blockPos = worldBorder.clamp(entity.getX() * d, entity.getY(), entity.getZ() * d);

            boolean atBottom = Math.abs(blockPos.getY()-world.getBottomY())<5;



            //world.getRegistryKey()获取现在的世界
            //主世界传地下世界,地下世界也可以传主世界






            if(world.getRegistryKey()==overworld && atBottom){
                teleport=underworld;
            } else if (world.getRegistryKey()==overworld && !atBottom) {
                teleport=overworld;
                if(entity.isPlayer()){
                    serverWorld=world.getServer().getWorld(teleport);

                    StatusEffectInstance statusEffectInstance1 = new StatusEffectInstance(StatusEffects.BLINDNESS, 60,255, false,false,false);
                    StatusEffectUtil.addEffectToPlayersWithinDistance(world, entity, entity.getPos(), 4, statusEffectInstance1,100);

                    StatusEffectInstance statusEffectInstance4 = new StatusEffectInstance(StatusEffects.SLOWNESS,40,255, false,false,false);
                    StatusEffectUtil.addEffectToPlayersWithinDistance(world, entity, entity.getPos(), 4, statusEffectInstance4,100);

                    StatusEffectInstance statusEffectInstance3 = new StatusEffectInstance(StatusEffects.NAUSEA,100,255, false,false,false);
                    StatusEffectUtil.addEffectToPlayersWithinDistance(world, entity, entity.getPos(), 4, statusEffectInstance3,100);



                    BlockPos spawnPos=world.getSpawnPos();


                    for(int i=0;i<=3;i++){
                        world.breakBlock(spawnPos.add(1,i,1),true);
                        world.breakBlock(spawnPos.add(1,i,0),true);
                        world.breakBlock(spawnPos.add(1,i,-1),true);
                        world.breakBlock(spawnPos.add(0,i,1),true);
                        world.breakBlock(spawnPos.add(0,i,0),true);
                        world.breakBlock(spawnPos.add(0,i,-1),true);
                        world.breakBlock(spawnPos.add(-1,i,1),true);
                        world.breakBlock(spawnPos.add(-1,i,0),true);
                        world.breakBlock(spawnPos.add(-1,i,-1),true);
                    }



                    entity.teleport(serverWorld,spawnPos.getX(),spawnPos.getY(),spawnPos.getZ(), Set.of(),entity.getYaw(), entity.getPitch());

                    return;
                }

            } else if (world.getRegistryKey()==underworld && !atBottom) {
                teleport=overworld;
            } else if (world.getRegistryKey()==underworld &&  atBottom) {
                teleport=nether;
            } else if(world.getRegistryKey()==nether){
                teleport=underworld;
            }
            else {
                teleport=world.getRegistryKey();
            }


            serverWorld=world.getServer().getWorld(teleport);
            cir.setReturnValue(this.getOrCreateExitPortalTarget(serverWorld, entity, pos, blockPos, inNether, worldBorder));

        }}











    }







