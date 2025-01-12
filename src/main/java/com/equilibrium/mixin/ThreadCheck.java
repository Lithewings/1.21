package com.equilibrium.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TripwireHookBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.equilibrium.MITEequilibrium.LOGGER;

@Mixin(TripwireHookBlock.class)
public class ThreadCheck {
    @Redirect(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private static boolean checkBeforeSet(World world, BlockPos pos, BlockState state, int flags){
        if (world.getBlockState(pos).isOf(state.getBlock())){
            LOGGER.info("Should Check BlockState");
            return world.setBlockState(pos,state, flags);
        }else {
            return false;
        }
    }
}
