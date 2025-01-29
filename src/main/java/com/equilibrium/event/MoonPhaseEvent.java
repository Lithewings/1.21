package com.equilibrium.event;

import com.equilibrium.MITEequilibrium;
import com.equilibrium.util.WorldMoonPhasesSelector;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.PlaySoundCommand;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

import net.minecraft.world.level.ServerWorldProperties;

public class MoonPhaseEvent {
    private static final EntityType<?>[] ANIMAL_TYPES = new EntityType[]{
            EntityType.PIG,
            EntityType.COW,
            EntityType.SHEEP,
            EntityType.CHICKEN
    };
    private static final EntityType<?>[] MOB_TYPES = new EntityType[]{
            EntityType.ZOMBIE,
            EntityType.CREEPER,
            EntityType.SPIDER,
            EntityType.SKELETON
    };
    private static String moonType;

    public static String getMoonType(World world) {
        moonType = WorldMoonPhasesSelector.setAndGetMoonType(world);
        if (moonType != null)
            return moonType;
        else {
            return "errorMoontype";
        }
    }

    //满月加强怪物
    public static void applyStrengthToHostileMobs(ServerWorld world) {
        for (PlayerEntity player : world.getPlayers())
            if (player != null) {
                // 玩家位置
                Vec3d playerPos = player.getPos();

                // 计算搜索区域：以玩家为中心的16个区块
                double range = 128; // ±128方块
                Box searchBox = new Box(playerPos.x - range, playerPos.y - range, playerPos.z - range,
                        playerPos.x + range, playerPos.y + range, playerPos.z + range);

                // 定义过滤条件：敌对生物
                TypeFilter<Entity, HostileEntity> hostileFilter = TypeFilter.instanceOf(HostileEntity.class);

                // 获取敌对生物列表
                List<HostileEntity> hostileMobs = world.getEntitiesByType(hostileFilter, searchBox, HostileEntity::isAlive);

                // 为每个敌对生物添加力量效果
                for (HostileEntity mob : hostileMobs) {
                    mob.getAttributeInstance(EntityAttributes.GENERIC_FOLLOW_RANGE).setBaseValue(256);
                    // 创建力量效果实例，持续时间单位为tick（20 ticks = 1秒），这里设为300 ticks, 即15秒
                    StatusEffectInstance strength = new StatusEffectInstance(StatusEffects.STRENGTH, 6000, 0); // 0 为效果等级，0 级即为 I 级
                    mob.addStatusEffect(strength);
                }
            }
    }
    //新月束缚怪物
    public static void applyWeaknessToHostileMobs(ServerWorld world) {
            for (PlayerEntity player : world.getPlayers())
                if (player != null) {
                    // 玩家位置
                    Vec3d playerPos = player.getPos();

                    // 计算搜索区域：以玩家为中心的16个区块
                    double range = 128; // ±128方块
                    Box searchBox = new Box(playerPos.x - range, playerPos.y - range, playerPos.z - range,
                            playerPos.x + range, playerPos.y + range, playerPos.z + range);

                    // 定义过滤条件：敌对生物
                    TypeFilter<Entity, HostileEntity> hostileFilter = TypeFilter.instanceOf(HostileEntity.class);

                    // 获取敌对生物列表
                    List<HostileEntity> hostileMobs = world.getEntitiesByType(hostileFilter, searchBox, HostileEntity::isAlive);

                    // 为每个敌对生物添加虚弱效果
                    for (HostileEntity mob : hostileMobs) {
                        // 创建虚弱效果实例，持续时间单位为tick（20 ticks = 1秒），这里设为300 ticks, 即15秒
                        StatusEffectInstance weakness = new StatusEffectInstance(StatusEffects.WEAKNESS, 6000, 0); // 0 为效果等级，0 级即为 I 级
                        StatusEffectInstance slowness = new StatusEffectInstance(StatusEffects.SLOWNESS, 6000, 1); // 0 为效果等级，0 级即为 I 级
                        mob.addStatusEffect(weakness);
                        mob.addStatusEffect(slowness);
                    }
                }
        }








