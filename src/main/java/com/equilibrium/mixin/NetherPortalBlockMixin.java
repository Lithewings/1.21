package com.equilibrium.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.block.Portal;
import net.minecraft.entity.Entity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NetherPortalBlock.class)
public abstract class NetherPortalBlockMixin extends Block implements Portal {


    @Shadow @Nullable protected abstract TeleportTarget getOrCreateExitPortalTarget(ServerWorld world, Entity entity, BlockPos pos, BlockPos scaledPos, boolean inNether, WorldBorder worldBorder);

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







