package com.equilibrium.item;

import com.equilibrium.MITEequilibrium;
import com.equilibrium.block.ModBlocksTest;
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
                                entries.add(ModBlocksTest.EXAMPLE_BLOCK);

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

    //锭栏
    public static final ItemGroup modIngots = Registry.register(Registries.ITEM_GROUP, Identifier.of(MITEequilibrium.MOD_ID,"ingotsgroup"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.ingotsgroup"))
                    .icon(()->new ItemStack(Ingots.adamantium)).entries((displayContext, entries) ->
                            {
                                entries.add(Ingots.adamantium);
                                entries.add(Ingots.copper);
                                entries.add(Ingots.ancient_metal);
                                entries.add(Ingots.gold);
                                entries.add(Ingots.mithril);
                                entries.add(Ingots.silver);
                            }
                    ).build());





    public static void registerModItemGroup(){

    }
}
