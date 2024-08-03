package com.equilibrium.item;


import com.equilibrium.item.tools_attribute.flint.FlintAxeOrHatchet;
import com.equilibrium.item.tools_attribute.flint.FlintKnife;
import com.equilibrium.item.tools_attribute.ModToolMaterials;
import com.equilibrium.item.tools_attribute.flint.FlintShovel;
import com.equilibrium.item.tools_attribute.metal.*;
import net.minecraft.item.*;
import net.minecraft.network.packet.c2s.play.UpdateSignC2SPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;

public class Tools {
    //一个完整的工具注册流程

    //1、把它放进标签里,用于实体交互距离增益上以及附魔选取上
    //2、为它注册枚举类中的基础伤害和耐久
    //3、继承工具类,确定伤害实体和方块所消耗的耐久值如何
    //4、物品注册,模型注册,放置贴图







    //以下开始添加物品:


    public static final Item FLINT_HATCHET =createFlintAxeOrHatchetItem(ModToolMaterials.FLINT_HATCHET,4,1f);
    public static final Item FLINT_AXE =createFlintAxeOrHatchetItem(ModToolMaterials.FLINT_HATCHET,5,1f);
    public static final Item FLINT_KNIFE =createFlintKnifeItem(ModToolMaterials.FLINT_KNIFE,3,2f);
    public static final Item FLINT_SHOVEL =createFlintShovelItem(ModToolMaterials.FLINT_SHOVEL,2,2f);




    public static final Item COPPER_AXE =createMetalAxeItem(ModToolMaterials.COPPER_AXE,8,0.7f);
    public static final Item GOLD_AXE = createMetalAxeItem(ModToolMaterials.GOLD_AXE,5,0.5f);
    public static final Item SILVER_AXE = createMetalAxeItem(ModToolMaterials.SILVER_AXE,8,0.7f);
    public static final Item IRON_AXE = createMetalAxeItem(ModToolMaterials.IRON_AXE,9,0.7f);
    public static final Item MITHRIL_AXE = createMetalAxeItem(ModToolMaterials.ADAMANTIUM_AXE,10,0.8f);
    public static final Item ADAMANTIUM_AXE = createMetalAxeItem(ModToolMaterials.ADAMANTIUM_AXE,10,1f);


    public static final Item COPPER_PICKAXE = createMetalPickAxeItem(ModToolMaterials.COOPER_PICKAXE,5,2f);
    public static final Item GOLD_PICKAXE = createMetalPickAxeItem(ModToolMaterials.GOLD_PICKAXE,5,2f);
    public static final Item SILVER_PICKAXE = createMetalPickAxeItem(ModToolMaterials.SILVER_PICKAXE,5,2f);
    public static final Item IRON_PICKAXE = createMetalPickAxeItem(ModToolMaterials.IRON_PICKAXE,6,2f);
    public static final Item MITHRIL_PICKAXE = createMetalPickAxeItem(ModToolMaterials.MITHRIL_PICKAXE,7,2f);
    public static final Item ADMANTIUM_PICKAXE = createMetalPickAxeItem(ModToolMaterials.ADAMANTIUM_PICKAXE,8,2f);

    public static final Item COPPER_HOE = createMetalHoeItem(ModToolMaterials.COOPER_HOE,4,3f);
    public static final Item GOLD_HOE = createMetalHoeItem(ModToolMaterials.GOLD_HOE,4,3f);
    public static final Item SILVER_HOE = createMetalHoeItem(ModToolMaterials.SILVER_HOE,4,3f);
    public static final Item IRON_HOE = createMetalHoeItem(ModToolMaterials.IRON_HOE,5,3f);
    public static final Item MITHRIL_HOE = createMetalHoeItem(ModToolMaterials.MITHRIL_HOE,6,3f);
    public static final Item ADMANTIUM_HOE =createMetalHoeItem(ModToolMaterials.ADAMANTIUM_HOE,7,3f);


    public static final Item COPPER_SHOVEL = createMetalShovelItem(ModToolMaterials.COOPER_SHOVEL,3,4f);
    public static final Item GOLD_SHOVEL = createMetalShovelItem(ModToolMaterials.GOLD_SHOVEL,3,4f);
    public static final Item SILVER_SHOVEL = createMetalShovelItem(ModToolMaterials.SILVER_SHOVEL,3,4f);
    public static final Item IRON_SHOVEL = createMetalShovelItem(ModToolMaterials.IRON_SHOVEL,4,4f);
    public static final Item MITHRIL_SHOVEL = createMetalShovelItem(ModToolMaterials.MITHRIL_SHOVEL,5,4f);
    public static final Item ADMANTIUM_SHOVEL =createMetalShovelItem(ModToolMaterials.ADAMANTIUM_SHOVEL,6,4f);

    public static final Item COPPER_SWORD = createMetalSwordItem(ModToolMaterials.COOPER_SWORD,7,3f);
    public static final Item GOLD_SWORD = createMetalSwordItem(ModToolMaterials.GOLD_SWORD,7,3f);
    public static final Item SILVER_SWORD = createMetalSwordItem(ModToolMaterials.SILVER_SWORD,7,3f);
    public static final Item IRON_SWORD = createMetalSwordItem(ModToolMaterials.IRON_SWORD,8,3f);
    public static final Item MITHRIL_SWORD = createMetalSwordItem(ModToolMaterials.MITHRIL_SWORD,9,3f);
    public static final Item ADMANTIUM_SWORD =createMetalSwordItem(ModToolMaterials.ADAMANTIUM_SHOVEL,10,3f);

