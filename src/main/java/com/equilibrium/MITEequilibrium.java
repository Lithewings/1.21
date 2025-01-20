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
import com.equilibrium.persistent_state.State;
import com.equilibrium.persistent_state.StateSaverAndLoader;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.DataFixer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.equilibrium.config.Config;
import com.equilibrium.craft_time_register.BlockEnityRegistry;

import com.equilibrium.event.sound.SoundEventRegistry;
import com.equilibrium.util.CreativeGroup;
import com.equilibrium.craft_time_worklevel.CraftingIngredients;
import com.equilibrium.craft_time_worklevel.FurnaceIngredients;


import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.equilibrium.entity.ModEntities.registerModEntities;


import static com.equilibrium.item.Metal.registerModItemRaw;
import static com.equilibrium.status.registerStatusEffect.registerStatusEffects;
import static com.equilibrium.tags.ModBlockTags.registerModBlockTags;
import static com.equilibrium.tags.ModItemTags.registerModItemTags;
import static com.equilibrium.util.LootTableModifier.modifierLootTables;

import static com.equilibrium.ore_generator.ModPlacementGenerator.registerModOre;


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


//
//	 执行 locate structure 指令
//	private static int executeLocateStructure(ServerCommandSource source) throws CommandSyntaxException {
//        return 0;
//    }
public int isPickAxeCrafted(CommandContext<ServerCommandSource> context) {
	ServerCommandSource source = context.getSource();
	MinecraftServer server = source.getServer();
	StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(server);
	if(serverState.savedValue)
		if(context.getSource().getEntity().isPlayer())
			context.getSource().getEntity().sendMessage(Text.of("村庄可以生成了"));
	if(!serverState.savedValue)
		if(context.getSource().getEntity().isPlayer())
			context.getSource().getEntity().sendMessage(Text.of("村庄还不能生成"));
	return 1;
}






	public void onInitialize() {











		CraftingMetalPickAxeCallback.EVENT.register((world,player) -> {

//			DataFixer dataFixer = client.getDataFixer();  // 你需要初始化 DataFixer 实例
//			RegistryWrapper.WrapperLookup registryLookup = client.player.getRegistryManager();  // 同样初始化RegistryWrapper








			StateSaverAndLoader serverState;
			if(!world.isClient()){
				serverState = StateSaverAndLoader.getServerState(world.getServer());
			}else{
				return ActionResult.PASS;
			}

			boolean craftedIronPickaxe = serverState.savedValue;

			if(!craftedIronPickaxe){
				if(!world.isClient()) {
					serverState.savedValue=true;
					player.sendMessage(Text.of("你第一次合成了铁镐"));
				}
				else
					return ActionResult.PASS;
			}else {
				if(!world.isClient()) {
					player.sendMessage(Text.of("你多次合成了铁镐"));
					return ActionResult.PASS;
				}
				else
					return ActionResult.PASS;
			}
			return ActionResult.PASS;
		});




		CommandRegistrationCallback.EVENT.register(this::registerCommands);


		init();
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