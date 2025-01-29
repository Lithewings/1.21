package com.equilibrium.gen;

import com.equilibrium.block.ModBlocks;
import com.equilibrium.item.Armors;
import com.equilibrium.item.Metal;
import com.equilibrium.item.Tools;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.ArmorItem;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }


    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
//        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.ADAMANTIUM_BLOCK);
//        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.COPPER_BLOCK);
//        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.SILVER_BLOCK);
//        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.MITHRIL_BLOCK);
//        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.ANCIENT_METAL_BLOCK);
//        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.GOLD_BLOCK);
//
//        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.ADAMANTIUM_ORE);
//        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.COPPER_ORE);
//        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.SILVER_ORE);
//        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.MITHRIL_ORE);
//        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.GOLD_ORE);

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

//        itemModelGenerator.register(Metal.ADAMANTIUM_RAW, Models.GENERATED);
//        itemModelGenerator.register(Metal.MITHRIL_RAW, Models.GENERATED);
//        itemModelGenerator.register(Metal.SILVER_RAW, Models.GENERATED);

        itemModelGenerator.registerArmor((ArmorItem) Armors.COPPER_BOOTS);
        itemModelGenerator.registerArmor((ArmorItem) Armors.COPPER_CHEST_PLATE);
        itemModelGenerator.registerArmor((ArmorItem) Armors.COPPER_HELMET);
        itemModelGenerator.registerArmor((ArmorItem)Armors.COPPER_LEGGINGS);

//        itemModelGenerator.register(Metal.adamantium_nugget, Models.GENERATED);
//        itemModelGenerator.register(Metal.ancient_metal_nugget, Models.GENERATED);
//        itemModelGenerator.register(Metal.copper_nugget, Models.GENERATED);
//        itemModelGenerator.register(Metal.gold_nugget, Models.GENERATED);
//        itemModelGenerator.register(Metal.mithril_nugget, Models.GENERATED);
//        itemModelGenerator.register(Metal.silver_nugget, Models.GENERATED);
//        itemModelGenerator.register(Metal.FLINT, Models.GENERATED);
//
//
//        itemModelGenerator.register(Tools.FLINT_HATCHET, Models.HANDHELD);
//        itemModelGenerator.register(Tools.FLINT_AXE, Models.HANDHELD);
//        itemModelGenerator.register(Tools.FLINT_KNIFE, Models.HANDHELD);
//        itemModelGenerator.register(Tools.FLINT_SHOVEL, Models.HANDHELD);
//
//        itemModelGenerator.register(Tools.IRON_AXE, Models.HANDHELD);
//        itemModelGenerator.register(Tools.ADAMANTIUM_AXE, Models.HANDHELD);
//        itemModelGenerator.register(Tools.GOLD_AXE, Models.HANDHELD);
//        itemModelGenerator.register(Tools.COPPER_AXE, Models.HANDHELD);
//        itemModelGenerator.register(Tools.SILVER_AXE, Models.HANDHELD);
//        itemModelGenerator.register(Tools.MITHRIL_AXE, Models.HANDHELD);
//
//        itemModelGenerator.register(Tools.IRON_HOE, Models.HANDHELD);
//        itemModelGenerator.register(Tools.ADMANTIUM_HOE, Models.HANDHELD);
//        itemModelGenerator.register(Tools.GOLD_HOE, Models.HANDHELD);
//        itemModelGenerator.register(Tools.COPPER_HOE, Models.HANDHELD);
//        itemModelGenerator.register(Tools.SILVER_HOE, Models.HANDHELD);
//        itemModelGenerator.register(Tools.MITHRIL_HOE, Models.HANDHELD);
//
//        itemModelGenerator.register(Tools.IRON_DAGGER, Models.HANDHELD);
//        itemModelGenerator.register(Tools.ADMANTIUM_DAGGER, Models.HANDHELD);
//        itemModelGenerator.register(Tools.GOLD_DAGGER, Models.HANDHELD);
//        itemModelGenerator.register(Tools.COPPER_DAGGER, Models.HANDHELD);
//        itemModelGenerator.register(Tools.SILVER_DAGGER, Models.HANDHELD);
//        itemModelGenerator.register(Tools.MITHRIL_DAGGER, Models.HANDHELD);
//
//        itemModelGenerator.register(Tools.IRON_SHOVEL, Models.HANDHELD);
//        itemModelGenerator.register(Tools.ADMANTIUM_SHOVEL, Models.HANDHELD);
//        itemModelGenerator.register(Tools.GOLD_SHOVEL, Models.HANDHELD);
//        itemModelGenerator.register(Tools.COPPER_SHOVEL, Models.HANDHELD);
//        itemModelGenerator.register(Tools.SILVER_SHOVEL, Models.HANDHELD);
//        itemModelGenerator.register(Tools.MITHRIL_SHOVEL, Models.HANDHELD);
//
//        itemModelGenerator.register(Tools.IRON_SWORD, Models.HANDHELD);
//        itemModelGenerator.register(Tools.ADMANTIUM_SWORD, Models.HANDHELD);
//        itemModelGenerator.register(Tools.GOLD_SWORD, Models.HANDHELD);
//        itemModelGenerator.register(Tools.COPPER_SWORD, Models.HANDHELD);
//        itemModelGenerator.register(Tools.SILVER_SWORD, Models.HANDHELD);
//        itemModelGenerator.register(Tools.MITHRIL_SWORD, Models.HANDHELD);
//
//        itemModelGenerator.register(Tools.IRON_PICKAXE, Models.HANDHELD);
//        itemModelGenerator.register(Tools.ADMANTIUM_PICKAXE, Models.HANDHELD);
//        itemModelGenerator.register(Tools.GOLD_PICKAXE, Models.HANDHELD);
//        itemModelGenerator.register(Tools.COPPER_PICKAXE, Models.HANDHELD);
//        itemModelGenerator.register(Tools.SILVER_PICKAXE, Models.HANDHELD);
//        itemModelGenerator.register(Tools.MITHRIL_PICKAXE, Models.HANDHELD);
    }
}
