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
import com.equilibrium.persistent_state.StateSaverAndLoader;
import com.equilibrium.util.ServerInfoRecorder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
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
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.equilibrium.entity.ModEntities.registerModEntities;


import static com.equilibrium.item.Armors.registerArmors;
import static com.equilibrium.item.Metal.registerModItemRaw;
import static com.equilibrium.status.registerStatusEffect.registerStatusEffects;
import static com.equilibrium.tags.ModBlockTags.registerModBlockTags;
import static com.equilibrium.tags.ModItemTags.registerModItemTags;
import static com.equilibrium.util.LootTableModifier.modifierLootTables;

import static com.equilibrium.ore_generator.ModPlacementGenerator.registerModOre;
import static com.equilibrium.util.ServerInfoRecorder.isServerInstanceSet;
import static net.minecraft.component.DataComponentTypes.ATTRIBUTE_MODIFIERS;


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


	//玩家护甲值下降
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
		// 触发新的一天事件
//		server.getPlayerManager().getPlayerList().forEach(player -> {
//			player.sendMessage(Text.literal("新的一天开始了！"), false);
//		});



	}


	public void onInitialize() {

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			ServerInfoRecorder.setServerInstance(server);  // 保存服务器实例
		});


		// 注册服务器 tick 事件
		ServerTickEvents.START_SERVER_TICK.register(server -> {
			// 记录服务器实例
			if(!isServerInstanceSet() )
				ServerInfoRecorder.setServerInstance(server);
			// 每隔 TICK_INTERVAL 次 tick 触发一次检查
			tickCount++;
			//护甲更新
			if (tickCount >= TICK_INTERVAL/10) {
				for(ServerPlayerEntity serverPlayerEntity : server.getPlayerManager().getPlayerList())
					updatePlayerArmor(serverPlayerEntity);
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









		CraftingMetalPickAxeCallback.EVENT.register((world,player) -> {

//			DataFixer dataFixer = client.getDataFixer();  // 你需要初始化 DataFixer 实例
//			RegistryWrapper.WrapperLookup registryLookup = client.player.getRegistryManager();  // 同样初始化RegistryWrapper








			StateSaverAndLoader serverState;
			if(!world.isClient()){
				serverState = StateSaverAndLoader.getServerState(ServerInfoRecorder.getServerInstance());
			}else{
				return ActionResult.PASS;
			}

			boolean craftedIronPickaxe = serverState.isPickAxeCrafted;

			if(!craftedIronPickaxe){
				if(!world.isClient()) {
					serverState.isPickAxeCrafted =true;
					player.sendMessage(Text.of("你第一次合成了铁镐"));
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




		CommandRegistrationCallback.EVENT.register(this::registerCommands);


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
		Registry.register(Registries.SOUND_EVENT, SoundEventRegistry.finishSoundID, SoundEventRegistry.finishSound);



		LOGGER.info("Hello Fabric world!");
	}




}