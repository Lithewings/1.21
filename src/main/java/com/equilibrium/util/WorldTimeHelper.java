package com.equilibrium.util;

public class WorldTimeHelper {
    public static int getDay(long time){
        int day =(int)(time / 24000L);
        return day;
    }
}
