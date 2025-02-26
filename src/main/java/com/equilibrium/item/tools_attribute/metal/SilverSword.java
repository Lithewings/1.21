package com.equilibrium.item.tools_attribute.metal;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import javax.swing.text.html.StyleSheet;
import java.util.List;

public class SilverSword extends MetalSword{
    public SilverSword(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal("对亡灵生物造成2倍伤害").formatted(Formatting.AQUA));
        tooltip.add(Text.literal("对亡灵生物给予最后一击后,获得5秒的再生II").formatted(Formatting.AQUA));
    }
    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.postDamageEntity(stack, target, attacker);
        if(target.isDead() && target.getType().isIn(EntityTypeTags.UNDEAD))
            attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION,100,1));
    }


}
