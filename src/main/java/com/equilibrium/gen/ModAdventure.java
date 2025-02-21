package com.equilibrium.gen;

import com.equilibrium.item.Armors;
import com.equilibrium.item.Metal;
import com.equilibrium.item.Tools;
import net.minecraft.advancement.*;
import net.minecraft.advancement.criterion.ChangedDimensionCriterion;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.UsedEnderEyeCriterion;
import net.minecraft.item.Items;
import net.minecraft.predicate.NumberRange;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.function.Consumer;

import static com.equilibrium.MITEequilibrium.MOD_ID;


public class ModAdventure  implements Consumer<Consumer<AdvancementEntry>>{



    @Override
    public void accept(Consumer<AdvancementEntry> exporter) {
        AdvancementEntry rootAdventure = Advancement.Builder.create()
                .display(
                        // 显示的图标
                        Items.SPYGLASS,
                        Text.literal("MITE-宁静"),
                        Text.literal("纪念品\n源自出生点的箱子"),
                        Identifier.ofVanilla("textures/gui/advancements/backgrounds/adventure.png"),
                        AdvancementFrame.TASK,
                        /*布尔值 当你达成了进度的时候，是否在屏幕右上角显示公告*/
                        true,
                        /*布尔值 当你达成了进度的时候，是否在聊天框发送消息*/
                        false,
                        /*布尔值 该进度是否可以在未达成时在进度选项卡中被看到*/
                        true

                )
                .criterion("got_spyglass", InventoryChangedCriterion.Conditions.items(Items.SPYGLASS))
                .build(exporter, "root");

        AdvancementEntry getFlint = Advancement.Builder.create().parent(rootAdventure)
                .display(
                        Items.GRAVEL,
                        Text.literal("燧石寻找者"),
                        Text.literal("挖掘沙砾,收集燧石碎片或者完整的燧石"),
                        null, // 子进度不需要设置背景
                        AdvancementFrame.TASK,
                        true,
                        false,
                        false
                )
                .criterion("get_flint_small", InventoryChangedCriterion.Conditions.items(Metal.FLINT))
                .criterion("get_flint", InventoryChangedCriterion.Conditions.items(Items.FLINT))
                .criteriaMerger(AdvancementRequirements.CriterionMerger.OR)
                .build(exporter, "get_flint");

        AdvancementEntry getHatchet = Advancement.Builder.create().parent(getFlint)
                .display(
                        Tools.FLINT_HATCHET,
                        Text.literal("生存达人"),
                        Text.literal("设法获取或合成第一块燧石,制作燧石手斧以砍伐树木"),
                        null, // 子进度不需要设置背景
                        AdvancementFrame.TASK,
                        true,
                        false,
                        false
                )
                .criterion("get_hatchet", InventoryChangedCriterion.Conditions.items(Tools.FLINT_HATCHET))
                .build(exporter, "get_hatchet");


        AdvancementEntry getFlintCraftTable = Advancement.Builder.create().parent(getHatchet)
                .display(
                        Tools.FLINT_HATCHET,
                        Text.literal("这是,工作台?"),
                        Text.literal("继续收集燧石,合成燧石小刀以制作燧石工作台"),
                        null, // 子进度不需要设置背景
                        AdvancementFrame.TASK,
                        true,
                        false,
                        false
                )
                .criterion("get_flint_craft_table", InventoryChangedCriterion.Conditions.items(Tools.FLINT_HATCHET))
                .build(exporter, "get_flint_craft_table");




        AdvancementEntry getSilver = Advancement.Builder.create().parent(getFlint)
                .display(
                        Metal.silver_nugget,
                        Text.literal("贵金属"),
                        Text.literal("不断挖掘沙砾\n直到收集9颗银粒"),
                        null, // 子进度不需要设置背景
                        AdvancementFrame.TASK,
                        true,
                        false,
                        false
                )
                .criterion("get_silver", InventoryChangedCriterion.Conditions.items(Metal.silver_nugget))
                .build(exporter, "get_silver");


        AdvancementEntry getCopper = Advancement.Builder.create().parent(getFlint)
                .display(
                        Metal.copper_nugget,
                        Text.literal("也许是抽卡游戏?"),
                        Text.literal("不断挖掘沙砾\n直到收集27颗铜粒"),
                        null, // 子进度不需要设置背景
                        AdvancementFrame.TASK,
                        true,
                        false,
                        false
                )
                .criterion("get_copper", InventoryChangedCriterion.Conditions.items(Metal.copper_nugget))
                .build(exporter, "get_copper");

        AdvancementEntry getMetalPickAxe = Advancement.Builder.create().parent(getCopper)
                .display(
                        Tools.COPPER_PICKAXE,
                        Text.literal("下矿时间到"),
                        Text.literal("制作银工作台\n制作你的第一把铜镐\n解锁村庄生成的条件之一"),
                        null, // 子进度不需要设置背景
                        AdvancementFrame.TASK,
                        true,
                        false,
                        false
                )
                .criterion("get_metal_pickaxe", InventoryChangedCriterion.Conditions.items(Metal.copper_nugget))
                .build(exporter, "get_metal_pickaxe");



        AdvancementEntry getIronPickAxe = Advancement.Builder.create().parent(getMetalPickAxe)
                .display(
                        Tools.IRON_PICKAXE,
                        Text.literal("黑曜石采集者"),
                        Text.literal("制作铁镐,然后用它收集一些黑曜石"),
                        null, // 子进度不需要设置背景
                        AdvancementFrame.TASK,
                        true,
                        false,
                        false
                )
                .criterion("get_iron_pickaxe", InventoryChangedCriterion.Conditions.items(Tools.IRON_PICKAXE))
                .build(exporter, "get_iron_pickaxe");







        AdvancementEntry getFullCopperArmor = Advancement.Builder.create().parent(getMetalPickAxe)
                .display(
                        Armors.COPPER_CHEST_PLATE,
                        Text.literal("全副武装 I"),
                        Text.literal("穿上全套铜护甲\n当你至少拥有10点护甲值时,获得永久性的抗性提升 I\n获得100XP"),
                        null, // 子进度不需要设置背景
                        AdvancementFrame.TASK,
                        true,
                        false,
                        false
                )
                .criterion("get_full_copper_armor1", InventoryChangedCriterion.Conditions.items(Armors.COPPER_BOOTS))
                .criterion("get_full_copper_armor2", InventoryChangedCriterion.Conditions.items(Armors.COPPER_HELMET))
                .criterion("get_full_copper_armor3", InventoryChangedCriterion.Conditions.items(Armors.COPPER_LEGGINGS))
                .criterion("get_full_copper_armor4", InventoryChangedCriterion.Conditions.items(Armors.COPPER_CHEST_PLATE))
                .rewards(AdvancementRewards.Builder.experience(100))
                .build(exporter, "get_full_copper_armor");


        AdvancementEntry getFullIronArmor = Advancement.Builder.create().parent(getFullCopperArmor)
                .display(
                        Items.IRON_CHESTPLATE,
                        Text.literal("全副武装 II"),
                        Text.literal("穿上全套铁护甲\n你可能要穿着它度过很长时间\n获得200XP"),
                        null, // 子进度不需要设置背景
                        AdvancementFrame.TASK,
                        true,
                        false,
                        false
                )
                .criterion("get_full_iron_armor1", InventoryChangedCriterion.Conditions.items(Items.IRON_BOOTS))
                .criterion("get_full_iron_armor2", InventoryChangedCriterion.Conditions.items(Items.IRON_HELMET))
                .criterion("get_full_iron_armor3", InventoryChangedCriterion.Conditions.items(Items.IRON_LEGGINGS))
                .criterion("get_full_iron_armor4", InventoryChangedCriterion.Conditions.items(Items.IRON_CHESTPLATE))
                .rewards(AdvancementRewards.Builder.experience(200))
                .build(exporter, "get_full_iron_armor");


        AdvancementEntry getFullMithrilArmor = Advancement.Builder.create().parent(getFullCopperArmor)
                .display(
                        Items.IRON_CHESTPLATE,
                        Text.literal("全副武装 III"),
                        Text.literal("穿上全套秘银护甲\n现在,没有什么能真正伤害到你了\n获得500XP"),
                        null, // 子进度不需要设置背景
                        AdvancementFrame.TASK,
                        true,
                        false,
                        false
                )
                .criterion("get_full_mithril_armor1", InventoryChangedCriterion.Conditions.items(Armors.MITHRIL_BOOTS))
                .criterion("get_full_mithril_armor2", InventoryChangedCriterion.Conditions.items(Armors.MITHRIL_HELMET))
                .criterion("get_full_mithril_armor3", InventoryChangedCriterion.Conditions.items(Armors.MITHRIL_LEGGINGS))
                .criterion("get_full_mithril_armor4", InventoryChangedCriterion.Conditions.items(Armors.MITHRIL_CHEST_PLATE))
                .rewards(AdvancementRewards.Builder.experience(500))
                .build(exporter, "get_full_mithril_armor");










        AdvancementEntry goToUnderworld = Advancement.Builder.create().parent(getMetalPickAxe)
                .display(
                        Items.FLINT_AND_STEEL,
                        Text.literal("黑暗洞穴"),
                        Text.literal("在主世界的最底部建立传送门,进入地下世界维度"),
                        null, // 子进度不需要设置背景
                        AdvancementFrame.TASK,
                        true,
                        false,
                        false
                )
                .criterion("go_to_underworld", ChangedDimensionCriterion.Conditions.to(RegistryKey.of(RegistryKeys.WORLD, Identifier.of(MOD_ID, "underworld"))))
                .build(exporter, "go_to_underworld");

        AdvancementEntry goToNether = Advancement.Builder.create().parent(goToUnderworld)
                .display(
                        Items.FLINT_AND_STEEL,
                        Text.literal("让我们更深入一些"),
                        Text.literal("进入下界\n这将会是一场极为漫长的旅途,你需要先设法穿过一层基岩层,然后在最底部的岩浆池底寻找可用空间建立传送门"),
                        null, // 子进度不需要设置背景
                        AdvancementFrame.TASK,
                        true,
                        false,
                        false
                )
                .criterion("go_to_nether", ChangedDimensionCriterion.Conditions.to(World.NETHER))
                .build(exporter, "go_to_nether");



        AdvancementEntry getNetheriteIngot = Advancement.Builder.create().parent(goToNether)
                .display(
                        Items.NETHERITE_INGOT,
                        Text.literal("科技线的尾声"),
                        Text.literal("设法获取或制作下届合金锭,以合成下界合金工作台\n你可以在地狱底部开凿矿井隧道获取远古残骸,或者在猪灵堡垒大肆冒险一番,在宝箱中拿到想要的材料"),
                        null, // 子进度不需要设置背景
                        AdvancementFrame.TASK,
                        true,
                        false,
                        false
                )
                .criterion("get_netherite_ingot", InventoryChangedCriterion.Conditions.items(Items.NETHERITE_INGOT))
                .build(exporter, "get_netherite_ingot");

        AdvancementEntry getAdamantiumPickaxe = Advancement.Builder.create().parent(getNetheriteIngot)
                .display(
                        Tools.ADAMANTIUM_PICKAXE,
                        Text.literal("末影水晶破坏者"),
                        Text.literal("在下界合金工作台上制作艾德曼镐\n这是唯一能够破坏末影水晶的工具"),
                        null, // 子进度不需要设置背景
                        AdvancementFrame.TASK,
                        true,
                        false,
                        false
                )
                .criterion("get_adamantium_pickaxe", InventoryChangedCriterion.Conditions.items(Items.NETHERITE_INGOT))
                .build(exporter, "get_adamantium_pickaxe");



        AdvancementEntry useEnderEye = Advancement.Builder.create().parent(getAdamantiumPickaxe)
                .display(
                        Items.FLINT_AND_STEEL,
                        Text.literal("要塞追寻者"),
                        Text.literal("来到x和z轴坐标绝对值大于12000之外的位置,尝试成功投掷一次末影之眼"),
                        null, // 子进度不需要设置背景
                        AdvancementFrame.TASK,
                        true,
                        false,
                        false
                )
                .criterion("use_ender_eye", Criteria.USED_ENDER_EYE.create(new UsedEnderEyeCriterion.Conditions(Optional.empty(), new NumberRange.DoubleRange(Optional.of(0d),Optional.of(3200d),Optional.of(0d),Optional.of(3200d)))))
                .build(exporter, "use_ender_eye");


        AdvancementEntry followTheEyes = Advancement.Builder.create().parent(getAdamantiumPickaxe)
                .build(exporter, "followTheEyes");





        AdvancementEntry getMithril = Advancement.Builder.create().parent(goToUnderworld)
                .display(
                        Items.FLINT_AND_STEEL,
                        Text.literal("神话时代"),
                        Text.literal("用岩浆驱动的黑曜石熔炉冶炼秘银矿石,获得秘银锭\n获得200XP"),
                        null, // 子进度不需要设置背景
                        AdvancementFrame.TASK,
                        true,
                        false,
                        false
                )
                .criterion("get_mithril", InventoryChangedCriterion.Conditions.items(Metal.mithril))
                .rewards(AdvancementRewards.Builder.experience(200))
                .build(exporter, "get_mithril");

        AdvancementEntry getDiamond = Advancement.Builder.create().parent(goToUnderworld)
                .display(
                        Items.FLINT_AND_STEEL,
                        Text.literal("钻石!"),
                        Text.literal("制作秘银镐,以采集钻石,为制作附魔台做准备"),
                        null, // 子进度不需要设置背景
                        AdvancementFrame.TASK,
                        true,
                        false,
                        false
                )
                .criterion("get_diamond", InventoryChangedCriterion.Conditions.items(Items.DIAMOND))
                .build(exporter, "get_diamond");

        AdvancementEntry getEnchantingTable = Advancement.Builder.create().parent(getDiamond)
                .display(
                        Items.FLINT_AND_STEEL,
                        Text.literal("附魔师"),
                        Text.literal("用它制作附魔武器\n现在你可以对烈焰人发起更有效的伤害了"),
                        null, // 子进度不需要设置背景
                        AdvancementFrame.TASK,
                        true,
                        false,
                        false
                )
                .criterion("get_enchantment_table", InventoryChangedCriterion.Conditions.items(Items.ENCHANTING_TABLE))
                .build(exporter, "get_enchantment_table");


    }
}
