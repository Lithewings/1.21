package com.equilibrium.item.tools_attribute.metal;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class AdamantiumHoe extends MetalHoe{
    public AdamantiumHoe(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }
    private int count = 100;
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(count>=100 && entity instanceof PlayerEntity player && selected){
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED,400));
            count=0;
        }
        count++;
    }
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal("获得20%的速度加成").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("泰然自若地快步行走").formatted(Formatting.AQUA));

    }
}
