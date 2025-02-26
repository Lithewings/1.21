package com.equilibrium.mixin.render;

import com.equilibrium.util.OnServerInitializeMethod;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.tab.GridScreenTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

@Mixin(CreateWorldScreen.WorldTab.class)
public abstract class CreativeWorldScreenWorldTabMixin  {


//    @Shadow
//    private static final Text AMPLIFIED_GENERATOR_INFO_TEXT = Text.translatable("generator.minecraft.amplified.info");

    @ModifyArg(method = "<init>",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/world/WorldScreenOptionGrid$Builder;add(Lnet/minecraft/text/Text;Ljava/util/function/BooleanSupplier;Ljava/util/function/Consumer;)Lnet/minecraft/client/gui/screen/world/WorldScreenOptionGrid$OptionBuilder;",ordinal = 1),index = 1)
    public BooleanSupplier setValue(BooleanSupplier getter){
        return OnServerInitializeMethod::alwaysFalse;
    }

//    @ModifyArg(method = "<init>",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/world/WorldScreenOptionGrid$Builder;add(Lnet/minecraft/text/Text;Ljava/util/function/BooleanSupplier;Ljava/util/function/Consumer;)Lnet/minecraft/client/gui/screen/world/WorldScreenOptionGrid$OptionBuilder;",ordinal = 1),index = 2)
//    public Consumer<Boolean> setValue2(Consumer<Boolean> setter){
//        return OnServerInitializeMethod::doNothing;
//    }




//    @ModifyVariable(method = "<init>",at = @At(value = "INVOKE", target = ""))

//    @ModifyVariable(method = "<init>",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/world/WorldCreator;addListener(Ljava/util/function/Consumer;)V"))
//    public Consumer<WorldCreator> worldType(Consumer<WorldCreator> listener){
//
//        listener.accept(creator -> {
//            WorldCreator.WorldType worldType = creator.getWorldType();
//            cyclingButtonWidget.setValue(worldType);
//            if (worldType.isAmplified()) {
//                cyclingButtonWidget.setTooltip(Tooltip.of(AMPLIFIED_GENERATOR_INFO_TEXT));
//            } else {
//                cyclingButtonWidget.setTooltip(null);
//            }
//            cyclingButtonWidget.active = CreateWorldScreen.this.worldCreator.getWorldType().preset() != null;})
//        ;
//        return listener;
//    }




}