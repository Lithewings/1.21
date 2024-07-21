package com.equilibrium.mixin.portal;

import com.equilibrium.MITEequilibrium;
import com.equilibrium.constant.ConstantString;
import com.equilibrium.util.ShouldSentText;
import com.mojang.brigadier.Message;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.block.Portal;
import net.minecraft.block.entity.VaultBlockEntity;
import net.minecraft.command.TranslatableBuiltInExceptions;
import net.minecraft.enchantment.effect.AllOfEnchantmentEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.message.SentMessage;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.Set;

import static com.equilibrium.constant.ConstantString.*;


@Mixin(NetherPortalBlock.class)
public abstract class NetherPortalBlockMixin extends Block implements Portal{


    @Shadow @Nullable protected abstract TeleportTarget getOrCreateExitPortalTarget(ServerWorld world, Entity entity, BlockPos pos, BlockPos scaledPos, boolean inNether, WorldBorder worldBorder);

    @Shadow @Final private static Logger LOGGER;

    public NetherPortalBlockMixin(Settings settings) {
        super(settings);
    }





    private static String getTeleportWorld(World world, Entity entity) {

        //获取目前的世界类型(访问注册方法)
        RegistryKey<World> registryKey = world.getRegistryKey();
        //传送后的世界类型
        RegistryKey<World> teleport;
        //避免空指针错误

        if (registryKey == null) {
            return "Not an illegal transportation";
        } else {
            boolean atBottom = Math.abs(entity.getY()-world.getBottomY())<5;
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
            String worldType;
            if(teleport==underworld){
                worldType= TRANSPORT_TARGET2;
            } else if (teleport==overworld) {
                worldType= TRANSPORT_TARGET1;
            } else if (teleport==nether) {
                worldType= TRANSPORT_TARGET3;
            }else{
                worldType="Not an illegal transportation";
            }

            return worldType;
        }


    }




    @Inject(method = "onEntityCollision",at = @At(value = "HEAD"),cancellable = true)
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        ci.cancel();

        if (entity.canUsePortals(false)) {
            entity.tryUsePortal(this, pos);
                if(ShouldSentText.count>=170) {
                    String teleport = getTeleportWorld(world, entity);
                    if (Objects.equals(teleport, TRANSPORT_TARGET1)){
                        entity.sendMessage(Text.of(Text.translatable("teleport.overworld").formatted(Formatting.YELLOW)));
                    }else if(Objects.equals(teleport, TRANSPORT_TARGET2)){
                        entity.sendMessage(Text.of(Text.translatable("teleport.underworld").formatted(Formatting.YELLOW)));
                    }else if(Objects.equals(teleport, TRANSPORT_TARGET3))
                        entity.sendMessage(Text.of(Text.translatable("teleport.nether").formatted(Formatting.YELLOW)));
                    else{
                        entity.sendMessage(Text.of("You shouldn't receive the mesaage, it might you will teleport to the wrong dimension that no supporting"));
                    }
                    ShouldSentText.count=0;
                }

        }
    }




    private static RegistryKey<World> overworld = World.OVERWORLD;
    private static RegistryKey<World> nether = World.NETHER;
    private static RegistryKey<World> underworld = RegistryKey.of(RegistryKeys.WORLD, Identifier.of("miteequilibrium", "underworld"));



    @Inject(method = "createTeleportTarget",at=@At(value = "HEAD"),cancellable = true)
    public void createTeleportTarget(ServerWorld world, Entity entity, BlockPos pos, CallbackInfoReturnable<TeleportTarget> cir) {
        cir.cancel();

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

            boolean atBottom = Math.abs(entity.getY()-world.getBottomY())<5;



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
                else{
                    teleport=underworld;
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

            //传送信息冷却
            ShouldSentText.count=-100;
            cir.setReturnValue(this.getOrCreateExitPortalTarget(serverWorld, entity, pos, blockPos, inNether, worldBorder));

        }}











    }







