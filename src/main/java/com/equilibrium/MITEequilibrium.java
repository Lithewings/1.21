package com.equilibrium;

import com.equilibrium.block.ModBlocks;

import com.equilibrium.craft_time_register.BlockInit;
import com.equilibrium.craft_time_register.UseBlock;
import com.equilibrium.entity.goal.BreakBlockGoal;
import com.equilibrium.event.BreakBlockEvent;
import com.equilibrium.event.CraftingMetalPickAxeCallback;
import com.equilibrium.item.*;
import com.equilibrium.persistent_state.StateSaverAndLoader;
import com.equilibrium.util.OnServerInitializeMethod;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.GameVersion;
import net.minecraft.SaveVersion;
import net.minecraft.SharedConstants;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.equilibrium.craft_time_register.BlockEnityRegistry;

import com.equilibrium.util.CreativeGroup;
import com.equilibrium.craft_time_worklevel.CraftingIngredients;
import com.equilibrium.craft_time_worklevel.FurnaceIngredients;


import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.equilibrium.block.enchanting_table.ModBlockEntityTypes.modBlockEntityTypesInit;

import static com.equilibrium.block.enchanting_table.ModScreenTypes.registerScreenHandlers;
import static com.equilibrium.enchantments.EnchantmentsCodec.registerAllOfEnchantments;
import static com.equilibrium.entity.ModEntities.registerModEntities;


import static com.equilibrium.event.MoonPhaseEvent.*;
import static com.equilibrium.event.MoonPhaseEvent.RandomTickModifier;
import static com.equilibrium.event.sound.SoundEventRegistry.registrySoundEvents;
import static com.equilibrium.item.Armors.registerArmors;
import static com.equilibrium.item.Metal.registerModItemRaw;
import static com.equilibrium.item.extend_item.CoinItems.registerCoinItems;
import static com.equilibrium.item.food.FoodItems.registerFoodItems;
import static com.equilibrium.item.food.ItemComponentModifier.foodComponentModify;
import static com.equilibrium.item.food.WaterBowl.vanillaBowlItemUse;
import static com.equilibrium.status.registerStatusEffect.registerStatusEffects;
import static com.equilibrium.tags.ModBlockTags.registerModBlockTags;
import static com.equilibrium.tags.ModItemTags.registerModItemTags;


import static com.equilibrium.ore_generator.ModPlacementGenerator.registerModOre;
import static com.equilibrium.util.OnServerInitializeMethod.onUseCrystalItem;


public class MITEequilibrium implements ModInitializer {

    public static final String MOD_ID = "miteequilibrium";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

//	public static Config config;


    public static final BooleanProperty FERTILIZED = BooleanProperty.of("fertilized");


    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void init() {
        // 任务在mod加载时初始化,初始化僵尸破坏的方块进度
        scheduler.scheduleAtFixedRate(() -> {
            synchronized (BreakBlockGoal.blockBreakProgressMap) {
                BreakBlockGoal.blockBreakProgressMap.clear();
                System.out.println("Progress map cleared.");
            }
        }, 240, 240, TimeUnit.SECONDS);  // 30秒后首次运行，以后每隔30秒执行一次
    }


    public static void talkToAllServerPlayer(MinecraftServer server, String context) {
        for (ServerPlayerEntity serverPlayer : server.getPlayerManager().getPlayerList()) {
            serverPlayer.sendMessage(Text.of(context));
        }
    }


