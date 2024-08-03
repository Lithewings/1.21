package com.equilibrium.item.tools_attribute;

import com.equilibrium.item.Metal;
import com.google.common.base.Suppliers;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;

import java.util.function.Supplier;

public enum ModToolMaterials implements ToolMaterial {

    //燧石武器无法附魔
    FLINT_HATCHET(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 360, 2.0F, 0.0F, 0, () -> Ingredient.ofItems(Items.FLINT)),
    FLINT_AXE(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 1200, 8.0F, 0.0F, 0, () -> Ingredient.ofItems(Items.FLINT)),
    FLINT_SHOVEL(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 360, 2F, 0.0F, 0, () -> Ingredient.ofItems(Items.FLINT)),
    FLINT_KNIFE(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 360, 4.0F, 0.0F, 0, () -> Ingredient.ofItems(Items.FLINT)),

    COPPER_AXE(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 4800, 12.0F, 0.0F, 15, () -> Ingredient.ofItems(Metal.copper)),
    COOPER_SHOVEL(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 1600, 4.0F, 0.0F, 15, () -> Ingredient.ofItems(Metal.copper)),
    COOPER_DAGGER(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 1600, 12.0F, 0.0F, 15, () -> Ingredient.ofItems(Metal.copper)),
    COOPER_PICKAXE(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 4800, 12.0F, 0.0F, 15, () -> Ingredient.ofItems(Metal.copper)),
    COOPER_SWORD(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 3200, 12.0F, 0.0F, 15, () -> Ingredient.ofItems(Metal.copper)),
    COOPER_HOE(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 3200, 12.0F, 0.0F, 15, () -> Ingredient.ofItems(Metal.copper)),

    SILVER_AXE(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 4800, 12.0F, 0.0F, 15, () -> Ingredient.ofItems(Metal.silver)),
    SILVER_SHOVEL(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 1600, 4.0F, 0.0F, 15, () -> Ingredient.ofItems(Metal.silver)),
    SILVER_DAGGER(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 1600, 12.0F, 0.0F, 15, () -> Ingredient.ofItems(Metal.silver)),
    SILVER_PICKAXE(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 4800, 12.0F, 0.0F, 15, () -> Ingredient.ofItems(Metal.silver)),
    SILVER_SWORD(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 3200, 12.0F, 0.0F, 15, () -> Ingredient.ofItems(Metal.silver)),
    SILVER_HOE(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 3200, 12.0F, 0.0F, 15, () -> Ingredient.ofItems(Metal.silver)),




    //金武器更容易获得高级附魔,采集速度也更快
    GOLD_AXE(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 4800, 18.0F, 0.0F, 25, () -> Ingredient.ofItems(Metal.gold)),
    GOLD_SHOVEL(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 1600, 6.0F, 0.0F, 25, () -> Ingredient.ofItems(Metal.gold)),
    GOLD_DAGGER(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 1600, 18.0F, 0.0F, 25, () -> Ingredient.ofItems(Metal.gold)),
    GOLD_PICKAXE(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 4800, 18.0F, 0.0F, 25, () -> Ingredient.ofItems(Metal.gold)),
    GOLD_SWORD(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 3200, 18.0F, 0.0F, 25, () -> Ingredient.ofItems(Metal.gold)),
    GOLD_HOE(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 3200, 18.0F, 0.0F, 25, () -> Ingredient.ofItems(Metal.gold)),


    IRON_AXE(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 9600, 16.0F, 0.0F, 15, () -> Ingredient.ofItems(Items.IRON_INGOT)),
    IRON_SHOVEL(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 3200, 8.0F, 0.0F, 15, () -> Ingredient.ofItems(Items.IRON_INGOT)),
    IRON_DAGGER(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 3200, 16.0F, 0.0F, 15, () -> Ingredient.ofItems(Items.IRON_INGOT)),
    IRON_PICKAXE(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 9600, 16.0F, 0.0F, 15, () -> Ingredient.ofItems(Items.IRON_INGOT)),
    IRON_SWORD(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 6400, 16.0F, 0.0F, 15, () -> Ingredient.ofItems(Items.IRON_INGOT)),
    IRON_HOE(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 6400, 16.0F, 0.0F, 15, () -> Ingredient.ofItems(Items.IRON_INGOT)),

    MITHRIL_AXE(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 76800, 32.0F, 0.0F, 25, () -> Ingredient.ofItems(Metal.mithril)),
    MITHRIL_SHOVEL(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 25600, 12.0F, 0.0F, 25, () -> Ingredient.ofItems(Metal.mithril)),
    MITHRIL_DAGGER(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 25600, 32.0F, 0.0F, 25, () -> Ingredient.ofItems(Metal.mithril)),
    MITHRIL_PICKAXE(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 76800, 32.0F, 0.0F, 25, () -> Ingredient.ofItems(Metal.mithril)),
    MITHRIL_SWORD(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 51200, 32.0F, 0.0F, 25, () -> Ingredient.ofItems(Metal.mithril)),
    MITHRIL_HOE(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 51200, 32.0F, 0.0F, 25, () -> Ingredient.ofItems(Metal.mithril)),


    ADAMANTIUM_AXE(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 309600, 32.0F, 0.0F, 25, () -> Ingredient.ofItems(Metal.adamantium)),
    ADAMANTIUM_SHOVEL(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 102400, 16.0F, 0.0F, 25, () -> Ingredient.ofItems(Metal.adamantium)),
    ADAMANTIUM_DAGGER(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 102400, 32.0F, 0.0F, 25, () -> Ingredient.ofItems(Metal.adamantium)),
    ADAMANTIUM_PICKAXE(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 309600, 32.0F, 0.0F, 25, () -> Ingredient.ofItems(Metal.adamantium)),
    ADAMANTIUM_SWORD(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 204800, 32.0F, 0.0F, 25, () -> Ingredient.ofItems(Metal.adamantium)),
    ADAMANTIUM_HOE(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 204800, 32.0F, 0.0F, 25, () -> Ingredient.ofItems(Metal.adamantium));

    private final TagKey<Block> inverseTag;
    private final int itemDurability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repairIngredient;

    private ModToolMaterials(
            final TagKey<Block> inverseTag,
            final int itemDurability,
            final float miningSpeed,
            final float attackDamage,
            final int enchantability,
            final Supplier<Ingredient> repairIngredient
    ) {
        this.inverseTag = inverseTag;
        this.itemDurability = itemDurability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairIngredient = Suppliers.memoize(repairIngredient::get);
    }

    @Override
    public int getDurability() {
        return this.itemDurability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return this.miningSpeed;
    }

    @Override
    public float getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    public TagKey<Block> getInverseTag() {
        return this.inverseTag;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return (Ingredient)this.repairIngredient.get();
    }
}
