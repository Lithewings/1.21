package com.equilibrium.util;

import com.equilibrium.persistent_state.StateSaverAndLoader;
import net.minecraft.server.MinecraftServer;

public class ServerInfoRecorder {
    //在玩家进入世界时,应该自动记录一次世界时间



    //可以获取一个时间的服务器实例
    private static MinecraftServer serverInstance;

    private static int day = 0;

    public static MinecraftServer getServerInstance() {
        return serverInstance;
    }

    // 单例设置服务器实例
    public static void setServerInstance(MinecraftServer server) {
        serverInstance = server;
    }

    // 判断是否已经设置过服务器实例
    public static boolean isServerInstanceSet() {
        if(serverInstance == null)
            return false;
        else
            return true;
    }
    //判断是否制作了稿子
    public static boolean getStructureGenerateValidity(){
        if(serverInstance!=null){
            StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(serverInstance);
            return serverState.isPickAxeCrafted;
        }else
            return false;

    }




    public static void setDay(int timeOfTheWorld){
        day = (int) (timeOfTheWorld / 24000L);
    }


    public static int getDay() {
        return day;
    }
}