package com.equilibrium.mixin.tool;

import net.minecraft.block.Block;
import net.minecraft.enchantment.effect.entity.DamageEntityEnchantmentEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.equilibrium.MITEequilibrium.LOGGER;

@Mixin(PickaxeItem.class)
public abstract class PickaxeItemMixin extends MiningToolItem {
    public PickaxeItemMixin(ToolMaterial toolMaterial, TagKey<Block> effectiveBlocks, Settings settings) {
        super(toolMaterial, effectiveBlocks, settings);
    }


}
