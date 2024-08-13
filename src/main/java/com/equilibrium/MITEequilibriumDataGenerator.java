package com.equilibrium;

import com.equilibrium.gen.ModLootTableProvider;
import com.equilibrium.gen.ModModelProvider;
import com.equilibrium.gen.ModRecipeGenerator;
import com.equilibrium.gen.TagsProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class MITEequilibriumDataGenerator implements DataGeneratorEntrypoint {


	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator){
		FabricDataGenerator.Pack pack=fabricDataGenerator.createPack();
//		pack.addProvider(ModRecipeGenerator::new);
//		pack.addProvider(ModModelProvider::new);
//		pack.addProvider(ModLanguageTranslatorZhCn::new);
//		pack.addProvider(TagsProvider::new);
		pack.addProvider(ModLootTableProvider::new);

	}
}
