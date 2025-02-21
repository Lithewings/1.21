package com.equilibrium;

import com.equilibrium.block.ModBlocks;

import com.equilibrium.craft_time_register.BlockInit;
import com.equilibrium.craft_time_register.UseBlock;
import com.equilibrium.entity.goal.BreakBlockGoal;
import com.equilibrium.event.BreakBlockEvent;
import com.equilibrium.event.CraftingMetalPickAxeCallback;
import com.equilibrium.item.Metal;
import com.equilibrium.item.ModItemGroup;
import com.equilibrium.item.ModItems;
import com.equilibrium.item.Tools;
import com.equilibrium.mixin.player.ClientPlayerEntityMixin;
import com.equilibrium.persistent_state.StateSaverAndLoader;
import com.equilibrium.util.ServerInfoRecorder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.option.GameOptions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameMode;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.equilibrium.config.Config;
import com.equilibrium.craft_time_register.BlockEnityRegistry;

import com.equilibrium.event.sound.SoundEventRegistry;
import com.equilibrium.util.CreativeGroup;
import com.equilibrium.craft_time_worklevel.CraftingIngredients;
import com.equilibrium.craft_time_worklevel.FurnaceIngredients;


import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.equilibrium.enchantments.EnchantmentsCodec.registerAllOfEnchantments;
import static com.equilibrium.entity.ModEntities.registerModEntities;


import static com.equilibrium.event.MoonPhaseEvent.*;
import static com.equilibrium.event.MoonPhaseEvent.RandomTickModifier;
import static com.equilibrium.event.sound.SoundEventRegistry.registrySoundEvents;
import static com.equilibrium.item.Armors.registerArmors;
import static com.equilibrium.item.Metal.registerModItemRaw;
import static com.equilibrium.item.extend_item.CoinItems.registerCoinItems;
import static com.equilibrium.status.registerStatusEffect.registerStatusEffects;
import static com.equilibrium.tags.ModBlockTags.registerModBlockTags;
import static com.equilibrium.tags.ModItemTags.registerModItemTags;
import static com.equilibrium.util.LootTableModifier.modifierLootTables;

import static com.equilibrium.ore_generator.ModPlacementGenerator.registerModOre;
import static com.equilibrium.util.ServerInfoRecorder.isServerInstanceSet;


public class MITEequilibrium implements ModInitializer {

	public static final String MOD_ID = "miteequilibrium";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Config config;





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





	// 注册命令的标准方式，适配 CommandDispatcher 的签名
	private void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
		// 注册 /locate structure 指令
//		dispatcher.register(
//				CommandManager.literal("locate")
//						.then(
//								CommandManager.literal("structure")
//										.then(
//												CommandManager.argument("structure", StringArgumentType.word())
//														.executes(context -> executeLocateStructure(context.getSource()))
//										)
//						)
//		);

//
		dispatcher.register(
				CommandManager.literal("village")
						.executes
								(this::isPickAxeCrafted)




		);

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

	// Logistic 曲线参数，可根据需求调整
	private static final double K = 10.0; // 陡峭度
	private static final double M = 0.5;  // 中点


