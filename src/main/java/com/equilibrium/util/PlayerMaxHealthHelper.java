package com.equilibrium.util;


import static com.equilibrium.MITEequilibrium.LOGGER;

public class PlayerMaxHealthHelper {
    //一切生命上限的值都从这里取得,初始为20,是玩家能够获得的最大值,作为最初赋值setHealth最大值
    private static int maxHealthLevel=20;




    public static int getMaxHealthLevel() {
        return maxHealthLevel;
    }

    public static void setMaxHealthLevel(int calculatedPlayerMaxLevel){
//        LOGGER.info("setMaxHealthLevel is: "+calculatedPlayerMaxLevel);
        maxHealthLevel = calculatedPlayerMaxLevel;
    }


}
