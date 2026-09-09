package com.equilibrium.server_and_client.client.fog_weather_event;

import com.equilibrium.util.CommandExecutor;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import static com.equilibrium.OnServerInitialize.MOD_ID;
import static com.equilibrium.common_gamerules.GameRuleRegister.FOG_WEATHER;

public class C2SUpdateFogWeather {

    public static final Identifier UPDATE_FOG_WEATHER = Identifier.of(MOD_ID, "update_fog_weather");

    /**
     * 在服务端注册数据包类型并添加接收处理器
     */
    public static void registerOnServer() {
        PayloadTypeRegistry.playC2S().register(UpdateGameRulePayload.ID, UpdateGameRulePayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(UpdateGameRulePayload.ID,
                (payload, context) -> {
                    context.server().execute(() -> {
                        onServerReceiveTrigger(context.server(), payload.isFogNow, context.player());
                    });
                });
    }

    /**
     * 服务端收到空触发器后执行的操作
     * 此处可根据需要实现具体的世界更新逻辑（如更新游戏规则、同步状态等）
     */
    private static void onServerReceiveTrigger(MinecraftServer server ,boolean isFogNow , @Nullable ServerPlayerEntity player) {
        String fogValue= isFogNow ?"true":"false";
        String command = "/gamerule " + FOG_WEATHER +" "+fogValue;
        CommandExecutor.executeCommandWithSlash(server,command);

        if(player!=null)
            player.sendMessage(Text.of("Received trigger from client for game rule update."));

    }

    /**
     * 客户端调用此方法发送更新请求
     */
    public static void sendToServerFogWeather(boolean isFogNow) {
        ClientPlayNetworking.send(new UpdateGameRulePayload(isFogNow));
    }

    /**
     * 空载荷：不包含任何数据
     */
    public static class UpdateGameRulePayload implements CustomPayload {
        public static final CustomPayload.Id<UpdateGameRulePayload> ID =
                new CustomPayload.Id<>(UPDATE_FOG_WEATHER);

        public final boolean isFogNow;

        public UpdateGameRulePayload(boolean isFogNow) {
            this.isFogNow = isFogNow;
        }


        public static final PacketCodec<PacketByteBuf, UpdateGameRulePayload> CODEC =
                PacketCodec.of(
                        // 编码器：写入布尔值
                        (payload, buf) -> buf.writeBoolean(payload.isFogNow),
                        // 解码器：读取布尔值并构造新实例
                        buf -> new UpdateGameRulePayload(buf.readBoolean())
                );


        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
}