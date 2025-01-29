package com.equilibrium.mixin.potion;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(BrewingRecipeRegistry.class)
public class BrewingRecipeRegistryMixin {


    @Final
    @Shadow
    private  List<BrewingRecipeRegistry.Recipe<Potion>> potionRecipes;
    @Final
    @Shadow
    private  List<BrewingRecipeRegistry.Recipe<Item>> itemRecipes;

    @Inject(method = "craft",at =@At("HEAD"))
    public void craft(ItemStack ingredient, ItemStack input, CallbackInfoReturnable<ItemStack> cir) {

//        cir.cancel();
//        if (input.isEmpty()) {
//            cir.setReturnValue(input);
//        } else {
//            Optional<RegistryEntry<Potion>> optional = input.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).potion();
//            if (optional.isEmpty()) {
//                cir.setReturnValue(input);
//            } else {
//                for  (BrewingRecipeRegistry.Recipe<Item> recipe : this.itemRecipes) {
//                    if (input.itemMatches(recipe.from) && recipe.ingredient.test(ingredient)) {
//                        cir.setReturnValue( PotionContentsComponent.createStack(recipe.to.value(), (RegistryEntry<Potion>) optional.get()));
//                    }
//                }
//
//                for (BrewingRecipeRegistry.Recipe<Potion> recipex : this.potionRecipes) {
//                    if (recipex.from.matches((RegistryEntry<Potion>) optional.get()) && recipex.ingredient.test(ingredient)) {
//                        cir.setReturnValue( PotionContentsComponent.createStack(input.getItem(), recipex.to));
//                    }
//                }
//            }
//        }

    }
}
