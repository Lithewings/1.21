package com.equilibrium.mixin.structure;

import net.minecraft.world.gen.chunk.placement.RandomSpreadStructurePlacement;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RandomSpreadStructurePlacement.class)
public class RandomSpreadStructurePlacementMixin {
    @Shadow
    @Final
    private int spacing = 256;
    @Shadow
    @Final
    private int separation = 254;
}
