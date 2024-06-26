package com.equilibrium.block;

import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static net.minecraft.block.Blocks.register;

public class ModBlocksTest {

    public static final Block EXAMPLE_BLOCK = new Block(Block.Settings.create().strength(4.0f));
    //strength中第一个为硬度,第二个为爆炸抗性
















    public static void registerModBlocks(){
        Registry.register(Registries.BLOCK, Identifier.of("miteequilibrium", "example_block"), EXAMPLE_BLOCK);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium", "example_block"), new BlockItem(EXAMPLE_BLOCK, new Item.Settings()));
    }
}
