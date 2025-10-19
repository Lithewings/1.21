package com.equilibrium.util;

import com.equilibrium.persistent_state.StateSaverAndLoader;
import net.minecraft.server.MinecraftServer;

public class ServerInfoRecorder {
    //在玩家进入世界时,应该自动记录一次世界时间

    private static int day = 0;

    public static void setDay(int timeOfTheWorld){
        day = (int) (timeOfTheWorld / 24000L);
    }


    public static int getDay() {
        return day;
    }
}