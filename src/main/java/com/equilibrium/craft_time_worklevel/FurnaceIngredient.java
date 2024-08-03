package com.equilibrium.craft_time_worklevel;

public class FurnaceIngredient {
    public static final int COBBLESTONE_FURNACE = 1, OBSIDIAN_FURNACE = 2, NETHERRACK_FURNACE = 3;
    public int workLevel;

    public FurnaceIngredient(int workLevel) {
        this.workLevel = workLevel;
    }
}
