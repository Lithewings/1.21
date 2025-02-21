package com.equilibrium.mixin.render;

import com.equilibrium.MITEequilibrium;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.WorldCreator;
import net.minecraft.client.gui.tab.TabManager;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.text.Text;
import net.minecraft.util.path.SymlinkFinder;
import net.minecraft.world.Difficulty;
import net.minecraft.world.gen.WorldPresets;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.swing.plaf.ToolTipUI;
import java.nio.file.Path;


@Mixin(CreateWorldScreen.class)

public abstract class CreativeWorldScreenMixin extends Screen {
    @Shadow
    @Final
    WorldCreator worldCreator;
    @Shadow
    private final TabManager tabManager = new TabManager(this::addDrawableChild, child -> this.remove(child));
    @Shadow
    private boolean recreated;
    @Shadow
    @Final
    private SymlinkFinder symlinkFinder;
    @Shadow
    @Final
    private Screen parent;
    @Shadow
    private Path dataPackTempDir;
    @Shadow
    private ResourcePackManager packManager;
    @Shadow
    private TabNavigationWidget tabNavigation;
    @Shadow
    private static final Logger LOGGER = LogUtils.getLogger();
    @Shadow
    private final ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);

    @Shadow
    public abstract void render(DrawContext context, int mouseX, int mouseY, float delta);

    @Shadow public abstract WorldCreator getWorldCreator();

    protected CreativeWorldScreenMixin(Text title) {
        super(title);
    }



    @Inject(method = "initTabNavigation", at = @At(value = "TAIL"))
    //按钮捕获
    private void modifyGameTab(CallbackInfo ci) {

        for (int i = 0; i < 2; i++) {
            int page = i;
            this.tabNavigation.tabs.get(i).forEachChild(element -> {
                if (element instanceof CyclingButtonWidget<?>) {

                    //名字见 TabNavigationWidget类往后翻,会有按钮的文本
                    //这里只是显示而已,不会真正改变值
                    MITEequilibrium.LOGGER.info(element.getMessage().toString());
                    if (element.getMessage().toString().contains("difficulty"))
                        ((CyclingButtonWidget<Difficulty>) element).setValue(Difficulty.HARD);
                    if (element.getMessage().toString().contains("gameMode"))
                        ((CyclingButtonWidget<WorldCreator.Mode>) element).setValue(WorldCreator.Mode.SURVIVAL);
                    if (element.getMessage().toString().contains("Command"))
                        ((CyclingButtonWidget<Boolean>) element).setValue(false);
                    if (element.getMessage().toString().contains("mapType"))
                        ((CyclingButtonWidget<WorldCreator.WorldType>) element).setValue(worldCreator.getNormalWorldTypes().getFirst());
                    element.active = false;

                } else if(element instanceof TextFieldWidget && page ==0) {
                    //世界名字输入不可用

                    ((TextFieldWidget) element).setText("World");
                    ((TextFieldWidget) element).setTooltip(Tooltip.of(Text.of("如果你想要修改世界名字,请先创建世界后再选择世界编辑")));
                    element.active=false;
                }
                else if(element instanceof TextFieldWidget && page ==1){
                    //种子输入不可用
                    ((TextFieldWidget) element).setTooltip(Tooltip.of(Text.of("目前阶段还无法指定世界种子,让随机数来决定这些")));
                    element.active=false;
                }
                else
                    element.active=false;

            });
        }







    }
}


//        this.layout.forEachElement(element->{
//                if(element instanceof CyclingButtonWidget<Difficulty>)
//                        ((CyclingButtonWidget<?>) element).visible=false;

//                element1.forEachChild(element2->{
//                    if(element2 instanceof CyclingButtonWidget<?>)
//                        element2.visible=false;








//                String string = element.
//                MITEequilibrium.LOGGER.info(string);
//                if (string.contains("allowCommand")){
//                    element.visible=false;
//                }
//
//                if (string.contains("gamemode.creative")){
//                    element.visible=false;
//                }
//        });
//            this.getWorldCreator().setCheatsEnabled(false);
//            this.getWorldCreator().setDifficulty(Difficulty.byId(3));

//        this.tabNavigation.tabButtons.set(1,this.tabNavigation.tabButtons.get(2));



//        adder.getGridWidget().forEachElement(element -> {
//            if (element instanceof CyclingButtonWidget<?> buttonWidget) {
//                // 判断按钮是否是“允许作弊”按钮
//                if (buttonWidget.getMessage().equals(CreateWorldScreen.ALLOW_COMMANDS_TEXT)) {
//                    buttonWidget.active = false;  // 禁用按钮
//                    buttonWidget.setMessage(Text.literal("作弊不可用"));  // 修改显示文本
//                    System.out.println("已找到并禁用'允许作弊'按钮");
//                }
//            }
//        });
//        this.tabNavigation = TabNavigationWidget.builder(this.tabManager, this.width)
//                .tabs(adder, new WorldTab(), new CreateWorldScreen.MoreTab())
//                .build();











//        @Shadow
//        private static final Text GAME_TAB_TITLE_TEXT = Text.translatable("createWorld.tab.game.title");
//        @Shadow
//        private static final Text ALLOW_COMMANDS_TEXT = Text.translatable("selectWorld.allowCommands.new");

//
//    @ModifyVariable(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/GridWidget$Adder;add(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;"), ordinal = 2)
//    public CyclingButtonWidget<Boolean> CyclingButtonWidget(CyclingButtonWidget<Boolean> cyclingButtonWidget) {
//
//        CyclingButtonWidget<Boolean> cyclingButtonWidget1 = CyclingButtonWidget.onOffBuilder()
//                        .tooltip(value -> Tooltip.of(CreateWorldScreen.ALLOW_COMMANDS_INFO_TEXT))
//                        .initially(false)
//                        .build(0, 0, 210, 20, ALLOW_COMMANDS_TEXT, (button, value) ->CreateWorldScreen.worldCreator.setCheatsEnabled(value)
//        );
//
//        return cyclingButtonWidget1;
//    }
//
//    @ModifyVariable(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/world/WorldCreator;addListener(Ljava/util/function/Consumer;)V"), ordinal = 2)
//    public CyclingButtonWidget<Boolean> CyclingButtonWidget2(CyclingButtonWidget<Boolean> cyclingButtonWidget) {
//        cyclingButtonWidget.active = false;
//        return cyclingButtonWidget;
//    }
//
//












//    @Mixin(CreateWorldScreen.GameTab.class)
//    static class GameTab extends GridScreenTab{
//

//
//

        //不允许作弊选项打开





