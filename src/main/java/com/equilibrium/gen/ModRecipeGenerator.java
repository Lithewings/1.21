package com.equilibrium.gen;

import com.equilibrium.item.Ingots;
import com.equilibrium.item.Tools;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModRecipeGenerator extends FabricRecipeProvider {
    public ModRecipeGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, Tools.adamantium_axe).
                pattern("XX").
                pattern("XY").
                pattern(" Y").
                input('X', Ingots.adamantium).
                input('Y',Items.STICK).
                criterion(FabricRecipeProvider.hasItem(Ingots.adamantium),
                        FabricRecipeProvider.conditionsFromItem(Ingots.adamantium)).offerTo(exporter, Identifier.of("adamantium_axe"));


    }
}
