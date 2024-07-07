package com.equilibrium;

import com.equilibrium.block.ModBlocks;

import com.equilibrium.item.Ingots;
import com.equilibrium.item.ModItemGroup;
import com.equilibrium.item.ModItems;
import com.equilibrium.item.Tools;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.equilibrium.worldgen.ModOreGenerator.registerModOre;


public class MITEequilibrium implements ModInitializer {

	public static final String MOD_ID = "miteequilibrium";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);





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
		Ingots.registerModItemIngots();

		//注册矿物

		registerModOre();










		LOGGER.info("Hello Fabric world!");
	}
}