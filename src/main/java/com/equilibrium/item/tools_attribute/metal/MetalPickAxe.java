package com.equilibrium.item.tools_attribute.metal;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;



public class MetalPickAxe extends ToolItem {

    public MetalPickAxe(ToolMaterial material,Settings settings) {
        super(material,settings.component(DataComponentTypes.TOOL, createToolComponent()));
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        ToolComponent toolComponent = stack.get(DataComponentTypes.TOOL);
        if (toolComponent == null) {
            return false;
        } else {
            if (!world.isClient && state.getHardness(world, pos) != 0.0F) {
                stack.damage(stack.isSuitableFor(state)? 0 : 480 , miner, EquipmentSlot.MAINHAND);
            }

            return true;
        }
    }
    private static ToolComponent createToolComponent() {
        return new ToolComponent(
                List.of(ToolComponent.Rule.ofAlwaysDropping(BlockTags.PICKAXE_MINEABLE, 4F)), 1.0F, 0
        );
    }



    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(150, attacker, EquipmentSlot.MAINHAND);
    }
}
