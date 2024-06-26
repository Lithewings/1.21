package com.equilibrium;

import com.equilibrium.block.ModBlocksTest;
import com.equilibrium.item.Ingots;
import com.equilibrium.item.ModItemGroup;
import com.equilibrium.item.ModItems;
import com.equilibrium.item.Tools;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
		ModBlocksTest.registerModBlocks();

		//以下开始正式添加物品:


		//添加工具物品
		Tools.registerModItemTools();
		//添加锭
		Ingots.registerModItemIngots();












		LOGGER.info("Hello Fabric world!");
	}
}