    public static final Item COPPER_DAGGER = createMetalSwordItem(ModToolMaterials.COOPER_DAGGER,6,4f);
    public static final Item GOLD_DAGGER = createMetalSwordItem(ModToolMaterials.GOLD_DAGGER,6,4f);
    public static final Item SILVER_DAGGER = createMetalSwordItem(ModToolMaterials.SILVER_DAGGER,6,4f);
    public static final Item IRON_DAGGER = createMetalSwordItem(ModToolMaterials.IRON_DAGGER,7,4f);
    public static final Item MITHRIL_DAGGER = createMetalSwordItem(ModToolMaterials.MITHRIL_DAGGER,8,4f);
    public static final Item ADMANTIUM_DAGGER =createMetalSwordItem(ModToolMaterials.ADAMANTIUM_DAGGER,9,4f);






    public static Item createFlintShovelItem(ToolMaterial material, int finalDamage,float finalDamageSpeed){
        return new FlintShovel(material,new Item.Settings().
                attributeModifiers(MiningToolItem.createAttributeModifiers(material,-1+finalDamage,-4+finalDamageSpeed))
        );
    }

    public static Item createFlintKnifeItem(ToolMaterial material, int finalDamage,float finalDamageSpeed){
        return new FlintKnife(material,new Item.Settings().
                attributeModifiers(MiningToolItem.createAttributeModifiers(material,-1+finalDamage,-4+finalDamageSpeed))
        );
    }




    public static Item createFlintAxeOrHatchetItem(ToolMaterial material, int finalDamage,float finalDamageSpeed){
        return new FlintAxeOrHatchet(material,new Item.Settings().
                attributeModifiers(MiningToolItem.createAttributeModifiers(material,-1+finalDamage,-4+finalDamageSpeed))
        );
    }
    public static Item createMetalAxeItem(ToolMaterial material, int finalDamage,float finalDamageSpeed){
        return new MetalAxe(material,new Item.Settings().
                attributeModifiers(MiningToolItem.createAttributeModifiers(material,-1+finalDamage,-4+finalDamageSpeed))
        );
    }
    public static Item createMetalHoeItem(ToolMaterial material, int finalDamage,float finalDamageSpeed){
        return new MetalHoe(material,new Item.Settings().
                attributeModifiers(MiningToolItem.createAttributeModifiers(material,-1+finalDamage,-4+finalDamageSpeed))
        );
    }
    public static Item createMetalShovelItem(ToolMaterial material, int finalDamage,float finalDamageSpeed){
        return new MetalShovel(material,new Item.Settings().
                attributeModifiers(MiningToolItem.createAttributeModifiers(material,-1+finalDamage,-4+finalDamageSpeed))
        );
    }


    public static Item createMetalPickAxeItem(ToolMaterial material, int finalDamage,float finalDamageSpeed){
        return new MetalPickAxe(material,BlockTags.PICKAXE_MINEABLE,new Item.Settings().
                attributeModifiers(MiningToolItem.createAttributeModifiers(material,-1+finalDamage,-4+finalDamageSpeed))
        );
    }
    public static Item createMetalSwordItem(ToolMaterial material, int finalDamage,float finalDamageSpeed){

        return new MetalSword(material,new Item.Settings().
                attributeModifiers(MiningToolItem.createAttributeModifiers(material,-1+finalDamage,-4+finalDamageSpeed))
        );
    }





    public static void registerModItemTools() {

        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","adamantium_axe"), ADAMANTIUM_AXE);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","mithril_axe"), MITHRIL_AXE);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","iron_axe"),IRON_AXE);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","copper_axe"),COPPER_AXE);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","silver_axe"),SILVER_AXE);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","gold_axe"),GOLD_AXE);

        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","adamantium_hoe"),ADMANTIUM_HOE);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","mithril_hoe"), MITHRIL_HOE);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","iron_hoe"),IRON_HOE);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","copper_hoe"),COPPER_HOE);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","silver_hoe"),SILVER_HOE);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","gold_hoe"),GOLD_HOE);

        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","adamantium_pickaxe"), ADMANTIUM_PICKAXE);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","mithril_pickaxe"), MITHRIL_PICKAXE);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","iron_pickaxe"),IRON_PICKAXE);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","copper_pickaxe"),COPPER_PICKAXE);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","silver_pickaxe"),SILVER_PICKAXE);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","gold_pickaxe"),GOLD_PICKAXE);

        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","adamantium_sword"), ADMANTIUM_SWORD);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","mithril_sword"), MITHRIL_SWORD);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","iron_sword"),IRON_SWORD);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","copper_sword"),COPPER_SWORD);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","silver_sword"),SILVER_SWORD);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","gold_sword"),GOLD_SWORD);


        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","adamantium_shovel"), ADMANTIUM_SHOVEL);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","mithril_shovel"), MITHRIL_SHOVEL);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","iron_shovel"),IRON_SHOVEL);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","copper_shovel"),COPPER_SHOVEL);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","silver_shovel"),SILVER_SHOVEL);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","gold_shovel"),GOLD_SHOVEL);


        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","adamantium_dagger"), ADMANTIUM_DAGGER);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","mithril_dagger"), MITHRIL_DAGGER);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","iron_dagger"),IRON_DAGGER);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","copper_dagger"),COPPER_DAGGER);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","silver_dagger"),SILVER_DAGGER);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","gold_dagger"),GOLD_DAGGER);



        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","flint_hatchet"),FLINT_HATCHET);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","flint_axe"),FLINT_AXE);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","flint_knife"), FLINT_KNIFE);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium","flint_shovel"),FLINT_SHOVEL);
    }
}
