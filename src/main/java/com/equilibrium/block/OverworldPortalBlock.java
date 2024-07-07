package com.equilibrium.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Portal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import org.jetbrains.annotations.Nullable;

public class OverworldPortalBlock extends Block implements Portal {
    public OverworldPortalBlock(Settings settings) {
        super(settings);
    }

    @Override
    public int getPortalDelay(ServerWorld world, Entity entity) {
        return entity instanceof PlayerEntity playerEntity
                ? Math.max(
                1,
                world.getGameRules()
                        .getInt(playerEntity.getAbilities().invulnerable ? GameRules.PLAYERS_NETHER_PORTAL_CREATIVE_DELAY : GameRules.PLAYERS_NETHER_PORTAL_DEFAULT_DELAY)
        )
                : 0;
    }


    @Override
    public TeleportTarget createTeleportTarget(ServerWorld world, Entity entity, BlockPos pos) {
        //获取你的现有维度
        RegistryKey<World> currentDimensionKey = world.getRegistryKey();

        //注册自定义维度
        RegistryKey<World> customDimensionKey = RegistryKey.of(RegistryKeys.WORLD, Identifier.of("miteequilibrium", "underworld"));
        //创建自定义维度世界实例
        ServerWorld serverWorld = world.getServer().getWorld(customDimensionKey);
        if (serverWorld!=null){
            System.out.println("weelll");
        }else{
            System.out.println("no!");
        }
        //当自定义维度存在时(否则会产生空指针错误):
        //世界边界限制器
        WorldBorder worldBorder = serverWorld.getWorldBorder();
        //1倍放大,参考下界是8倍
        BlockPos Pos = worldBorder.clamp(entity.getX() * 1, entity.getY(), entity.getZ() * 1);

        TeleportTarget.PostDimensionTransition postTransition = TeleportTarget.SEND_TRAVEL_THROUGH_PORTAL_PACKET;

        return TeleportTarget.missingSpawnBlock(serverWorld, entity, postTransition);
    }


    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity.canUsePortals(false)) {
            entity.tryUsePortal(this, pos);
        }
    }
    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(100) == 0) {
            world.playSound(
                    (double)pos.getX() + 0.5,
                    (double)pos.getY() + 0.5,
                    (double)pos.getZ() + 0.5,
                    SoundEvents.BLOCK_PORTAL_AMBIENT,
                    SoundCategory.BLOCKS,
                    0.5F,
                    random.nextFloat() * 0.4F + 0.8F,
                    false
            );
        }

        for (int i = 0; i < 4; i++) {
            double d = (double)pos.getX() + random.nextDouble();
            double e = (double)pos.getY() + random.nextDouble();
            double f = (double)pos.getZ() + random.nextDouble();
            double g = ((double)random.nextFloat() - 0.5) * 0.5;
            double h = ((double)random.nextFloat() - 0.5) * 0.5;
            double j = ((double)random.nextFloat() - 0.5) * 0.5;
            int k = random.nextInt(2) * 2 - 1;
            if (!world.getBlockState(pos.west()).isOf(this) && !world.getBlockState(pos.east()).isOf(this)) {
                d = (double)pos.getX() + 0.5 + 0.25 * (double)k;
                g = (double)(random.nextFloat() * 2.0F * (float)k);
            } else {
                f = (double)pos.getZ() + 0.5 + 0.25 * (double)k;
                j = (double)(random.nextFloat() * 2.0F * (float)k);
            }

            world.addParticle(ParticleTypes.PORTAL, d, e, f, g, h, j);
        }
    }


}
