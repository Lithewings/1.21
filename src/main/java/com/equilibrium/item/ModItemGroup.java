package com.equilibrium.item;

import com.equilibrium.MITEequilibrium;

import com.equilibrium.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroup {
    //自定义物品栏
    public static final ItemGroup modGroup = Registry.register(Registries.ITEM_GROUP, Identifier.of(MITEequilibrium.MOD_ID,"testgroup"),
            //注册名小写
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.testgroup"))
                    //itemgroup.testgroup是不加翻译前的物品栏名字
                    .icon(()->new ItemStack(ModItems.test)).entries((displayContext, entries) ->
                    //这里开始添加物品
                            {
                                entries.add(ModItems.test);
                                entries.add(Items.BOOK);//可以加原版物品
                                entries.add(ModBlocks.EXAMPLE_BLOCK);

                            }
                    ).build());

    //工具栏
    public static final ItemGroup modTools = Registry.register(Registries.ITEM_GROUP, Identifier.of(MITEequilibrium.MOD_ID,"toolsgroup"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.toolsgroup"))
                    .icon(()->new ItemStack(Tools.adamantium_axe)).entries((displayContext, entries) ->
                            {
                                entries.add(Tools.adamantium_axe);
                            }
                    ).build());

    //金属栏
    public static final ItemGroup modIngots = Registry.register(Registries.ITEM_GROUP, Identifier.of(MITEequilibrium.MOD_ID,"metalgroup"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.metalgroup"))
                    .icon(()->new ItemStack(Metal.adamantium)).entries((displayContext, entries) ->
                            {
                                entries.add(Metal.adamantium);
                                entries.add(Metal.copper);
                                entries.add(Metal.ancient_metal);
                                entries.add(Metal.gold);
                                entries.add(Metal.mithril);
                                entries.add(Metal.silver);

                                entries.add(Metal.adamantium_nugget);
                                entries.add(Metal.ancient_metal_nugget);
                                entries.add(Metal.copper_nugget);
                                entries.add(Metal.gold_nugget);
                                entries.add(Metal.silver_nugget);
                                entries.add(Metal.mithril_nugget);

                                entries.add(ModBlocks.SILVER_BLOCK);
                                entries.add(ModBlocks.COPPER_BLOCK);
                                entries.add(ModBlocks.ADAMANTIUM_BLOCK);
                                entries.add(ModBlocks.ANCIENT_METAL_BLOCK);
                                entries.add(ModBlocks.MITHRIL_BLOCK);
                                entries.add(ModBlocks.GOLD_BLOCK);

                                entries.add(ModBlocks.GOLD_ORE);
                                entries.add(ModBlocks.ADAMANTIUM_ORE);
                                entries.add(ModBlocks.COPPER_ORE);
                                entries.add(ModBlocks.MITHRIL_ORE);
                                entries.add(ModBlocks.SILVER_ORE);


                            }
                    ).build());






    public static void registerModItemGroup(){

    }
}
