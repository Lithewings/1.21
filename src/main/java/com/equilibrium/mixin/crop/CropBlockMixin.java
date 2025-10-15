package com.equilibrium.mixin.crop;

import com.equilibrium.MITEequilibrium;
import com.equilibrium.util.ServerInfoRecorder;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.List;

import static com.equilibrium.MITEequilibrium.FERTILIZED;
import static com.equilibrium.MITEequilibrium.MOD_ID;

@Mixin(CropBlock.class)
public abstract class CropBlockMixin extends PlantBlock implements Fertilizable {


    @Shadow public abstract int getAge(BlockState state);

    @Shadow public abstract BlockState withAge(int age);

    @Shadow public abstract void applyGrowth(World world, BlockPos pos, BlockState state);

    protected CropBlockMixin(Settings settings) {
        super(settings);
    }
   @Shadow
    public int getMaxAge() {
        return 7;
    }


    private int lastRandomTickDay;

    @Inject(method = "<init>",at = @At(value = "TAIL"))
    public void CropBlock(Settings settings, CallbackInfo ci) {
        //初始化种植日期
        lastRandomTickDay =ServerInfoRecorder.getDay();
    }

    @Inject(method = "randomTick",at = @At(value = "HEAD"),cancellable = true)
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        ci.cancel();
        //成功进行了一轮随机刻,记录当前日期
        int thisRandomTickDay = ServerInfoRecorder.getDay();
        //12天必然成长一个阶段
        if(thisRandomTickDay-lastRandomTickDay>=12){
            this.applyGrowth(world,pos,state);
            lastRandomTickDay = thisRandomTickDay;
        }
        if (world.getBaseLightLevel(pos, 0) >= 9) {
            int i = this.getAge(state);
            if (i < this.getMaxAge()) {
                float f = CropBlock.getAvailableMoisture(this, world, pos);
                float times = 128;
                //检查农田是否具有施肥标签
                if(world.getBlockState(pos.down()).contains(FERTILIZED)) {
                    if (world.getBlockState(pos.down()).get(FERTILIZED) == true)
                        //原先的两倍加速
                        times=64f;
                    else
                        times=128;
                }
                else
                    MITEequilibrium.LOGGER.error("No such Block State called fertilized");


                if (random.nextInt((int)(times*25.0F / f) + 1) == 0) {
                    world.setBlockState(pos, this.withAge(i + 1), Block.NOTIFY_LISTENERS);
                }
            }
        }





}}
