package com.equilibrium.gen;

import com.equilibrium.block.ModBlocks;
import com.equilibrium.constant.ConstantString;
import com.equilibrium.item.Armors;
import com.equilibrium.item.Metal;
import com.equilibrium.item.Tools;
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

        translationBuilder.add(Metal.adamantium,"艾德曼锭");
        translationBuilder.add(Metal.adamantium_nugget,"艾德曼粒");
        translationBuilder.add(Metal.ancient_metal,"远古金属锭");
        translationBuilder.add(Metal.ADAMANTIUM_RAW,"艾德曼矿");
        translationBuilder.add(Metal.ancient_metal_nugget,"远古金属粒");
        translationBuilder.add(Metal.copper,"精铜锭");
        translationBuilder.add(Metal.copper_nugget,"铜粒");
        translationBuilder.add(Metal.FLINT,"燧石碎片");
        translationBuilder.add(Metal.gold,"纯金锭");
        translationBuilder.add(Metal.gold_nugget,"金粒");
        translationBuilder.add(Metal.mithril,"秘银");
        translationBuilder.add(Metal.MITHRIL_RAW,"秘银矿");
        translationBuilder.add(Metal.mithril_nugget,"秘银粒");
        translationBuilder.add(Metal.silver,"银锭");
        translationBuilder.add(Metal.silver_nugget,"银粒");
        translationBuilder.add(Metal.SILVER_RAW,"银矿");



        translationBuilder.add(Tools.ADAMANTIUM_AXE,"艾德曼斧");
        translationBuilder.add(Tools.ADMANTIUM_DAGGER,"艾德曼短剑");
        translationBuilder.add(Tools.ADAMANTIUM_HOE,"艾德曼锄");
        translationBuilder.add(Tools.ADAMANTIUM_PICKAXE,"艾德曼镐");
        translationBuilder.add(Tools.ADAMANTIUM_SHOVEL,"艾德曼铲");
        translationBuilder.add(Tools.ADMANTIUM_SWORD,"艾德曼剑");
        translationBuilder.add(Tools.SILVER_AXE,"银斧");
        translationBuilder.add(Tools.SILVER_SWORD,"银剑");
        translationBuilder.add(Tools.SILVER_SHOVEL,"银剑");
        translationBuilder.add(Tools.SILVER_DAGGER,"银短剑");
        translationBuilder.add(Tools.SILVER_HOE,"银锄");
        translationBuilder.add(Tools.SILVER_PICKAXE,"银镐");

        translationBuilder.add(Tools.COPPER_AXE,"铜斧");
        translationBuilder.add(Tools.COPPER_DAGGER,"铜短剑");
        translationBuilder.add(Tools.COPPER_HOE,"铜锄");
        translationBuilder.add(Tools.COPPER_PICKAXE,"铜镐");
        translationBuilder.add(Tools.COPPER_SHOVEL,"铜锹");
        translationBuilder.add(Tools.COPPER_SWORD,"铜剑");
        translationBuilder.add(Tools.FLINT_AXE,"燧石斧");
        translationBuilder.add(Tools.FLINT_HATCHET,"燧石手斧");
        translationBuilder.add(Tools.FLINT_KNIFE,"燧石小刀");
        translationBuilder.add(Tools.FLINT_SHOVEL,"燧石锹");
        translationBuilder.add(Tools.GOLD_AXE,"金斧");
        translationBuilder.add(Tools.GOLD_DAGGER,"金短剑");
        translationBuilder.add(Tools.GOLD_PICKAXE,"金镐");
        translationBuilder.add(Tools.GOLD_SWORD,"金剑");
        translationBuilder.add(Tools.GOLD_SHOVEL,"金锹");
        translationBuilder.add(Tools.IRON_AXE,"铁斧");
        translationBuilder.add(Tools.IRON_DAGGER,"铁短剑");
        translationBuilder.add(Tools.IRON_HOE,"铁锄");
        translationBuilder.add(Tools.IRON_SWORD,"铁剑");
        translationBuilder.add(Tools.IRON_PICKAXE, "铁镐");
        translationBuilder.add(Tools.IRON_SHOVEL,"铁锹");
        translationBuilder.add(Tools.MITHRIL_AXE,"秘银斧");
        translationBuilder.add(Tools.MITHRIL_DAGGER,"秘银短剑");
        translationBuilder.add(Tools.MITHRIL_HOE,"秘银锄");
        translationBuilder.add(Tools.MITHRIL_PICKAXE,"秘银镐");
        translationBuilder.add(Tools.MITHRIL_SHOVEL,"秘银锹");
        translationBuilder.add(Tools.MITHRIL_SWORD,"秘银剑");





        translationBuilder.add(ModBlocks.ADAMANTIUM_BLOCK,"艾德曼块");
        translationBuilder.add(ModBlocks.COPPER_BLOCK,"铜块");
        translationBuilder.add(ModBlocks.GOLD_BLOCK,"金块");
        translationBuilder.add(ModBlocks.MITHRIL_BLOCK,"秘银块");
        translationBuilder.add(ModBlocks.SILVER_BLOCK,"银块");
        translationBuilder.add(ModBlocks.ANCIENT_METAL_BLOCK,"远古金属块");


        translationBuilder.add(Armors.COPPER_HELMET,"铜头盔");
        translationBuilder.add(Armors.COPPER_CHEST_PLATE,"铜胸甲");
        translationBuilder.add(Armors.COPPER_LEGGINGS,"铜护腿");
        translationBuilder.add(Armors.COPPER_BOOTS,"铜靴");


    }
}
