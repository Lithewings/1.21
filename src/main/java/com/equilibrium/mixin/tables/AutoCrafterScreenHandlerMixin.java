package com.equilibrium.mixin.tables;

import net.minecraft.block.CrafterBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.screen.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrafterScreenHandler.class)
public abstract class AutoCrafterScreenHandlerMixin  extends ScreenHandler implements ScreenHandlerListener {
    protected AutoCrafterScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }
@Shadow
    private final CraftingResultInventory resultInventory = new CraftingResultInventory();
    @Shadow
    @Final
    private PropertyDelegate propertyDelegate;
    @Shadow
    @Final
    private  PlayerEntity player;
    @Shadow
    @Final
    private RecipeInputInventory inputInventory;


    @Inject(method = "updateResult",at = @At("HEAD"),cancellable = true)
    private void updateResult(CallbackInfo ci) {
        ci.cancel();
        if (this.player instanceof ServerPlayerEntity serverPlayerEntity) {
            World world = serverPlayerEntity.getWorld();
            CraftingRecipeInput craftingRecipeInput = this.inputInventory.createRecipeInput();
            ItemStack itemStack = (ItemStack)CrafterBlock.getCraftingRecipe(world, craftingRecipeInput)
                    .map(recipeEntry -> ((CraftingRecipe)recipeEntry.value()).craft(craftingRecipeInput, world.getRegistryManager()))
                    .orElse(ItemStack.EMPTY);
            //继续加入别的禁用名单
            if(itemStack.isOf(Items.GOLDEN_APPLE))
                itemStack=ItemStack.EMPTY;
            this.resultInventory.setStack(0, itemStack);
        }
    }



}
