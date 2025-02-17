package com.equilibrium.util;


import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.Collections;

public class PlayerMaxHealthOrFoodLevelHelper {

    //自动从记录的服务器实例里获取玩家,返回玩家这里的经验最大值
    public static int getMaxHealthOrFoodLevel() {
        ArrayList<Integer> playerExperience = new ArrayList<>();
        for(PlayerEntity player :ServerInfoRecorder.getServerInstance().getPlayerManager().getPlayerList()){
            playerExperience.add(player.experienceLevel);
        }
        int maxExperienceLevel = playerExperience.isEmpty()? 0 :Collections.max(playerExperience);
        return maxExperienceLevel >= 35 ? 20 : 6 + (int)(( maxExperienceLevel/ 5) * 2);
    }


}
