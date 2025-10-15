package com.equilibrium.mixin.render;

import com.equilibrium.util.BooleanStorageUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.WorldCreator;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;

import java.nio.file.Path;

@Mixin(CreateWorldScreen.GameTab.class)
public class CreativeWorldScreenGameTabMixin {


    @Unique
    private static final Text GAME_MODE_TEXT = Text.translatable("selectWorld.gameMode");
    @Unique
    private static final Text ALLOW_COMMANDS_TEXT = Text.translatable("selectWorld.allowCommands.new");
    @Unique
    private static final Text ALLOW_COMMANDS_INFO_TEXT = Text.translatable("selectWorld.allowCommands.info");



    @Redirect(
            method = "<init>", // 目标方法是 GameTab 的构造函数
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/Difficulty;values()[Lnet/minecraft/world/Difficulty;"
            )
    )
    private Difficulty[] redirectDifficultyValues() {
        // 返回自定义的 Difficulty 数组
        return new Difficulty[] {
                Difficulty.HARD,
                Difficulty.NORMAL,
                Difficulty.EASY
        };
    }
    @Unique
    private final String fileName = "Finish The Game Once.dat";
    @Unique
    private final Path configPath = FabricLoader.getInstance().getConfigDir().normalize().resolve(fileName);



    @ModifyArg(method = "<init>",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/CyclingButtonWidget$Builder;values([Ljava/lang/Object;)Lnet/minecraft/client/gui/widget/CyclingButtonWidget$Builder;",ordinal = 0))
    public Object[] modifyWorldCreatorMode(Object[] originalValues) {
        // 替换为自定义的 WorldCreator.Mode 数组
        if(BooleanStorageUtil.load(configPath.toString(), false)) {
            return new Object[] {
                    WorldCreator.Mode.SURVIVAL,
                    WorldCreator.Mode.HARDCORE,
                    WorldCreator.Mode.CREATIVE
            };
        }
        else
            return new Object[] {
                    WorldCreator.Mode.SURVIVAL,

            };
    }
//
//        return (T) CyclingButtonWidget.<WorldCreator.Mode>builder(value -> value.name)
//            .values(WorldCreator.Mode.SURVIVAL, WorldCreator.Mode.HARDCORE)
//                .build(0, 0, 210, 20, GAME_MODE_TEXT);
//    @ModifyArg(method = "<init>",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/GridWidget$Adder;add(Lnet/minecraft/client/gui/widget/Widget;Lnet/minecraft/client/gui/widget/Positioner;)Lnet/minecraft/client/gui/widget/Widget;",ordinal = 2),index = 0)
//    public <T extends Widget> T difficulty (T widget){
//        return (T) CyclingButtonWidget.builder(Difficulty::getTranslatableName)
//                .tooltip()
//                .build(0, 0, 210, 20, Text.translatable("options.difficulty"));
//    }






}



