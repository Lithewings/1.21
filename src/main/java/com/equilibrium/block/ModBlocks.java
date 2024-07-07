package com.equilibrium.block;


import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import static net.minecraft.block.Blocks.register;

public class ModBlocks {

    public static final Block EXAMPLE_BLOCK = new Block(Block.Settings.create().strength(4.0f));
    //strength中第一个为硬度,第二个为爆炸抗性
    public static final Block UNDERWORLD_PORTAL =
            new UnderworldPortalBlock(
                    AbstractBlock.Settings.create()
                            .noCollision()
                            .ticksRandomly()
                            .strength(-1.0F)
                            .sounds(BlockSoundGroup.GLASS)
                            .luminance(state -> 11)
                            .pistonBehavior(PistonBehavior.BLOCK)
            );
    public static final Block OVERWORLD_PORTAL =
            new UnderworldPortalBlock(
                    AbstractBlock.Settings.create()
                            .noCollision()
                            .ticksRandomly()
                            .strength(-1.0F)
                            .sounds(BlockSoundGroup.GLASS)
                            .luminance(state -> 11)
                            .pistonBehavior(PistonBehavior.BLOCK)
            );









    public static void registerModBlocks(){
        Registry.register(Registries.BLOCK, Identifier.of("miteequilibrium", "example_block"), EXAMPLE_BLOCK);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium", "example_block"), new BlockItem(EXAMPLE_BLOCK, new Item.Settings()));

        Registry.register(Registries.BLOCK, Identifier.of("miteequilibrium", "underworld_portalblock"), UNDERWORLD_PORTAL);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium", "underworld_portalblock"), new BlockItem(UNDERWORLD_PORTAL, new Item.Settings()));

        Registry.register(Registries.BLOCK, Identifier.of("miteequilibrium", "overworld_portalblock"), OVERWORLD_PORTAL);
        Registry.register(Registries.ITEM, Identifier.of("miteequilibrium", "overworld_portalblock"), new BlockItem(OVERWORLD_PORTAL, new Item.Settings()));


    }
}