	//玩家护甲值下降,套装集齐效果
	public static Text updatePlayerArmor(PlayerEntity player) {



		ArrayList<ItemStack> armorItemList = new ArrayList<>();
		player.getArmorItems().forEach(element->{
			if(element.getItem() instanceof ArmorItem)
				armorItemList.add(element);
				}
		);
		//空护甲时直接返回
		if(armorItemList.isEmpty()) {
			player.getAttributeInstance(EntityAttributes.GENERIC_ARMOR).setBaseValue(0.0D);
			return Text.of("The armor equipment is empty");
		}

		//由于耐久损耗,实际获得的护甲值
		double protection = 0;
		//最大护甲值,或者说满耐久护甲值
		double maxProtection = 0;

		for(ItemStack itemStack : armorItemList){
			if(itemStack.getItem() instanceof ArmorItem){
				ArmorItem armorItem = (ArmorItem) itemStack.getItem();
				//最大护甲值
				int baseProtection = armorItem.getProtection();
				//加到理论最大护甲值里面
				maxProtection = maxProtection + baseProtection;
				//最大耐久
				int baseDurability = itemStack.getMaxDamage();
				//目前耐久
				int durability = baseDurability - itemStack.getDamage();
				//满耐久一定获得满护甲值
				if (durability==baseDurability) {
					protection = protection + baseProtection;
				} else {
					//计算线性耐久度比例
					float linearRatio = (float) durability / baseDurability;
					//应用 S 型曲线 (Logistic) 做非线性衰减
					float sCurveRatio = (float) logisticFunction(linearRatio, K, M);
					//实际获得的护甲值
					double exactProtection = baseProtection * (sCurveRatio);
					//加到获得的总护甲值里面
					protection = protection + exactProtection;
				}
			}
		}
		//总护甲损耗
		double protectionReduction = maxProtection-protection;


		//设定玩家护甲
		player.getAttributeInstance(EntityAttributes.GENERIC_ARMOR).setBaseValue(-protectionReduction);
		//拥有至少10点护甲时,获得抗性提升效果
		if(protection>10) {
			boolean hasResistance = player.hasStatusEffect(StatusEffects.RESISTANCE);
			if (!hasResistance)
				player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 120, 0, false, false, false));
			else if (player.getStatusEffect(StatusEffects.RESISTANCE).getDuration()<=20) {
				player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 120, 0, false, false, false));
			}
		}


		Text text =Text.literal(String.format(
					"满耐久护甲=%.2f, 衰减系数=%.2f%%, 实际护甲=%.2f",
					maxProtection,
					100*(float)(1-protection/maxProtection),
					protection));

//		if(!player.getWorld().isClient())
//			player.sendMessage(text);
		return text;




		// 6. 可选：发送提示给玩家
//		if(!player.getWorld().isClient())
//			player.sendMessage(Text.literal(String.format(
//					"满耐久护甲=%.2f, 衰减系数=%.2f%%, 实际护甲=%.2f",
//					maxProtection,
//					100*(float)(1-protection/maxProtection),
//					protection
//			)), true);
	}




//        // 给玩家发送调试信息（可选）
//        player.sendMessage(Text.literal(
//                String.format(
//                        "当前耐久: %.0f%% -> 护甲系数(S曲线): %.1f%%",
//                        100 * linearRatio,
//                        100 * sCurveRatio
//                )
//        ), true);


	/**
	 * logisticFunction(r, k, m):
	 *   r: 线性比例 (0 ~ 1)
	 *   k: 陡峭度 (越大曲线越陡)
	 *   m: 中点 (0 ~ 1)
	 */
	private static double logisticFunction(double r, double k, double m) {
		// 避免溢出，可做一些极值保护
		// 例如 r 超过 [0,1] 范围时先 clamp 到 [0,1]
		r = Math.max(0.0, Math.min(1.0, r));

		return 1.0 / (1.0 + Math.exp(-k * (r - m)));
	}









