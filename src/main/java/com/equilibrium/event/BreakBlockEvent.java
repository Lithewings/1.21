package com.equilibrium.event;
import com.equilibrium.item.Metal;
import com.equilibrium.tags.ModBlockTags;
import com.equilibrium.util.IsMinable;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class BreakBlockEvent implements PlayerBlockBreakEvents.After{
    /**
     * Called after a block is successfully broken.
     *
     * @param world       the world where the block was broken
     * @param player      the player who broke the block
     * @param pos         the position where the block was broken
     * @param state       the block state <strong>before</strong> the block was broken
     * @param blockEntity the block entity of the broken block, can be {@code null}
     */
    @Override
    public void afterBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {

        ItemStack itemStack =player.getMainHandStack();

        player.sendMessage(Text.of("Block Harvest Level is :"+IsMinable.getBlockHarvertLevel(state)));
        player.sendMessage(Text.of("Item Harvest Level is :"+IsMinable.getItemHarvertLevel(itemStack)));
        player.sendMessage(Text.of(""+state.getBlock().toString()));

        if(IsMinable.getBlockHarvertLevel(state)<=IsMinable.getItemHarvertLevel(itemStack))
            player.sendMessage(Text.of("Is Minable"));
        else{
            player.sendMessage(Text.of("Not Minable"));
        }


        if(state.isIn(ModBlockTags.LOG_120)){
            itemStack.damage(120-1,player, EquipmentSlot.MAINHAND);
            player.sendMessage(Text.of("-120"));
        }else

        if(state.isIn(ModBlockTags.STONE_LIKE_240)){
            itemStack.damage(240-1,player, EquipmentSlot.MAINHAND);
            player.sendMessage(Text.of("-240"));
        }
        else
        if(state.isIn(ModBlockTags.STONE_LIKE_360)){
            itemStack.damage(360-1,player, EquipmentSlot.MAINHAND);
            player.sendMessage(Text.of("-360"));
        }
        else
        if(state.isIn(ModBlockTags.STONE_LIKE_480)){
            itemStack.damage(480-1,player, EquipmentSlot.MAINHAND);
            player.sendMessage(Text.of("-480"));
        }

        //240 360 480其余没有列出的,但是确实比较硬的物品,而且在三者子集里容易冲突
        else
        if(state.isIn(BlockTags.NEEDS_STONE_TOOL)){
            itemStack.damage(240-1,player, EquipmentSlot.MAINHAND);
            player.sendMessage(Text.of("-240"));}
        else
        if(state.isIn(BlockTags.NEEDS_IRON_TOOL)) {
            itemStack.damage(360 - 1, player, EquipmentSlot.MAINHAND);
            player.sendMessage(Text.of("-360"));
        }
        else
        if(state.isIn(BlockTags.NEEDS_DIAMOND_TOOL)) {
            itemStack.damage(480 - 1, player, EquipmentSlot.MAINHAND);
            player.sendMessage(Text.of("-480"));
        }

        else
        if(state.isIn(ModBlockTags.NORMAL_30)){
            itemStack.damage(30-1,player, EquipmentSlot.MAINHAND);
        player.sendMessage(Text.of("-30"));
        }
        else
        if(state.isIn(ModBlockTags.NORMAL_60)){
            itemStack.damage(60-1,player, EquipmentSlot.MAINHAND);
            player.sendMessage(Text.of("-60"));
        }



        if (state.getBlock() == Blocks.GRAVEL) {

            itemStack =player.getMainHandStack();
            int funtuneLevel=EnchantmentHelper.getLevel(world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.FORTUNE).get(),itemStack);
            int slikTouch=EnchantmentHelper.getLevel(world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.SILK_TOUCH).get(),itemStack);

            if (slikTouch==1){
                world.spawnEntity(new ItemEntity(world, pos.getX()+0.5, pos.getY(), pos.getZ()+0.5,
                        new ItemStack(Items.GRAVEL)));
                return;
            }
            Random random =new Random();
            int randomNumber1 = random.nextInt(100);
            if(randomNumber1<75-funtuneLevel*15){
                world.spawnEntity(new ItemEntity(world, pos.getX()+0.5, pos.getY(), pos.getZ()+0.5,
                        new ItemStack(Items.GRAVEL)));
                return;
            }




            int randomNumber2 = random.nextInt(100);


            ItemEntity itemDrop;
            if(randomNumber2==0){
                //0,就1个,1%
                itemDrop = new ItemEntity( world, pos.getX()+0.5, pos.getY(), pos.getZ()+0.5,
                        new ItemStack(Metal.copper_nugget));
                world.spawnEntity(itemDrop);
                world.spawnEntity(itemDrop);
                world.spawnEntity(itemDrop);

            } else if(randomNumber2<=8) {
                //1-8,共8个 8%
                itemDrop = new ItemEntity( world, pos.getX()+0.5, pos.getY(), pos.getZ()+0.5,
                        new ItemStack(Metal.silver_nugget));
                world.spawnEntity(itemDrop);

            } else if (randomNumber2 <= 24) {
                //9-24,共16个 16%
                itemDrop = new ItemEntity( world, pos.getX()+0.5, pos.getY(), pos.getZ()+0.5,
                        new ItemStack(Items.FLINT));
                world.spawnEntity(itemDrop);

            } else if (randomNumber2 <= 46) {
                //25-46,共22个 22%
                itemDrop = new ItemEntity( world, pos.getX()+0.5, pos.getY(), pos.getZ()+0.5,
                        new ItemStack(Metal.copper_nugget));
                world.spawnEntity(itemDrop);

            }else{
                //47-99,共53个 53%
                itemDrop = new ItemEntity(world, pos.getX()+0.5, pos.getY(), pos.getZ()+0.5,
                        new ItemStack(Metal.FLINT));
                world.spawnEntity(itemDrop);
            }
        }else{
            return;
        }
        }






}









