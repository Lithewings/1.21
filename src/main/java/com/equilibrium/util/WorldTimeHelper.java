package com.equilibrium.util;

public class WorldTimeHelper {
    private static int day;
    public static void setDay(long time){
        day =(int)( time / 24000L);
    }

    public static int getDay() {
        return day;
    }
}