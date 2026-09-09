package com.equilibrium.common_gamerules;

import com.equilibrium.OnServerInitialize;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import org.jetbrains.annotations.NotNull;

public class GameRuleGetter {
    public static boolean booleanGameRuleGetterFromServer(MinecraftServer server, GameRules.Key<GameRules.BooleanRule> key) {
        GameRules.BooleanRule rule = server.getGameRules().get(key);
        return rule.get();
    }

    public static boolean booleanGameRuleGetterFromClient(GameRules.Key<GameRules.BooleanRule> key) {
        if (MinecraftClient.getInstance().world instanceof ClientWorld clientWorld) {
            GameRules.BooleanRule rule = clientWorld.getGameRules().get(key);
            if (rule == null)
                return false;
            return rule.get();
        }
        return false;
    }
    public static boolean booleanGameRuleGetterFromClient(@NotNull ClientWorld clientWorld, GameRules.Key<GameRules.BooleanRule> key) {
        GameRules.BooleanRule rule = clientWorld.getGameRules().get(key);
        if (rule == null)
            return false;
        return rule.get();

    }


}
