package com.equilibrium.mixin;

import com.equilibrium.item.Tools;
import com.equilibrium.tags.ModItemTags;
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


            //先删除要移除的物品
            if(itemStack.isIn(ModItemTags.REMOVEITEM))
                itemStack = ItemStack.EMPTY;




            //斧子中,替换铁,金
            if(itemStack.isIn(ModItemTags.AXES)){
                if(itemStack.isOf(Items.IRON_AXE))
                    itemStack = Tools.IRON_AXE.getDefaultStack();

                if(itemStack.isOf(Items.GOLDEN_AXE))
                    itemStack = Tools.GOLD_AXE.getDefaultStack();
            }

            if(itemStack.isIn(ModItemTags.HOES)){
                if(itemStack.isOf(Items.IRON_HOE))
                    itemStack = Tools.IRON_HOE.getDefaultStack();

                if(itemStack.isOf(Items.GOLDEN_HOE))
                    itemStack = Tools.GOLD_HOE.getDefaultStack();
            }

            if(itemStack.isIn(ModItemTags.SHOVELS)){
                if(itemStack.isOf(Items.IRON_SHOVEL))
                    itemStack = Tools.IRON_AXE.getDefaultStack();

                if(itemStack.isOf(Items.GOLDEN_SHOVEL))
                    itemStack = Tools.GOLD_SHOVEL.getDefaultStack();
            }

            if(itemStack.isIn(ModItemTags.SWORDS)){
                if(itemStack.isOf(Items.IRON_SWORD))
                    itemStack = Tools.IRON_SWORD.getDefaultStack();

                if(itemStack.isOf(Items.GOLDEN_SWORD))
                    itemStack = Tools.GOLD_SWORD.getDefaultStack();
            }
            if(itemStack.isIn(ModItemTags.PICKAXES)){
                if(itemStack.isOf(Items.IRON_PICKAXE))
                    itemStack = Tools.IRON_PICKAXE.getDefaultStack();

                if(itemStack.isOf(Items.GOLDEN_PICKAXE))
                    itemStack = Tools.GOLD_PICKAXE.getDefaultStack();
            }







            resultInventory.setStack(0, itemStack);
            handler.setPreviousTrackedSlot(0, itemStack);
            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(handler.syncId, handler.nextRevision(), 0, itemStack));
        }
    }
}
