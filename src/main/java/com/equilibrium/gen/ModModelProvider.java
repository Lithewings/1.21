package com.equilibrium.gen;

import com.equilibrium.block.ModBlocks;
import com.equilibrium.item.Metal;
import com.equilibrium.item.Tools;
import com.equilibrium.item.tools.ModToolMaterials;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.Item;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }


    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.ADAMANTIUM_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.COPPER_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.SILVER_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.MITHRIL_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.ANCIENT_METAL_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.GOLD_BLOCK);

        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.ADAMANTIUM_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.COPPER_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.SILVER_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.MITHRIL_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.GOLD_ORE);

//        blockStateModelGenerator.registerItemModel(ModBlocks.ADAMANTIUM_BLOCK);
//        blockStateModelGenerator.registerItemModel(ModBlocks.COPPER_BLOCK);
//        blockStateModelGenerator.registerItemModel(ModBlocks.SILVER_BLOCK);
//        blockStateModelGenerator.registerItemModel(ModBlocks.MITHRIL_BLOCK);
//        blockStateModelGenerator.registerItemModel(ModBlocks.ANCIENT_METAL_BLOCK);
//        blockStateModelGenerator.registerItemModel(ModBlocks.GOLD_BLOCK);
//
//        blockStateModelGenerator.registerItemModel(ModBlocks.ADAMANTIUM_ORE);
//        blockStateModelGenerator.registerItemModel(ModBlocks.COPPER_ORE);
//        blockStateModelGenerator.registerItemModel(ModBlocks.SILVER_ORE);
//        blockStateModelGenerator.registerItemModel(ModBlocks.MITHRIL_ORE);
//        blockStateModelGenerator.registerItemModel(ModBlocks.GOLD_ORE);
    }


    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(Metal.adamantium_nugget, Models.GENERATED);
        itemModelGenerator.register(Metal.ancient_metal_nugget, Models.GENERATED);
        itemModelGenerator.register(Metal.copper_nugget, Models.GENERATED);
        itemModelGenerator.register(Metal.gold_nugget, Models.GENERATED);
        itemModelGenerator.register(Metal.mithril_nugget, Models.GENERATED);
        itemModelGenerator.register(Metal.silver_nugget, Models.GENERATED);


        itemModelGenerator.register(Tools.FLINT_HATCHET, Models.HANDHELD);

    }
}
