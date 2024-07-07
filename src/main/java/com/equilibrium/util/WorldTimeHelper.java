package com.equilibrium.util;

public class WorldTimeHelper {
    private static long worldTime=0;

    public static void setWorldTime(long time) {
        worldTime = time;
    }
    public static long getWorldTime() {
        return worldTime;
    }
    public static int getMoonPhase(long time){
        return (int)(time / 24000L) % 128;
    }
}
