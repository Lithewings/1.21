package com.equilibrium;

import com.equilibrium.gen.ModLanguageTranslatorZhCn;
import com.equilibrium.gen.ModModelProvider;
import com.equilibrium.gen.ModRecipeGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class MITEequilibriumDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator){
		FabricDataGenerator.Pack pack=fabricDataGenerator.createPack();
//		pack.addProvider(ModRecipeGenerator::new);
//		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModLanguageTranslatorZhCn::new);
	}
}
