package com.equilibrium.mixin.vanilla_blocksmixin;

import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import static net.minecraft.block.Blocks.register;

@Mixin(Blocks.class)
public class BlocksMixin {

//    @Final
//    private static  Block COAL_ORE = register(
//            "coal_ore",
//            new ExperienceDroppingBlock(
//                    UniformIntProvider.create(0, 2),
//                    AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(1.5F, 3.0F)
//            )
//    );
//    @Shadow
//    public static Block register(String id, Block block) {
//        return Registry.register(Registries.BLOCK, id, block);
//    }
//@Shadow
//@Final
//    public static Block WATER = register(
//            "water",
//            new FluidBlock(
//                    Fluids.WATER,
//                    AbstractBlock.Settings.create()
//                            .mapColor(MapColor.WATER_BLUE)
//                            .replaceable()
//                            .noCollision()
//                            .strength(100.0F)
//                            .pistonBehavior(PistonBehavior.DESTROY)
//                            .dropsNothing()
//                            .liquid()
//                            .sounds(BlockSoundGroup.INTENTIONALLY_EMPTY)
//            )
//    );
//



}
