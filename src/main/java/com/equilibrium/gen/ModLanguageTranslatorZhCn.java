package com.equilibrium.gen;

import com.equilibrium.block.ModBlocks;
import com.equilibrium.constant.ConstantString;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModLanguageTranslatorZhCn extends FabricLanguageProvider {


    public ModLanguageTranslatorZhCn(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput,"zh_cn", registryLookup);
    }

    /**
     * Implement this method to register languages.
     *
     * <p>Call {@link TranslationBuilder#add(String, String)} to add a translation.
     *
     * @param registryLookup
     * @param translationBuilder
     */
    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(ConstantString.TRANSPORT_TARGET1,"该传送门会将你传送至主世界");
        translationBuilder.add(ConstantString.TRANSPORT_TARGET2,"该传送门会将你传送至地下世界");
        translationBuilder.add(ConstantString.TRANSPORT_TARGET3,"该传送门会将你传送至下界");



    }
}
