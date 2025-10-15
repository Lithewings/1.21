package com.equilibrium.mixin.crop;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.client.render.model.BlockStatesLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.equilibrium.MITEequilibrium.FERTILIZED;
import static net.minecraft.block.FarmlandBlock.MOISTURE;


@Mixin(FarmlandBlock.class)
public abstract class FarmlandBlockMixin extends Block {
    @Shadow @Final public static IntProperty MOISTURE;

    public FarmlandBlockMixin(Settings settings) {
        super(settings);
    }


    @Inject(method = "appendProperties",at = @At(value = "TAIL"))
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(FERTILIZED);
    }

//    @Override
//    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
//        builder.add(MOISTURE);
//    }
//
//
//    private boolean FERTILIZED =false;
////
////
////    @Override
////    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
////        builder.add(FERTILIZED);
////    }
//
//
    @Inject(method = "<init>",at = @At(value = "TAIL"))
    public void FarmlandBlock(Settings settings, CallbackInfo ci) {
        this.setDefaultState(this.stateManager.getDefaultState().with(FERTILIZED, false));
    }
//
//
    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {

        if (player.getStackInHand(Hand.MAIN_HAND).isOf(Items.BONE_MEAL)) {
            if (!world.isClient) {
                // 设置施肥状态为 true

                world.setBlockState(pos, state.with(FERTILIZED, true), Block.NOTIFY_ALL);
                // 添加视觉和声音效果
                world.syncWorldEvent(1505, pos, 0); // 骨粉使用效果

                // 消耗骨粉（非创造模式）
                if (!player.getAbilities().creativeMode) {
                    player.getStackInHand(Hand.MAIN_HAND).decrement(1);
                }
            }
            return ActionResult.SUCCESS;
        }







//        if(state.get(FERTILIZED)==true){
//            player.sendMessage(Text.of("该耕地已被施肥"));
//            return ActionResult.PASS;
//        }
//        else if(state.get(FERTILIZED)==false){
//            player.sendMessage(Text.of("该耕地还没有被施肥"));
//            return ActionResult.PASS;
//        }






        return ActionResult.PASS;
    }

}