    // 注册命令的标准方式，适配 CommandDispatcher 的签名
    private void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(
                CommandManager.literal("village")
                        .executes
                                (OnServerInitializeMethod::isPickAxeCrafted)

        );
//		dispatcher.register(CommandManager.literal("hardcore")
//				.executes(context -> {
//					ServerCommandSource source = context.getSource();
//					PlayerEntity player = source.getPlayer();
//
//
//
//
//					// 构建可点击文本
//					Text clickableText = Text.literal("[切换到聊天栏,点击文字切换至极限模式(该操作将无法撤销)]")
//							.styled(style -> style
//									.withColor(Formatting.GREEN)
//									.withClickEvent(new ClickEvent(
//											ClickEvent.Action.RUN_COMMAND,
//											"/triggeraction" // 点击后触发的命令
//									))
//									.withHoverEvent(new HoverEvent(
//											HoverEvent.Action.SHOW_TEXT,
//											Text.of("该操作将无法撤销,是否继续?")
//									))
//							);
//					// 发送消息给玩家
//					player.sendMessage(clickableText, false);
//					return 1;
//				}));
//
//
//		dispatcher.register(CommandManager.literal("triggeraction")
//				.executes(context -> {
//					MinecraftServer server = context.getSource().getServer();
//					ServerCommandSource source = context.getSource();
//					PlayerEntity player = source.getPlayer();
//
//					if(context.getSource().getWorld().getTimeOfDay()>24000L){
//						player.sendMessage(Text.of("目前状态无法再切换至极限模式（生存时间已大于1天）"));
//						return 1;
//					}
//
//
//
//					// 执行自定义逻辑（例如发送提示）
//					talkToAllServerPlayer(server,"世界已切换至极限模式,重新进入世界以生效设置");
//
//					// 这里可以添加更多操作，如给予物品、修改游戏状态等
//
//					//放到记录类中,在别的位置可能就会用到
//					StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(ServerInfoRecorder.getServerInstance());
//					serverState.keepHardcore=true;
//
//
//
//
//					return 1;
//				}));


//		dispatcher.register(
//				CommandManager.literal("difficultyLevel")
//						.then(CommandManager.literal("set")
//								.then(CommandManager.argument("level", IntegerArgumentType.integer())
//										.executes(commandContext -> {
//											MinecraftServer server = commandContext.getSource().getServer();
//											if(server.getWorld(World.OVERWORLD).getTimeOfDay()>24000L) {
//												talkToAllServerPlayer(server, "目前无法再使用该命令调整游戏难度等级（生存时间已大于1天）");
//												return 1;
//											}
//
//
//
//											int level = IntegerArgumentType.getInteger(commandContext, "level");
//
//
//											StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(server);
//
//
//											switch(level){
//												case 0:{
//													server.setDifficulty(Difficulty.EASY,true);
//													talkToAllServerPlayer(server,"游戏难度将始终保持在简单难度");
//													serverState.difficultyLevel = level;
//													break;
//												}
//												case 1: {
//													server.setDifficulty(Difficulty.NORMAL, true);
//													talkToAllServerPlayer(server, "游戏难度将始终保持在普通难度");
//													serverState.difficultyLevel = level;
//													break;
//												}
//												case 2: {
//													server.setDifficulty(Difficulty.HARD, true);
//													talkToAllServerPlayer(server, "游戏难度将始终保持在困难难度");
//													serverState.difficultyLevel = level;
//													break;
//												}
//												default:
//													server.setDifficulty(Difficulty.NORMAL,true);
//													talkToAllServerPlayer(server,"未知的游戏难度等级,请重新选择难度");
//													break;
//											}
//
//
//											return 1;
//
//
//
//
//
//
//										}
//
//
//
//
//										)
//
//
//
//		)));
        // 监听右键点击事件，检查是否持有指南针
//		ServerTickEvents.END_SERVER_TICK.register(server -> {
//			for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
//				if (player.getMainHandStack().getItem() == Items.COMPASS) {
//					// 当玩家右键点击指南针时，触发 locate 指令
//					player.sendMessage(Text.literal("Right-clicked with compass! Locating fortress..."), false);
//					try {
//						executeLocateStructure(player.getCommandSource());
//						player.sendMessage(Text.of("你右键了指南针!"));
//					} catch (CommandSyntaxException e) {
//						throw new RuntimeException(e);
//					}
//				}
//			}
//		});
    }


//        // 给玩家发送调试信息（可选）
//        player.sendMessage(Text.literal(
//                String.format(
//                        "当前耐久: %.0f%% -> 护甲系数(S曲线): %.1f%%",
//                        100 * linearRatio,
//                        100 * sCurveRatio
//                )
//        ), true);