//
//	 执行 locate structure 指令
//	private static int executeLocateStructure(ServerCommandSource source) throws CommandSyntaxException {
//        return 0;
//    }
public int isPickAxeCrafted(CommandContext<ServerCommandSource> context) {
	ServerCommandSource source = context.getSource();
	MinecraftServer server = source.getServer();
	StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(server);
	boolean isVillageCanGenerate=serverState.isPickAxeCrafted && ServerInfoRecorder.getDay()>=16;
	if(isVillageCanGenerate){
		if(context.getSource().getEntity().isPlayer())
			context.getSource().getEntity().sendMessage(Text.of("村庄可以生成了"));}
	else{
		if(context.getSource().getEntity().isPlayer())
			context.getSource().getEntity().sendMessage(Text.of("村庄还不能生成"));
	}
	return 1;
}



	private static final int TICK_INTERVAL = 500; // 每隔500 tick检查一次
	private int tickCount = 0; // 记录当前 tick
	// 判断世界是否进入新的一天
	private boolean hasNewDayPassed(long worldTime) {
		return worldTime % 24000L < 1000L; // 假设新的一天发生在世界时间的0到1000tick之间
	}

	// 新的一天到来时触发的事件
	private void onNewDay(MinecraftServer server, ServerWorld world, long worldTime) {
	}

	public static TypedActionResult<ItemStack> onUseCrystalItem(ItemStack itemStack , PlayerEntity player,World world,int experience){

		// 播放玻璃破碎的声音
		player.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1.0F, 1.0F);
		//经验球获取的声音
		player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
		// 返回成功，表示已处理

		//增加经验
		player.addExperience(experience);

		// 获取玩家眼前的方向和位置
		Vec3d eyePos = player.getEyePos();  // 玩家眼睛的位置
		Vec3d lookDir = player.getRotationVector();  // 玩家视线方向

		// 计算在玩家眼前的一定距离处的位置
		double distance = 0.5;  // 控制粒子生成的距离
		Vec3d particlePos = eyePos.add(lookDir.multiply(distance));

		// 创建物品材质的破碎粒子
		ItemStackParticleEffect particleEffect = new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack);

		// 生成青金石物品的破碎粒子
		for (int i = 0; i < 10; i++) {
			double xOffset = (Math.random() - 0.5) * 0.85;  // 随机偏移
			double yOffset = (Math.random() - 0.5) * 0.85;
			double zOffset = (Math.random() - 0.5) * 0.85;

			// 使用 `ITEM` 粒子类型生成青金石物品的破碎效果
			world.addParticle(particleEffect,
					particlePos.x + xOffset, particlePos.y + yOffset, particlePos.z + zOffset,
					0, 0, 0);  // 可根据需要调整粒子速度
		}
		//消耗一个晶体
		itemStack.setCount(itemStack.getCount()-1);
		return TypedActionResult.success(itemStack);

	}







	public void onInitialize() {
		//记录服务器实例
		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			ServerInfoRecorder.setServerInstance(server);  // 保存服务器实例
			ServerInfoRecorder.setDay((int) server.getWorld(World.OVERWORLD).getTimeOfDay());
			//锁定游戏难度:困难
			server.setDifficulty(Difficulty.HARD,true);
			server.setDifficultyLocked(true);
		});


		// 注册服务器 tick 事件
		ServerTickEvents.START_SERVER_TICK.register(server -> {
			// 若此前没有记录服务器实例,则记录一次
			if(!isServerInstanceSet() )
				ServerInfoRecorder.setServerInstance(server);
			// 每隔 TICK_INTERVAL 次 tick 触发一次检查
			tickCount++;
			//获取时间,得到月相,决定是否触发月相事件

			//月相事件
			String moonType = getMoonType(server.getWorld(World.OVERWORLD));
			ServerWorld serverOverWorld = server.getWorld(World.OVERWORLD);
			boolean isNoPlayersInTheOverWorld = serverOverWorld.getPlayers().isEmpty();
			Random random = new Random();


			if (isNoPlayersInTheOverWorld) {
				if (serverOverWorld.getGameRules().getInt(GameRules.RANDOM_TICK_SPEED) != 3)
					for (PlayerEntity player : server.getPlayerManager().getPlayerList())
						player.sendMessage(Text.of("由于主世界没有玩家,随机刻速度已回调至默认值"), true);
				RandomTickModifier(serverOverWorld, 3);

			}

			if (Objects.equals(moonType, "errorMoontype"))
				for (PlayerEntity player : server.getPlayerManager().getPlayerList())
					player.sendMessage(Text.of("月相加载失败"), true);
			else{
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
			if (tickCount %(TICK_INTERVAL/10) == 0) {
				for(ServerPlayerEntity serverPlayerEntity : server.getPlayerManager().getPlayerList()) {
					updatePlayerArmor(serverPlayerEntity);
//					if(serverPlayerEntity.isCreative())
//						serverPlayerEntity.changeGameMode(GameMode.SURVIVAL);
				}
			}

			if (tickCount >= TICK_INTERVAL) {
				// 获取所有世界并检查时间

				for (ServerWorld world : server.getWorlds()) {
					long worldTime = world.getTimeOfDay();
					//不管是哪个世界,总之先记录时间
					ServerInfoRecorder.setDay((int) worldTime);
					// 记录服务器实例
					ServerInfoRecorder.setServerInstance(server);

					// 检查是否为主世界
					if (world.getRegistryKey() == World.OVERWORLD) {
						// 只有在主世界才执行下面的逻辑
						// 执行主世界的相关逻辑
						if (hasNewDayPassed(worldTime)) {
							onNewDay(server, world,worldTime);
						}
					}
				}

				tickCount = 0; // 重置 tick 计数器
			}
		});




		//使用物品监听器
		UseItemCallback.EVENT.register((player, world, hand) -> {
			// 获取玩家手中的物品
			ItemStack itemStack = player.getStackInHand(hand);

			// 判断是否为青金石
			if (itemStack.getItem() == Items.LAPIS_LAZULI) {
				return onUseCrystalItem(itemStack,player,world,25);
			}
			if (itemStack.getItem() == Items.QUARTZ) {
				return onUseCrystalItem(itemStack,player,world,50);
			}
			if (itemStack.getItem() == Items.DIAMOND) {
				return onUseCrystalItem(itemStack,player,world,500);
			}



			// 其他物品时不做处理
			return TypedActionResult.pass(itemStack);
        });

		//原版物品添加tooltip
		//不能和数据生成一起使用

//		ItemTooltipCallback.EVENT.register((stack, context, type,lines) -> {
//			// 判断物品是青金石（Lapis Lazuli）或其他物品
//			if (stack.getItem() == Items.LAPIS_LAZULI) {
//				lines.add(Text.literal("每个25XP").formatted(Formatting.DARK_GRAY));
//			}
//			if (stack.getItem() == Items.QUARTZ) {
//				lines.add(Text.literal("每个50XP").formatted(Formatting.DARK_GRAY));
//			}
//			if (stack.getItem() == Items.DIAMOND) {
//				lines.add(Text.literal("每个500XP").formatted(Formatting.DARK_GRAY));
//			}
//			if (stack.getItem() == Items.GOLDEN_APPLE) {
//				lines.add(Text.literal("生命恢复 II（00:40）").formatted(Formatting.BLUE));
//				lines.add(Text.literal("抗火（00:40）").formatted(Formatting.BLUE));
//			}
//		});


		//玩家食用食品监听器
//		OnPlayerEntityEatEvent.EVENT.register((player)->{
//			if(!player.getWorld().isClient()) {
//				player.sendMessage(Text.of("你吃掉了食物!"),true);
//			}
//			return ActionResult.SUCCESS;
//        });



		//合成金属镐监听器
		CraftingMetalPickAxeCallback.EVENT.register((world,player) -> {

//			DataFixer dataFixer = client.getDataFixer();  // 你需要初始化 DataFixer 实例
//			RegistryWrapper.WrapperLookup registryLookup = client.player.getRegistryManager();  // 同样初始化RegistryWrapper



			//创建持久状态类
			StateSaverAndLoader serverState;
			if(!world.isClient()){
				//为这个新创建的类赋值,获取到服务器实例,这个实例在一开始从本地文件载入,然后复制一份到ServerInfoRecorder中去,所以这是可以读取到本地文件数据的
				serverState = StateSaverAndLoader.getServerState(ServerInfoRecorder.getServerInstance());
			}else{
				return ActionResult.PASS;
			}
			//直接访问成员变量即可
			boolean craftedIronPickaxe = serverState.isPickAxeCrafted;

			if(!craftedIronPickaxe){
				if(!world.isClient()) {
					serverState.isPickAxeCrafted =true;
					player.sendMessage(Text.of("你第一次合成了金属镐"));
				}
				else
					return ActionResult.PASS;
			}else {
				if(!world.isClient()) {
//					player.sendMessage(Text.of("你多次合成了铁镐"));
					return ActionResult.PASS;
				}
				else
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


		//修改战利品表
		modifierLootTables();

		//注册事件
		PlayerBlockBreakEvents.AFTER.register(new BreakBlockEvent());



		//创建标签
		registerModBlockTags();
		registerModItemTags();

		//注册(药水)效果
		registerStatusEffects();




		config = new Config();
		config.load();

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

		LOGGER.info("Hello Fabric world!");
	}




}