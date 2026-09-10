package com.equilibrium.server_and_client.fog_weather_event;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;


public class FogWeatherMediator {


    private static boolean samplingFrequencyEnabled(long time) {
        return time % 80L == 0;
    }
    //第一天不生效
    private static boolean isValidCircumstance(long time){
        return samplingFrequencyEnabled(time) && time > 24000L;
    }

    public static void synchronizeFogWeatherIfAvailable(ClientWorld clientWorld){
        if(isValidCircumstance(clientWorld.getTimeOfDay())){
            FogWeatherHandler fogWeatherHandler = new FogWeatherHandler(clientWorld);
            fogWeatherHandler.situationSwitch();

        }
    }

}
