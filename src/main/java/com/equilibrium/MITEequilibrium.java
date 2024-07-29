package com.equilibrium;

import com.equilibrium.block.ModBlocks;

import com.equilibrium.event.BreakBlockEvent;
import com.equilibrium.item.Metal;
import com.equilibrium.item.ModItemGroup;
import com.equilibrium.item.ModItems;
import com.equilibrium.item.Tools;
import com.equilibrium.register.tags.ModItemTags;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.equilibrium.config.Config;
import com.equilibrium.register.BlockEnityRegistry;
import com.equilibrium.register.BlockInit;
import com.equilibrium.register.UseBlock;
import com.equilibrium.event.sound.SoundEventRegistry;
import com.equilibrium.util.CreativeGroup;
import com.equilibrium.worklevel.CraftingIngredients;
import com.equilibrium.worklevel.FurnaceIngredients;


import static com.equilibrium.entity.ModEntities.registerModEntities;


import static com.equilibrium.register.tags.ModBlockTags.registerModBlockTags;
import static com.equilibrium.register.tags.ModItemTags.registerModItemTags;
import static com.equilibrium.util.LootTableModifier.modifierLootTables;

import static com.equilibrium.worldgen.ModPlacementGenerator.registerModOre;


public class MITEequilibrium implements ModInitializer {

	public static final String MOD_ID = "miteequilibrium";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


	public static Config config;



	@Override
	public void onInitialize() {
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