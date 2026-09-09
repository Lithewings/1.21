package com.equilibrium.server_and_client.client.fog_weather_event;

import net.minecraft.client.world.ClientWorld;


public class FogWeatherMediator {


    private static boolean isNewDay(long time) {
        return time % 24000L == 0;
    }

    private static boolean isValidCircumstance(long time){
        return isNewDay(time) && time > 24000L;
    }


    public static void executeFogWeather(ClientWorld clientWorld){
        if(isValidCircumstance(clientWorld.getTimeOfDay())){
            FogWeatherHandler fogWeatherHandler = new FogWeatherHandler(clientWorld);
            fogWeatherHandler.applyFogWeather();
        }
    }

}
