package com.equilibrium.block.blockentity;

import com.equilibrium.craft_time_register.BlockEnityRegistry;
import com.equilibrium.craft_time_register.BlockInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.FurnaceScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class TheFurnaceEntity extends AbstractFurnaceBlockEntity {
    public TheFurnaceEntity(BlockPos pos, BlockState state) {
        super(BlockEnityRegistry.THE_FURNACE, pos, state, RecipeType.SMELTING);
    }

    @Override
    public Text getContainerName() {
        Block block = this.world.getBlockState(this.getPos()).getBlock();
        if(block == BlockInit.CLAY_FURNACE){
            return Text.translatable("container.clay_furnace");
        }
        if(block == BlockInit.NETHERRACK_FURNACE){
            return Text.translatable("container.netherrack_furnace");
        }
        if(block == BlockInit.OBSIDIAN_FURNACE){
            return Text.translatable("container.obsidian_furnace");
        }
        return Text.translatable("container.furnace");
    }

    @Override
    public ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new FurnaceScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    public int getFuelTime(ItemStack fuel) {
        //此处world必须判断是否为null，否则熔炉数据无法保存。
        if(this.getWorld() != null){
            Block block = this.world.getBlockState(this.getPos()).getBlock();
            if(block == BlockInit.CLAY_FURNACE){
                return (int) (super.getFuelTime(fuel));
            }
            if(block == BlockInit.OBSIDIAN_FURNACE){
                return (int) (super.getFuelTime(fuel) / 5);
            }
            if(block == BlockInit.NETHERRACK_FURNACE){
                return (int) (super.getFuelTime(fuel) / 10);
            }
        }
        return (int) (super.getFuelTime(fuel));
    }

}
