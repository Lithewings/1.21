package com.equilibrium.util;

public class WorldMoonPhasesSelector {
    private static String moonType=null;


    static boolean fullMoon;
    static boolean newMoon;
    static boolean harvestMoon;
    static boolean bloodMoon;
    static boolean blueMoon;
    static boolean normalMoon;
    static boolean haloMoon;

    public static void setMoonType(long time) {

        int day= getDay(time);
        //满月变种:黄月、血月、蓝月
        //黄月变种:幻月
        //进行判断时,如果是满月或者黄月,需要继续判断,直到达到最深层


        if(day % 8 == 0){
            fullMoon = true;
        }else{
            fullMoon=false;
        }
        if(day % 5 ==0){
            newMoon=true;
        }else{
            newMoon=false;
        }
        if ( fullMoon && (day+8)%32==0 ){
            harvestMoon=true;
        }else{
            harvestMoon =false;
        }
        if(fullMoon && day%32==0){
            bloodMoon =true;
        }else{
            bloodMoon =false;
        }
        if(bloodMoon && day%128==0){
            blueMoon =true;
        }else{
            blueMoon =false;
        }

        if(fullMoon && (day+8)%128 ==0){
            haloMoon=true;
        }else{
            haloMoon=false;
        }


        if (fullMoon) {
            moonType="fullMoon";
            //默认都是满月,如果触发了变种,则继续修改月种
            if(harvestMoon){
                //默认都是黄月,如果触发变种,则继续修改
                moonType ="harvestMoon";
                if(haloMoon){
                    moonType="haloMoon";
                }
            }
            if(bloodMoon){
                moonType ="bloodMoon";
            }
            if (blueMoon){
                moonType="blueMoon";
            }

        } else if (newMoon) {
            moonType="newMoon";
        } else {
            normalMoon=true;
            moonType="normalMoon";
        }

//        LOGGER.info("The day is : "+day+" "+"The Moon is : "+ moonType);

    }


    public static String getMoonType() {
        return moonType;
    }

    public static int getDay(float time){
        int day =(int)(time / 24000L) % 128;
        return day;
    }







}
