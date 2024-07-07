package com.equilibrium.util;

import net.minecraft.entity.player.PlayerEntity;

import java.util.logging.Logger;

import static com.equilibrium.MITEequilibrium.LOGGER;


public class PlayerMaxHungerHelper {
    private static int maxFoodLevel=20;

    public static int getMaxFoodLevel() {
        return maxFoodLevel;
    }

    public static void setMaxFoodLevel(int calculatedPlayerLevel){
        maxFoodLevel = calculatedPlayerLevel;
    }


}
