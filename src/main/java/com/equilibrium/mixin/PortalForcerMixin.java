package com.equilibrium.mixin;

import com.equilibrium.block.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.dimension.PortalForcer;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraft.world.poi.PointOfInterestTypes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Comparator;
import java.util.Optional;


import static net.minecraft.world.poi.PointOfInterestType.*;

@Mixin(PortalForcer.class)
public abstract class PortalForcerMixin {


    @Final
    @Shadow private ServerWorld world;

        @Inject(method = "getPortalPos",at = @At("TAIL"),cancellable = true)
        public void getPortalPos(BlockPos pos, boolean destIsNether, WorldBorder worldBorder, CallbackInfoReturnable<Optional<BlockPos>> cir) {
            System.out.println(cir.getReturnValue());
    }
//    @Inject(method = "createPortal",at = @At("HEAD"))
//    public void createPortal(BlockPos pos, Direction.Axis axis, CallbackInfoReturnable<Optional<BlockLocating.Rectangle>> cir) {
//        System.out.println("getPortalPos");
//    }
//    @Inject(method = "isBlockStateValid",at = @At("HEAD"))
//    private void isBlockStateValid(BlockPos.Mutable pos, CallbackInfoReturnable<Boolean> cir) {
//        System.out.println("isBlockStateValid");
//    }
//    @Inject(method = "isValidPortalPos",at = @At("HEAD"))
//    private void isValidPortalPos(BlockPos pos, BlockPos.Mutable temp, Direction portalDirection, int distanceOrthogonalToPortal, CallbackInfoReturnable<Boolean> cir) {
//        System.out.println("isValidPortalPos");
//    }
//    @Inject(method = "getPortalPos",at = @At("HEAD"), cancellable = true)
//    public void getPortalPos(BlockPos pos, boolean destIsNether, WorldBorder worldBorder, CallbackInfoReturnable<Optional<BlockPos>> cir) {
//        cir.cancel();
//        PointOfInterestStorage pointOfInterestStorage = this.world.getPointOfInterestStorage();
//        int i = destIsNether ? 16 : 128;
//        pointOfInterestStorage.preloadChunks(this.world, pos, i);
//
//        Comparator<BlockPos> distanceComparator = Comparator.comparingDouble(blockPos2 -> blockPos2.getSquaredDistance(pos));
//        Comparator<BlockPos> yComparator = Comparator.comparingInt(BlockPos::getY);
//        Comparator<BlockPos> combinedComparator = distanceComparator.thenComparing(yComparator);
//
//
//        RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, Identifier.of("miteequilibrium","underworld_portal"));
//        PointOfInterestTypes.registerAndGetDefault((Registry<PointOfInterestType>) UNDERWORLD_PORTAL);
//
//
//
//
//        cir.setReturnValue(pointOfInterestStorage.getInSquare(
//                        poiType -> poiType.matchesKey(PointOfInterestTypes.NETHER_PORTAL), pos, i, PointOfInterestStorage.OccupationStatus.ANY
//                )
//                .map(PointOfInterest::getPos)
//                .filter(worldBorder::contains)
//                .filter(blockPos -> this.world.getBlockState(blockPos).contains(Properties.HORIZONTAL_AXIS))
//                .min(combinedComparator));
//    }



}
