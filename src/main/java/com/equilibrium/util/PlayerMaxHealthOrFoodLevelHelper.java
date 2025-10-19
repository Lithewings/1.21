package com.equilibrium.util;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.Collections;

public class PlayerMaxHealthOrFoodLevelHelper {


    //返回玩家这里的经验最大值
    public static int getMaxHealthOrFoodLevel(PlayerEntity player) {
        return player.experienceLevel >= 35 ? 20 : 6 + (int)(( player.experienceLevel/ 5) * 2);
    }

}
