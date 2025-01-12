package com.equilibrium.event;
import com.equilibrium.block.ModBlocks;
import com.equilibrium.item.Metal;
import com.equilibrium.tags.ModBlockTags;
import com.equilibrium.tags.ModItemTags;
import com.equilibrium.util.BlockToItemConverter;
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

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class BreakBlockEvent implements PlayerBlockBreakEvents.After{
    BlockToItemConverter blockToItemConverter = new BlockToItemConverter();

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
            itemStack.damage(120,player, EquipmentSlot.MAINHAND);
            player.sendMessage(Text.of("-120"));
        }else

        if(state.isIn(ModBlockTags.STONE_LIKE_240)){
            itemStack.damage(240,player, EquipmentSlot.MAINHAND);
            player.sendMessage(Text.of("-240"));
        }
        else
        if(state.isIn(ModBlockTags.STONE_LIKE_360)){
            itemStack.damage(360,player, EquipmentSlot.MAINHAND);
            player.sendMessage(Text.of("-360"));
        }
        else
        if(state.isIn(ModBlockTags.STONE_LIKE_480)){
            itemStack.damage(480,player, EquipmentSlot.MAINHAND);
            player.sendMessage(Text.of("-480"));
        }

        //240 360 480其余没有列出的,但是确实比较硬的物品,而且在三者子集里容易冲突
        else
        if(state.isIn(BlockTags.NEEDS_STONE_TOOL)){
            itemStack.damage(240,player, EquipmentSlot.MAINHAND);
            player.sendMessage(Text.of("-240"));}
        else
        if(state.isIn(BlockTags.NEEDS_IRON_TOOL)) {
            itemStack.damage(360, player, EquipmentSlot.MAINHAND);
            player.sendMessage(Text.of("-360"));
        }
        else
        if(state.isIn(BlockTags.NEEDS_DIAMOND_TOOL)) {
            itemStack.damage(480, player, EquipmentSlot.MAINHAND);
            player.sendMessage(Text.of("-480"));
        }
        else
        if(state.isIn(ModBlockTags.NORMAL_30)){
            itemStack.damage(30,player, EquipmentSlot.MAINHAND);
        player.sendMessage(Text.of("-30"));
        }
        else
        if(state.isIn(ModBlockTags.NORMAL_60)){
            itemStack.damage(60,player, EquipmentSlot.MAINHAND);
            player.sendMessage(Text.of("-60"));
        }

        Random random =new Random();
        int furtuneLevel=EnchantmentHelper.getLevel(world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.FORTUNE).get(),itemStack);
        int slikTouch=EnchantmentHelper.getLevel(world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.SILK_TOUCH).get(),itemStack);

        if (state.isIn(BlockTags.LEAVES)) {
            ItemEntity itemDrop;


            int randomNumber = random.nextInt(100-furtuneLevel*30);
            if(randomNumber<5) {
                itemDrop = new ItemEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
                        new ItemStack(Items.STICK));
                world.spawnEntity(itemDrop);
            }

        }
        if (state.getBlock() == Blocks.GRAVEL) {


            if (slikTouch==1){
                world.spawnEntity(new ItemEntity(world, pos.getX()+0.5, pos.getY(), pos.getZ()+0.5,
                        new ItemStack(Items.GRAVEL)));
                return;
            }

            int randomNumber1 = random.nextInt(100);
            if(randomNumber1<75-furtuneLevel*15){
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
        }





        if (state.isIn(ModBlockTags.ORE)){
            //掉落个数,比如红石就应该多次掉落
            int dropTime = 1;
            //获取矿石掉落物
            Item item = blockToItemConverter.convertBlockToItem(state.getBlock());
            if(item==Items.LAPIS_LAZULI || item==Items.REDSTONE || item==Items.GOLD_NUGGET)
                //4~7次掉落
                dropTime= 4+ random.nextInt(4);

            //掉落1次还是4次
            for(int i =0 ;i<dropTime;i++){
                world.spawnEntity(new ItemEntity(world, pos.getX()+0.5, pos.getY(), pos.getZ()+0.5,
                        new ItemStack(item)));
                //若时运为3,则表示随机的数字 0 1 2 3 4 5 6 7 8 9 中大于等于7的概率,即0.3
                if(random.nextInt(10)>=(10-furtuneLevel)){
                    world.spawnEntity(new ItemEntity(world, pos.getX()+0.5, pos.getY(), pos.getZ()+0.5,
                            new ItemStack(item)));
                }
            }





        }




    }













}









