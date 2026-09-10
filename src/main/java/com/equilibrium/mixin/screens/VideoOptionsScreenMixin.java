package com.equilibrium.mixin.screens;

import com.equilibrium.common_gamerules.GameRuleGetter;
import com.equilibrium.server_and_client.fog_weather_event.IsFogWeatherSuitableUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.equilibrium.OnServerInitialize.MOD_ID;
import static com.equilibrium.common_gamerules.GameRuleRegister.IS_FOG_WEATHER_NOW;

@Mixin(VideoOptionsScreen.class)
public abstract class VideoOptionsScreenMixin extends GameOptionsScreen {
    public VideoOptionsScreenMixin(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }



    @Inject(method = "addOptions", at = @At("RETURN"))
    private void disableViewDistanceOption(CallbackInfo ci) {
        SimpleOption<Integer> viewDistanceOption = this.gameOptions.getViewDistance();
        // 获取对应的 widget
        Widget widget = this.body.getWidgetFor(viewDistanceOption);
        if (widget instanceof ClickableWidget clickableWidget) {
            if(client!=null && client.world!=null){

                boolean isRightDimension = IsFogWeatherSuitableUtil.isValid(client.world);
                if(isRightDimension && GameRuleGetter.booleanGameRuleGetterFromClient(IS_FOG_WEATHER_NOW)==true)
                    clickableWidget.active = false;
            }

        }
    }
}