//
//	 执行 locate structure 指令
//	private static int executeLocateStructure(ServerCommandSource source) throws CommandSyntaxException {
//        return 0;
//    }


    private static final int TICK_INTERVAL = 500; // 每隔500 tick检查一次
    private int tickCount = 0; // 记录当前 tick

    // 判断世界是否进入新的一天
    private boolean hasNewDayPassed(long worldTime) {
        return worldTime % 24000L < 1000L; // 假设新的一天发生在世界时间的0到1000tick之间
    }

    // 新的一天到来时触发的事件
    private void onNewDay(MinecraftServer server, ServerWorld world, long worldTime) {
    }


    public void onInitialize() {

        SharedConstants.gameVersion = new GameVersion() {
            @Override
            public SaveVersion getSaveVersion() {
                return new SaveVersion(106);
            }

            @Override
            public String getId() {
                return "MITE:Equilibrium Beta v1.0.6";
            }

            @Override
            public String getName() {
                return "MITE:Equilibrium Beta v1.0.6";
            }

            @Override
            public int getProtocolVersion() {
                return 106;
            }

            @Override
            public int getResourceVersion(ResourceType type) {
                return 106;
            }

            @Override
            public Date getBuildTime() {
                return new Date();
            }

            @Override
            public boolean isStable() {
                return true;
            }
        };


        ServerLifecycleEvents.SERVER_STARTED.register(server -> {

            //锁定游戏难度
            server.setDifficultyLocked(true);












//			StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(server);
//			int level = serverState.difficultyLevel;
//			//极限模式下不生效
//			switch (level){
//				case 0:
//					server.setDifficulty(Difficulty.EASY,true);
//					break;
//				case 1:
//					server.setDifficulty(Difficulty.NORMAL,true);
//					break;
//				case 2:
//					server.setDifficulty(Difficulty.HARD,true);
//					break;
//				default:
//					server.setDifficulty(Difficulty.NORMAL,true);
//					break;
//			}


        });


        // 注册服务器 tick 事件
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            // 每隔 TICK_INTERVAL 次 tick 触发一次检查
            tickCount++;
            //获取时间,得到月相,决定是否触发月相事件

            //月相事件
            String moonType = getMoonType(server.getWorld(World.OVERWORLD));
            ServerWorld serverOverWorld = server.getWorld(World.OVERWORLD);
            boolean isNoPlayersInTheOverWorld = serverOverWorld.getPlayers().isEmpty();
            Random random = new Random();


            if (isNoPlayersInTheOverWorld) {
                if (serverOverWorld.getGameRules().getInt(GameRules.RANDOM_TICK_SPEED) != 3) {
                    for (PlayerEntity player : server.getPlayerManager().getPlayerList())
                        player.sendMessage(Text.of("由于主世界没有玩家,随机刻速度已回调至默认值"), true);
                    //有可能目前是蓝月,但玩家在地底世界,所以会陷入这里恢复默认,但蓝月那边又改成5,这样反复执行了这段代码
                    RandomTickModifier(serverOverWorld, 3);
                }

            }

            if (Objects.equals(moonType, "errorMoontype"))
                for (PlayerEntity player : server.getPlayerManager().getPlayerList())
                    player.sendMessage(Text.of("月相加载失败"), true);
            else {
                //月相事件,只在主世界进行
                //增大随机刻的条件
                boolean shouldRandomTickIncrease = (moonType.equals("blueMoon") || (moonType.equals("harvestMoon")) || (moonType.equals("haloMoon")));
                if (!shouldRandomTickIncrease) {
                    if (serverOverWorld.getGameRules().getInt(GameRules.RANDOM_TICK_SPEED) != 3) {
                        for (PlayerEntity player : server.getPlayerManager().getPlayerList())
                            player.sendMessage(Text.of("由于处在普通月相,随机刻已回调至默认值"), true);
                        RandomTickModifier(serverOverWorld, 3);
                    }
                }


                if (moonType.equals("bloodMoon")) {
                    if (serverOverWorld.getTimeOfDay() % 100 == 0) {
                        //执行间隔事件
                        spawnMobNearPlayer(serverOverWorld);
                    }
                    if (serverOverWorld.getTimeOfDay() % random.nextInt(50, 64) == 0) {
                        //执行间隔事件
                        controlWeather(serverOverWorld);
//                        this.sendMessage(Text.of("雷电事件"));
                    }
                }


                if (moonType.equals("harvestMoon") || (moonType.equals("haloMoon"))) {
                    if (serverOverWorld.getGameRules().getInt(GameRules.RANDOM_TICK_SPEED) != 4)
                        RandomTickModifier(serverOverWorld, 4);
//               if (this.age % 100 == 0) {
                    //执行间隔事件
//               this.sendMessage(Text.of("黄月/幻月升起,触发事件"));
//               }
                }

                if (moonType.equals("fullMoon")) {
                    if (serverOverWorld.getTimeOfDay() % 100 == 0) {
//              this.sendMessage(Text.of("满月升起,触发事件"));
                        applyStrengthToHostileMobs(serverOverWorld);
                    }
                }

                if (moonType.equals("newMoon")) {
                    if (serverOverWorld.getTimeOfDay() % 100 == 0) {
                        applyWeaknessToHostileMobs(serverOverWorld);
//              this.sendMessage(Text.of("新月升起,触发事件"));
                    }
                }

                //第一次蓝月,不改变随机刻速度
                if (moonType.equals("blueMoon")) {
                    if (serverOverWorld.getTimeOfDay() > 24000) {

                        if (serverOverWorld.getGameRules().getInt(GameRules.RANDOM_TICK_SPEED) != 5)
                            RandomTickModifier(serverOverWorld, 5);
                        if (serverOverWorld.getTimeOfDay() % 1200 == 0) {

//								this.sendMessage(Text.of("蓝月升起,触发事件"));
                            //执行间隔事件
                            spawnAnimalNearPlayer(serverOverWorld);
                        }
                    } else {
                        if (serverOverWorld.getGameRules().getInt(GameRules.RANDOM_TICK_SPEED) != 3) {
                            for (PlayerEntity player : server.getPlayerManager().getPlayerList())
                                player.sendMessage(Text.of("由于第一天的蓝月并没有随机刻增益,随机刻应该修改为3"), true);
                            RandomTickModifier(serverOverWorld, 3);
                        }
                    }
                    //应该是用world.找到所有玩家,这里无非就是避免客户端世界直接转服务器世界造成崩溃
                    //待改进:应该是this.getWorld,如果不是客户端世界再执行spawnAnimal方法

                }
            }
            //护甲更新,玩家游戏模式更新
            if (tickCount % (TICK_INTERVAL / 10) == 0) {
                for (ServerPlayerEntity serverPlayerEntity : server.getPlayerManager().getPlayerList()) {
                    OnServerInitializeMethod.updatePlayerArmor(serverPlayerEntity);
//					if(serverPlayerEntity.isCreative())
//						serverPlayerEntity.changeGameMode(GameMode.SURVIVAL);
                }
            }

            if (tickCount >= TICK_INTERVAL) {
//				// 获取所有世界并检查时间
//
//				for (ServerWorld world : server.getWorlds()) {
//					long worldTime = world.getTimeOfDay();
//
//					// 检查是否为主世界
//					if (world.getRegistryKey() == World.OVERWORLD) {
//						// 只有在主世界才执行下面的逻辑
//						// 执行主世界的相关逻辑
//						if (hasNewDayPassed(worldTime)) {
//							onNewDay(server, world,worldTime);
//						}
//					}
//				}

                tickCount = 0; // 重置 tick 计数器
            }
        });
        //使用物品监听器
        UseItemCallback.EVENT.register((player, world, hand) -> {
            // 获取玩家手中的物品
            ItemStack itemStack = player.getStackInHand(hand);

            // 判断是否为青金石
            if (itemStack.getItem() == Items.LAPIS_LAZULI) {
                return onUseCrystalItem(itemStack, player, world, 25);
            }
            if (itemStack.getItem() == Items.QUARTZ) {
                return onUseCrystalItem(itemStack, player, world, 50);
            }
            if (itemStack.getItem() == Items.DIAMOND) {
                return onUseCrystalItem(itemStack, player, world, 500);
            }

            if (itemStack.getItem() == Items.BOWL) {
                return vanillaBowlItemUse(world,player,hand,itemStack);
            }
            // 其他物品时不做处理
            return TypedActionResult.pass(itemStack);
        });


        //原版物品添加tooltip
        //不能和数据生成一起使用


        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
            // 判断物品是青金石（Lapis Lazuli）或其他物品
            if (stack.getItem() == Items.LAPIS_LAZULI) {
                lines.add(Text.literal("25XP").formatted(Formatting.DARK_GRAY));
            }
            if (stack.getItem() == Items.QUARTZ) {
                lines.add(Text.literal("50XP").formatted(Formatting.DARK_GRAY));
            }
            if (stack.getItem() == Items.DIAMOND) {
                lines.add(Text.literal("500XP").formatted(Formatting.DARK_GRAY));
            }
            if (stack.getItem() == Items.ENCHANTED_GOLDEN_APPLE) {
                lines.add(Text.literal("Regeneration II（00:40）").formatted(Formatting.BLUE));
                lines.add(Text.literal("Fire Resistance（00:40）").formatted(Formatting.BLUE));
            }
            if (stack.getItem() == Items.GOLDEN_APPLE) {
                lines.add(Text.literal("Regeneration I（00:20）").formatted(Formatting.BLUE));
            }
            if (stack.getItem() == Armors.MITHRIL_CHEST_PLATE) {
                lines.add(Text.literal("Regeneration: Doubles the natural health recovery rate").formatted(Formatting.BLUE));
            }
        });
