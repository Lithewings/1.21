package com.equilibrium.util;

import net.minecraft.world.World;

public class WorldTimeHelper {
    private static int day;
    private static World entityWorld;

    public static World getEntityWorld() {
        return entityWorld;
    }

    public static void setDay(World world){
        entityWorld=world;
        long time = world.getTimeOfDay();
        day =(int)( time / 24000L);
    }

    public static int getDay() {
        return day;
    }
}