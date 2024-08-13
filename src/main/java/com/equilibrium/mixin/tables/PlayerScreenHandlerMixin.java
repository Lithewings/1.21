package com.equilibrium.mixin.tables;

import com.equilibrium.craft_time_register.BlockInit;
import com.equilibrium.item.Metal;
import com.equilibrium.item.Tools;
import com.equilibrium.tags.ModItemTags;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Mixin(PlayerScreenHandler.class)
public abstract class PlayerScreenHandlerMixin extends AbstractRecipeScreenHandler<CraftingRecipeInput, CraftingRecipe> {
    @Shadow @Final private CraftingResultInventory craftingResult;
    @Shadow @Final public boolean onServer;
    @Final
    @Shadow private  PlayerEntity owner;
    @Shadow public abstract int getCraftingResultSlotIndex();

    @Shadow @Final private RecipeInputInventory craftingInput;

    public PlayerScreenHandlerMixin(ScreenHandlerType<?> screenHandlerType, int i) {
        super(screenHandlerType, i);
    }

//    @Inject(at = @At("HEAD"), method = "onContentChanged", cancellable = true)
//    public void onContentChanged(Inventory inventory, CallbackInfo ca) {

    @Inject(method = "onContentChanged",at = @At(value = "HEAD"),cancellable = true)
    public void onContentChanged(Inventory inventory, CallbackInfo ci) {
        ci.cancel();
        this.craftingResult.clear();
        if(this.onServer) {
            //确定4个输入物品的合成等级
            List<Integer> list = new ArrayList<>();

            for (int i = 0; i < 4; i++) {
                int craftLevel = 0;
                ItemStack itemStack = this.craftingInput.getStack(i);
                if (itemStack.isIn(ModItemTags.CRAFT_LEVEL1))
                    craftLevel = 1;
                else if (itemStack.isIn(ModItemTags.CRAFT_LEVEL2))
                    craftLevel = 2;
                else if (itemStack.isIn(ModItemTags.CRAFT_LEVEL3))
                    craftLevel = 3;
                else if (itemStack.isIn(ModItemTags.CRAFT_LEVEL4))
                    craftLevel = 4;
                else if (itemStack.isIn(ModItemTags.CRAFT_LEVEL5))
                    craftLevel = 5;
                else
                    craftLevel = 0;
                list.add(craftLevel);
            }
            int maxCraftLevel = Collections.max(list);

            //是否在合成工作台?
            ItemStack itemStack0 = this.craftingInput.getStack(0);ItemStack itemStack1 = this.craftingInput.getStack(1);
            ItemStack itemStack2 = this.craftingInput.getStack(2);ItemStack itemStack3 = this.craftingInput.getStack(3);


            boolean condition1 = itemStack0.isOf(Tools.FLINT_KNIFE)&&itemStack2.isIn(ItemTags.LOGS);
            boolean condition2 = itemStack1.isOf(Tools.FLINT_KNIFE)&&itemStack3.isIn(ItemTags.LOGS);
            boolean isCraftTable =condition1||condition2;

            if (maxCraftLevel > 0 && !isCraftTable) {
            //什么都不做,不更新
            } else
                CraftingScreenHandler.updateResult(this, this.owner.getWorld(), this.owner, this.craftingInput, this.craftingResult, null);
        }
    }

}






///**
// * @author
// * @reason
// */
//@Overwrite
//    public void onContentChanged(Inventory inventory) {
//        if (this.onServer) {
//            //输出物品
//            CraftingRecipeInput craftingRecipeInput = craftingInput.createRecipeInput();
//            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)owner;
//            ItemStack itemStack = ItemStack.EMPTY;
//            Optional<RecipeEntry<CraftingRecipe>> optional = serverPlayerEntity.getWorld().getServer().getRecipeManager().getFirstMatch(RecipeType.CRAFTING, craftingRecipeInput, serverPlayerEntity.getWorld(),(RecipeEntry)null);
//            if (optional.isPresent()) {
//                RecipeEntry<CraftingRecipe> recipeEntry = (RecipeEntry)optional.get();
//                CraftingRecipe craftingRecipe = (CraftingRecipe)recipeEntry.value();
//                if (this.craftingResult.shouldCraftRecipe(serverPlayerEntity.getWorld(), serverPlayerEntity, recipeEntry)) {
//                    itemStack = craftingRecipe.craft(craftingRecipeInput,owner.getWorld().getRegistryManager());
//                }
//            }
//
//            Item item = itemStack.getItem();
//            String name = Registries.ITEM.getId(item).toString();
//
//
//            if(item != BlockInit.FLINT_CRAFTING_TABLE.asItem() && count > 0 || item==Items.ENCHANTED_GOLDEN_APPLE
//
//                this.craftingResult.setStack(0, ItemStack.EMPTY);
//            } else {
//                CraftingScreenHandler.updateResult(this, this.owner.getWorld(), this.owner, ((PlayerScreenHandlerAccessor)(Object)this).getCraftingInput(), this.craftingResult, (RecipeEntry)null);
//            }
//        }





