package com.equilibrium.mixin.structure.village;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructureLiquidSettings;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePoolElementType;
import net.minecraft.structure.pool.alias.StructurePoolAliasLookup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.gen.structure.DimensionPadding;
import net.minecraft.world.gen.structure.Structure;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

import static com.equilibrium.MITEequilibrium.LOGGER;

@Mixin(StructurePoolBasedGenerator.class)
public class structurePoolBasedGeneratorMixin {

    @Shadow @Final private static Logger LOGGER;

    private static Optional<? extends RegistryKey<?>> key;

    private static boolean regEntryContains(@NotNull RegistryEntry<?> entry, String pattern) {
        key = entry.getKey();
        if (key != null && key.isPresent()) return key.get().getValue().getPath().contains(pattern);
        return false;
    }

    @Inject(method = "generate(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/util/Identifier;ILnet/minecraft/util/math/BlockPos;Z)Z", at = @At("HEAD"), cancellable = true)
    private static void generate(ServerWorld world, RegistryEntry<StructurePool> structurePool, Identifier id, int size, BlockPos pos, boolean keepJigsaws, CallbackInfoReturnable<Boolean> cir) {
        if (regEntryContains(structurePool, "village")) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Inject(method = "generate(Lnet/minecraft/world/gen/structure/Structure$Context;Lnet/minecraft/registry/entry/RegistryEntry;Ljava/util/Optional;ILnet/minecraft/util/math/BlockPos;ZLjava/util/Optional;ILnet/minecraft/structure/pool/alias/StructurePoolAliasLookup;Lnet/minecraft/world/gen/structure/DimensionPadding;Lnet/minecraft/structure/StructureLiquidSettings;)Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
    private static void generate(Structure.Context context, RegistryEntry<StructurePool> structurePool, Optional<Identifier> id, int size, BlockPos pos, boolean useExpansionHack, Optional<Heightmap.Type> projectStartToHeightmap, int maxDistanceFromCenter, StructurePoolAliasLookup aliasLookup, DimensionPadding dimensionPadding, StructureLiquidSettings liquidSettings, CallbackInfoReturnable<Optional<Structure.StructurePosition>> cir) {
        if (regEntryContains(structurePool, "village")) {
            cir.setReturnValue(Optional.empty());
            cir.cancel();
        }
    }

    @Mixin(StructurePoolBasedGenerator.StructurePoolGenerator.class)
    private static class StructurePoolGeneratorMixin {
        @Inject(method = "generatePiece", at = @At("HEAD"), cancellable = true)
        void generatePiece(PoolStructurePiece piece, MutableObject<VoxelShape> pieceShape, int minY, boolean modifyBoundingBox, HeightLimitView world, NoiseConfig noiseConfig, StructurePoolAliasLookup aliasLookup, StructureLiquidSettings liquidSettings, CallbackInfo ci) {
            if (piece == null) return;
            StructurePoolElement element = piece.getPoolElement();
            if (element == null) return;
            StructurePoolElementType<?> type = element.getType();
            if (type == null) return;
            if (type.toString().contains("village")) {
                ci.cancel();
            }
        }
    }



}