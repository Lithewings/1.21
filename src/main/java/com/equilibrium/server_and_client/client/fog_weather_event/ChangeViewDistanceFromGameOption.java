package com.equilibrium.server_and_client.client.fog_weather_event;


import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;

/**
 * 渲染距离(GameOptions.getViewDistance)的统一读写入口。
 * Handler 只做决策，具体访问游戏选项的动作都收敛在这里，避免读写路径被复制到多处。
 */
public class ChangeViewDistanceFromGameOption {
    private static MinecraftClient getClientInstance() {
        return MinecraftClient.getInstance();
    }
    private static GameOptions getGameOption() {
        return getClientInstance().options;
    }

    /** 当前渲染距离 */
    public static int getViewDistance() {
        return getGameOption().getViewDistance().getValue();
    }

    /** 设置渲染距离 */
    public static void changeViewDistance(int distance) {
        getGameOption().getViewDistance().setValue(distance);
    }
}
