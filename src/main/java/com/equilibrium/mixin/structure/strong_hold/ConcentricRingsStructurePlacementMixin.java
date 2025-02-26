package com.equilibrium.mixin.structure.strong_hold;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.structure.StructureSet;
import net.minecraft.structure.StructureSets;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionTypeRegistrar;
import net.minecraft.world.gen.chunk.placement.ConcentricRingsStructurePlacement;
import net.minecraft.world.gen.chunk.placement.StructurePlacement;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(ConcentricRingsStructurePlacement.class)
public abstract class ConcentricRingsStructurePlacementMixin implements StructureSets {


    @Final
    @Shadow
    private int distance = 16;

    @Final
    @Shadow
    private int spread = 1;

    @Final
    @Shadow
    //谨慎选取大小,这会严重影响加载世界时的性能
    private int count =128;
}
