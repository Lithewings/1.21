package com.equilibrium.mixin;

import com.equilibrium.register.BlockInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(CraftingScreenHandler.class)
public abstract class recipeModifier {
    @Inject(method = "updateResult",at = @At(value = "HEAD"),cancellable = true)
    private static void updateResult(
            ScreenHandler handler, World world, PlayerEntity player, RecipeInputInventory craftingInventory, CraftingResultInventory resultInventory, @Nullable RecipeEntry<CraftingRecipe> recipe, CallbackInfo ci
    ) {


        ci.cancel();
        if (!world.isClient) {
            CraftingRecipeInput craftingRecipeInput = craftingInventory.createRecipeInput();
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)player;
            ItemStack itemStack = ItemStack.EMPTY;
            Optional<RecipeEntry<CraftingRecipe>> optional = world.getServer().getRecipeManager().getFirstMatch(RecipeType.CRAFTING, craftingRecipeInput, world, recipe);
            if (optional.isPresent()) {
                RecipeEntry<CraftingRecipe> recipeEntry = (RecipeEntry<CraftingRecipe>)optional.get();
                CraftingRecipe craftingRecipe = recipeEntry.value();
                if (resultInventory.shouldCraftRecipe(world, serverPlayerEntity, recipeEntry)) {
                    ItemStack itemStack2 = craftingRecipe.craft(craftingRecipeInput, world.getRegistryManager());
                    if (itemStack2.isItemEnabled(world.getEnabledFeatures())) {
                        itemStack = itemStack2;
                    }
                }
            }
            //合成表过滤器,按照物品频率排序
            if(itemStack.isOf(Items.WOODEN_SWORD)){
                itemStack = ItemStack.EMPTY;
            }
            if(itemStack.isOf(Items.WOODEN_AXE)){
                itemStack = ItemStack.EMPTY;
            }
            if(itemStack.isOf(Items.WOODEN_HOE)){
                itemStack = ItemStack.EMPTY;
            }
            if(itemStack.isOf(Items.WOODEN_PICKAXE)){
                itemStack = ItemStack.EMPTY;
            }

            if(itemStack.isOf(Items.STONE_SWORD)){
                itemStack = ItemStack.EMPTY;
            }
            if(itemStack.isOf(Items.STONE_PICKAXE)){
                itemStack = ItemStack.EMPTY;
            }
            if(itemStack.isOf(Items.STONE_AXE)){
                itemStack = ItemStack.EMPTY;
            }
            if(itemStack.isOf(Items.STONE_HOE)){
                itemStack = ItemStack.EMPTY;
            }
            if(itemStack.isOf(Items.STONE_SHOVEL)){
                itemStack = ItemStack.EMPTY;
            }







            resultInventory.setStack(0, itemStack);
            handler.setPreviousTrackedSlot(0, itemStack);
            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(handler.syncId, handler.nextRevision(), 0, itemStack));
        }
    }
}
