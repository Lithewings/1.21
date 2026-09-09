package com.equilibrium.server_and_client.client.fog_weather_event;

import com.equilibrium.common_gamerules.GameRuleGetter;
import com.equilibrium.common_gamerules.GameRuleUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import static com.equilibrium.OnServerInitialize.MOD_ID;
import static com.equilibrium.common_gamerules.GameRuleRegister.IS_FOG_WEATHER_NOW;
import static com.equilibrium.server_and_client.client.fog_weather_event.C2SUpdateFogWeather.sendToServerFogWeather;
import static com.equilibrium.server_and_client.client.fog_weather_event.ChangeViewDistanceFromGameOption.changeViewDistance;
import static com.equilibrium.server_and_client.client.fog_weather_event.ChangeViewDistanceFromGameOption.getViewDistance;


/**
 * 决策器：判断维度并依据概率决定"起雾 / 放晴"。
 *
 * 注意：因为 Mediator 每天 new 一个新实例，一段"雾天"可能跨多天持续，
 * 所以"当前是否起雾 / 雾前的原始距离"这两条 Memento 基线状态必须做成
 * static，跨日持久，而不能存进每次重建的实例里。
 */
public class FogWeatherHandler {

    private final ClientWorld clientWorld;

    public static final int VIEW_DISTANCE_MIN = 4;
    public static final int VIEW_DISTANCE_MAX = 8;
    /** 正常晴天(无雾)的渲染距离兜底值(防御性编程) */
    public static final int VIEW_DISTANCE_NORMAL = 16;
    public static final float FOG_WEATHER_POSSIBILITY = 0F;

    public static boolean APPLY_ON_OVERWORLD = true;
    public static boolean APPLY_ON_UNDERWORLD = true;
    public static boolean APPLY_ON_NETHER = false;
    public static boolean APPLY_ON_END = false;

    /** 是否正处于一段雾天中(跨日持久)。
     *  初始化由游戏规则来读取
     **/
    private static boolean fogActive = false;
    /** 本段雾天开始前的原始渲染距离，用于放晴时还原(跨日持久)。 */
    private static int fogBaseViewDistance = -1;

    public static final RegistryKey<World> OVERWORLD = RegistryKey.of(RegistryKeys.WORLD, Identifier.ofVanilla("overworld"));
    public static final RegistryKey<World> NETHER = RegistryKey.of(RegistryKeys.WORLD, Identifier.ofVanilla("the_nether"));
    public static final RegistryKey<World> END = RegistryKey.of(RegistryKeys.WORLD, Identifier.ofVanilla("the_end"));
    public static final RegistryKey<World> UNDERWORLD = RegistryKey.of(RegistryKeys.WORLD, Identifier.of(MOD_ID, "underworld"));

    public FogWeatherHandler(ClientWorld clientWorld) {
        this.clientWorld = clientWorld;
        fogActive = GameRuleGetter.booleanGameRuleGetterFromClient(clientWorld,IS_FOG_WEATHER_NOW);
    }

    private int getNextRandomDistance() {
        return clientWorld.getRandom().nextBetween(VIEW_DISTANCE_MIN, VIEW_DISTANCE_MAX);
    }

    private boolean isWorld(RegistryKey<World> target) {
        // RegistryKey.of(...) 每次新建对象，必须用 equals() 做值比较，不能用 ==。
        return target.equals(clientWorld.getRegistryKey());
    }

    public boolean isOverWorld() {
        return isWorld(OVERWORLD);
    }

    public boolean isNether() {
        return isWorld(NETHER);
    }

    public boolean isEnd() {
        return isWorld(END);
    }

    public boolean isUnderWorld() {
        return isWorld(UNDERWORLD);
    }

    public boolean isValid() {
        return isOverWorld() && APPLY_ON_OVERWORLD
                || isNether() && APPLY_ON_NETHER
                || isEnd() && APPLY_ON_END
                || isUnderWorld() && APPLY_ON_UNDERWORLD;
    }

    public void applyFogWeather() {
        if (!isValid()) {
            return;                     // 不在适用维度：什么都不改，避免误上报
        }

        // 每天只掷一次骰，决定今天是否起雾。
        boolean rollFog = clientWorld.getRandom().nextFloat() < FOG_WEATHER_POSSIBILITY;

        if (rollFog) {
            if (!fogActive) {           // 本段雾天第一次出现：锁定"雾前原始距离"
                fogBaseViewDistance = getViewDistance();
                fogActive = true;
            }
            changeViewDistance(getNextRandomDistance());
            sendToServerFogWeather(true);
        } else {
            //代码执行到这里,就应该确保放晴
            if (fogActive) {            // 由雾放晴：还原到本段雾天开始前的距离
                int restore = fogBaseViewDistance > 0 ? fogBaseViewDistance : VIEW_DISTANCE_NORMAL;
                changeViewDistance(restore);
                fogActive = false;
                fogBaseViewDistance = -1;
                sendToServerFogWeather(false);
            }
            // 由晴天过渡到放晴,什么都不做

            //我此前测试到如果玩家在雾天退出游戏
            //那么即使第二天通过计算,应该重新变晴，由于fogActive由于游戏退出导致恢复到初始化值false导致无法执行分支代码
            //也就是无法放晴
            //只有再起一次雾而且不退出才能变晴


            //或者意外地,本来应该由雾天变成晴天,但是fogActive变量没有正确初始化,补发同步一次

            //情况为fogActive==false,去检查游戏规则的fog有没有起雾,如果游戏规则是起雾的,补发同步一次
            //代码执行到这里,就应该确保放晴,让游戏规则起的雾散掉,让玩家可以正常调节按钮滑块

            if(fogActive==false && GameRuleGetter.booleanGameRuleGetterFromClient(clientWorld,IS_FOG_WEATHER_NOW)==true)
                sendToServerFogWeather(false);

        }
    }

}
