package com.equilibrium.craft_time_worklevel;

public class CraftingIngredient {
    public static final int FLINT_CRAFTING_TABLE = 1, COPPER_CRAFTING_TABLE = 2, IRON_CRAFTING_TABLE = 3, DIAMOND_CRAFTING_TABLE = 4, NETHERITE_CRAFTING_TABLE = 5;
    public int workLevel;
    public CraftingIngredient(int workLevel) {
        this.workLevel = workLevel;
    }
}
