package com.equilibrium.item.tools;

import com.equilibrium.MITEequilibrium;
import com.equilibrium.mixin.MITEequilibriumMixin;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;

public class FlintHatchet extends MiningToolItem {
    public FlintHatchet(ToolMaterial material,Settings settings) {
        super(material, BlockTags.AXE_MINEABLE, settings);
    }

}
