package com.equilibrium.event;


import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.network.message.SentMessage;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

import static com.equilibrium.MITEequilibrium.LOGGER;


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
        if (state.getBlock() == Blocks.GRAVEL) {
            ItemStack itemStack =player.getMainHandStack();
            LOGGER.info("itemStack is "+itemStack);
            LOGGER.info("enchantment is "+itemStack.getEnchantments());
//            world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.LOOTING).get();
            LOGGER.info("enchantment level is "+EnchantmentHelper.getLevel(world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.FORTUNE).get(),itemStack));

            Random random =new Random();
            int randomNumber = random.nextInt(33);
            ItemEntity itemDrop;
            if(randomNumber==0){
                itemDrop = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(),
                        new ItemStack(Items.GOLD_BLOCK));
            } else if(randomNumber<=4) {
                itemDrop = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(),
                        new ItemStack(Items.GOLD_INGOT));
            }else if(randomNumber <= 8){
                itemDrop = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(),
                        new ItemStack(Items.GOLD_INGOT));
            } else if (randomNumber <= 16) {
                itemDrop = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(),
                        new ItemStack(Items.GOLD_NUGGET));
            }else{
                itemDrop = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(),
                        new ItemStack(Items.GRAVEL));
            }
            world.spawnEntity(itemDrop);
        }else{
            return;
        }
        }
    public static void addEnchantmentToItem(PlayerEntity player, Enchantment enchantment, int level) {

    }





}









