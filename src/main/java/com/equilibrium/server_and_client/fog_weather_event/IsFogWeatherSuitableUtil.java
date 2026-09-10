package com.equilibrium.server_and_client.fog_weather_event;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import static com.equilibrium.OnServerInitialize.MOD_ID;

public class IsFogWeatherSuitableUtil {

    public static final RegistryKey<World> OVERWORLD = RegistryKey.of(RegistryKeys.WORLD, Identifier.ofVanilla("overworld"));
    public static final RegistryKey<World> NETHER = RegistryKey.of(RegistryKeys.WORLD, Identifier.ofVanilla("the_nether"));
    public static final RegistryKey<World> END = RegistryKey.of(RegistryKeys.WORLD, Identifier.ofVanilla("the_end"));
    public static final RegistryKey<World> UNDERWORLD = RegistryKey.of(RegistryKeys.WORLD, Identifier.of(MOD_ID, "underworld"));

    public static boolean APPLY_ON_OVERWORLD = true;
    public static boolean APPLY_ON_UNDERWORLD = true;
    public static boolean APPLY_ON_NETHER = false;
    public static boolean APPLY_ON_END = false;

    private static boolean isWorld(RegistryKey<World> target , ClientWorld clientWorld) {
        // RegistryKey.of(...) 每次新建对象，必须用 equals() 做值比较，不能用 ==。
        return target.equals(clientWorld.getRegistryKey());
    }

    private static boolean isOverWorld(ClientWorld clientWorld) {
        return isWorld(OVERWORLD,clientWorld);
    }

    private static boolean isNether(ClientWorld clientWorld) {
        return isWorld(NETHER,clientWorld);
    }

    private static boolean isEnd(ClientWorld clientWorld) {
        return isWorld(END,clientWorld);
    }

    private static boolean isUnderWorld(ClientWorld clientWorld) {
        return isWorld(UNDERWORLD,clientWorld);
    }

    //可以对外暴露的方法
    public static boolean isValid(ClientWorld clientWorld) {
        return (isOverWorld(clientWorld) && APPLY_ON_OVERWORLD)
                || (isNether(clientWorld) && APPLY_ON_NETHER)
                || (isEnd(clientWorld) && APPLY_ON_END)
                || (isUnderWorld(clientWorld) && APPLY_ON_UNDERWORLD);
    }
}