    public static void spawnMobNearPlayer(ServerWorld world) {
        for (PlayerEntity player : world.getPlayers())
            if (player != null) {
                Random random = new Random();
                Vec3d playerPos = player.getPos();

                for (int i = 0; i < 10; i++) { // Try up to 10 times to find a valid position
                    double offsetX = (random.nextDouble() - 0.5) * 40.0; // Offset range extended to 40
                    double offsetZ = (random.nextDouble() - 0.5) * 40.0; // Offset range extended to 40
                    if (Math.abs(offsetX) < 16) {
                        offsetX += 16 * Math.signum(offsetX); // Ensure minimum offset of 16 blocks
                    }
                    if (Math.abs(offsetZ) < 16) {
                        offsetZ += 16 * Math.signum(offsetZ); // Ensure minimum offset of 16 blocks
                    }
                    BlockPos spawnPos = new BlockPos((int) (playerPos.x + offsetX), (int) playerPos.y, (int) (playerPos.z + offsetZ));

                    // Find the highest non-air block at the spawn position
                    while (world.isAir(spawnPos) && spawnPos.getY() > 0) {
                        spawnPos = spawnPos.down();
                    }

                    // Ensure the spawn position is not in lava and the block above is air
                    if (world.getBlockState(spawnPos).getBlock() != Blocks.LAVA && !world.isAir(spawnPos)) {
                        BlockPos spawnAbovePos = spawnPos.up();
                        BlockPos spawnAbovePos2 = spawnPos.up(2);

                        // Check if the position is valid for spawning a mob
                        if (world.isAir(spawnAbovePos) && world.isAir(spawnAbovePos2)) {
//                            MITEequilibrium.LOGGER.info("A Mob Respawned");
//                            player.sendMessage(Text.of("血月升起,怪物刷新一次"));
                            // ----------- 新增光照检查逻辑 -----------
                            // Minecraft中，世界光照范围是0~15，值越高表示越亮
                            // 如果光照值 >= 5，就跳过此位置
                            int lightLevel = world.getLightLevel(spawnAbovePos);
                            if (lightLevel >= 5) {
//                                player.sendMessage(Text.of("光线抑制"));
//                                player.sendMessage(Text.of("lightLevel"+lightLevel));
                                continue;  // 光线过亮，不生成怪物

                            }
                            // ------------------------------------
                            EntityType<?> mobType = MOB_TYPES[random.nextInt(MOB_TYPES.length)];
                            MobEntity mob = (MobEntity) mobType.create(world);
                            StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.STRENGTH, -1, 2, false, true, false);
                            if (mob != null) {
                                mob.refreshPositionAndAngles(spawnPos.getX() + 0.5, spawnPos.getY() + 1, spawnPos.getZ() + 0.5, random.nextFloat() * 360.0F, 0.0F);
                                //为怪物施加力量效果,除了苦力怕
                                if (!(mob instanceof CreeperEntity)) {
                                    mob.addStatusEffect(statusEffectInstance);
                                }
                                // Ensure SkeletonEntity spawns with a bow
                                if (mob instanceof SkeletonEntity) {
                                    SkeletonEntity skeleton = (SkeletonEntity) mob;
                                    skeleton.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
                                }

                                world.spawnEntity(mob);
                                break;
                            }
                        }
                    }
                }
            }

    }







    public static void spawnLighteningNearPlayer(ServerWorld world, PlayerEntity player) {
        Random random = new Random();
        Vec3d playerPos = player.getPos();

        double offsetX = (random.nextDouble() - 0.5) * 40.0; // Offset range extended to 40
        double offsetZ = (random.nextDouble() - 0.5) * 40.0; // Offset range extended to 40

        BlockPos spawnPos = new BlockPos((int) (playerPos.x + offsetX), (int) playerPos.y, (int) (playerPos.z + offsetZ));
        // Find the highest non-air block at the spawn position
        while (world.isAir(spawnPos) && spawnPos.getY() > 0) {
            spawnPos = spawnPos.down();
        }

        LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(world);
        if (lightning != null) {
            lightning.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(spawnPos));
            world.spawnEntity(lightning);
        }

    }



    public static void spawnAnimalNearPlayer(ServerWorld world) {
        PlayerEntity player = world.getRandomAlivePlayer();
        if (player != null) {
            Random random = new Random();
            Vec3d playerPos = player.getPos();

            for (int i = 0; i < 10; i++) { // Try up to 10 times to find a valid position
                double offsetX = (random.nextDouble() - 0.5) * 64.0; // Offset range extended to 64
                double offsetZ = (random.nextDouble() - 0.5) * 64.0; // Offset range extended to 64
                if (Math.abs(offsetX) < 32) {
                    offsetX += 32 * Math.signum(offsetX); // Ensure minimum offset of 32 blocks
                }
                if (Math.abs(offsetZ) < 32) {
                    offsetZ += 32 * Math.signum(offsetZ); // Ensure minimum offset of 32 blocks
                }
                BlockPos spawnPos = new BlockPos((int) (playerPos.x + offsetX), (int) playerPos.y, (int) (playerPos.z + offsetZ));

                // Find the highest non-air block at the spawn position
                while (world.isAir(spawnPos) && spawnPos.getY() > 0) {
                    spawnPos = spawnPos.down();
                }

                // Ensure the spawn position is not in lava
                if (world.getBlockState(spawnPos).getBlock() == Blocks.LAVA || world.getBlockState(spawnPos).getBlock() == Blocks.WATER) {
                    continue;
                }

                // Check if the position is valid for spawning an animal
                if (world.isAir(spawnPos.up()) && world.isAir(spawnPos.up(2))) {
                    EntityType<?> animalType = ANIMAL_TYPES[random.nextInt(ANIMAL_TYPES.length)];
                    MobEntity animal = (MobEntity) animalType.create(world);
                    if (animal != null) {
                        animal.refreshPositionAndAngles(spawnPos.getX() + 0.5, spawnPos.getY() + 1, spawnPos.getZ() + 0.5, random.nextFloat() * 360.0F, 0.0F);
                        world.spawnEntity(animal);
                        break;
                    }
                }
            }
        }
    }


    public static void RandomTickModifier(ServerWorld world, int randomTickSpeed) {
        PlayerEntity player = world.getRandomAlivePlayer();
        if (player != null)
            player.getWorld().getGameRules().get(GameRules.RANDOM_TICK_SPEED).set(randomTickSpeed, player.getServer());
    }


    public static void controlWeather(ServerWorld world) {
        PlayerEntity playerEntity = world.getRandomAlivePlayer();
        long timeOfDay = world.getTimeOfDay() % 24000; // 获取当前时间（一天有24000刻）

        if (timeOfDay >= 0 && timeOfDay < 14000) {
            startThunderstorm(world);
            if (playerEntity != null)
                if (playerEntity.getRandom().nextInt(4) == 0) {
//                    playerEntity.sendMessage(Text.of("雷声"));
                    playerEntity.getWorld().playSound(null, BlockPos.ofFloored(playerEntity.getPos()), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.WEATHER, 1.0F, 1.0F);
                } else {
                    spawnLighteningNearPlayer(world, playerEntity);
//                    playerEntity.sendMessage(Text.of("雷电"));
                }


        } else { // 其他时间段
            clearWeather(world);
        }
    }

    public static void startThunderstorm(ServerWorld world) {
        ServerWorldProperties worldProperties = (ServerWorldProperties) world.getLevelProperties();
        worldProperties.setThundering(true);
        worldProperties.setRaining(true);
        worldProperties.setClearWeatherTime(0);
        worldProperties.setRainTime(6000);
        worldProperties.setThunderTime(6000);


    }

    public static void clearWeather(ServerWorld world) {
        ServerWorldProperties worldProperties = (ServerWorldProperties) world.getLevelProperties();
        worldProperties.setThundering(false); // 关闭雷雨
        worldProperties.setRaining(false); // 关闭降雨
        worldProperties.setClearWeatherTime(12000); // 设置晴天时间长度（可以根据需要调整）
    }


}