//
//
//
//
//
//
//		});


        //食物修改
        foodComponentModify();





        //玩家食用食品监听器
//		OnPlayerEntityEatEvent.EVENT.register((player)->{
//			if(!player.getWorld().isClient()) {
//				player.sendMessage(Text.of("你吃掉了食物!"),true);
//			}
//			return ActionResult.SUCCESS;
//        });


        //合成金属镐监听器
        CraftingMetalPickAxeCallback.EVENT.register((world, player) -> {

//			DataFixer dataFixer = client.getDataFixer();  // 你需要初始化 DataFixer 实例
//			RegistryWrapper.WrapperLookup registryLookup = client.player.getRegistryManager();  // 同样初始化RegistryWrapper


            //创建持久状态类
            StateSaverAndLoader serverState;
            if (!world.isClient()) {
                serverState = StateSaverAndLoader.getServerState(world.getServer());
            } else {
                return ActionResult.PASS;
            }
            //直接访问成员变量即可
            boolean craftedIronPickaxe = serverState.isPickAxeCrafted;

            if (!craftedIronPickaxe) {
                if (!world.isClient()) {
                    serverState.isPickAxeCrafted = true;
                    player.sendMessage(Text.of("你第一次合成了金属镐"));
                } else
                    return ActionResult.PASS;
            } else {
                if (!world.isClient()) {
//					player.sendMessage(Text.of("你多次合成了铁镐"));
                    return ActionResult.PASS;
                } else
                    return ActionResult.PASS;
            }
            return ActionResult.PASS;
        });
        //命令注册
        CommandRegistrationCallback.EVENT.register(this::registerCommands);
        //附魔注册(记得把数据驱动部分也做好)
        registerAllOfEnchantments();
        //僵尸破坏方块进度列表
        init();
        //护甲添加
        registerArmors();
        //物品栏添加
        ModItemGroup.registerModItemGroup();
        //物品添加测试
        ModItems.registerModItemTest();
        //方块添加测试
        ModBlocks.registerModBlocks();
        //以下开始正式添加物品:

        //食物
        registerFoodItems();
        //添加硬币物品
        registerCoinItems();
        //添加工具物品
        Tools.registerModItemTools();
        //添加锭
        Metal.registerModItemIngots();
        //添加金属颗粒
        Metal.registerModItemNuggets();
        //粗矿
        registerModItemRaw();
        //注册矿物
        registerModOre();
        //注册实体
        registerModEntities();
        //修改战利品表(已经弃用,用数据包代替)
//		modifierLootTables();
        //注册事件
        PlayerBlockBreakEvents.AFTER.register(new BreakBlockEvent());
        //创建标签
        registerModBlockTags();
        registerModItemTags();
        //注册(药水)效果
        registerStatusEffects();


//		config = new Config();
//		config.load();

        registerScreenHandlers();



        BlockInit.registerBlocks();
        BlockInit.registerBlockItems();
        BlockInit.registerFuels();

        BlockEnityRegistry.init();
        CraftingIngredients.init();
        FurnaceIngredients.initFuel();
        FurnaceIngredients.initItem();

        CreativeGroup.addGroup();
        UseBlock.init();

        registrySoundEvents();
        modBlockEntityTypesInit();
        LOGGER.info("Hello Fabric world!");
    }


}