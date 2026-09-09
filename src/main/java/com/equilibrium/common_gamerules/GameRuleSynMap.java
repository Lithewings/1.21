package com.equilibrium.common_gamerules;

import net.minecraft.world.GameRules;

import java.util.Map;

import static com.equilibrium.common_gamerules.GameRuleRegister.FOG_WEATHER;
import static com.equilibrium.common_gamerules.GameRuleRegister.IS_FOG_WEATHER_NOW;

public class GameRuleSynMap {
    //id字典,用于将服务端的规则同步到客户端上去
    public static Map<String, GameRules.Key<GameRules.BooleanRule>> GET_ALL_RULES = Map.ofEntries(
            Map.entry(FOG_WEATHER, IS_FOG_WEATHER_NOW)
    );
}
