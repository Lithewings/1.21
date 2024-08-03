package com.equilibrium.mixin.tables;

import com.equilibrium.craft_time_register.BlockInit;
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
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

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
/**
 * @author
 * @reason
 */
@Overwrite
    public void onContentChanged(Inventory inventory) {
        if (this.onServer) {
            //输出物品
            CraftingRecipeInput craftingRecipeInput = craftingInput.createRecipeInput();
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)owner;
            ItemStack itemStack = ItemStack.EMPTY;
            Optional<RecipeEntry<CraftingRecipe>> optional = serverPlayerEntity.getWorld().getServer().getRecipeManager().getFirstMatch(RecipeType.CRAFTING, craftingRecipeInput, serverPlayerEntity.getWorld(),(RecipeEntry)null);
            if (optional.isPresent()) {
                RecipeEntry<CraftingRecipe> recipeEntry = (RecipeEntry)optional.get();
                CraftingRecipe craftingRecipe = (CraftingRecipe)recipeEntry.value();
                if (this.craftingResult.shouldCraftRecipe(serverPlayerEntity.getWorld(), serverPlayerEntity, recipeEntry)) {
                    itemStack = craftingRecipe.craft(craftingRecipeInput,owner.getWorld().getRegistryManager());
                }
            }

            Item item = itemStack.getItem();
            String name = Registries.ITEM.getId(item).toString();
            //输入物品
            int count = 0;
            int countHay = 0;

            int i;
            int j;
            for(i = 0; i < 2; ++i) {
                for(j = 0; j < 2; ++j) {
                    Item inputItem = ((PlayerScreenHandlerAccessor)(Object)this).getCraftingInput().getStack(j + i * 2).getItem();
//                    String inputItemName = Registry.ITEM.getId(inputItem).toString();
                    //输入物品检测：铜，铁，金，红石，绿宝石，青金石，钻石，下界合金，黑曜石
                    if(inputItem== Items.COPPER_INGOT || inputItem== Items.IRON_INGOT || inputItem== Items.IRON_BLOCK
                            || inputItem== Items.GOLD_INGOT || inputItem== Items.GOLD_BLOCK
                            || inputItem== Items.REDSTONE || inputItem== Items.REDSTONE_BLOCK || inputItem== Items.OBSIDIAN
                            || inputItem== Items.EMERALD || inputItem== Items.EMERALD_BLOCK || inputItem== Items.LAPIS_LAZULI || inputItem== Items.LAPIS_BLOCK
                            || inputItem== Items.DIAMOND || inputItem== Items.DIAMOND_BLOCK
                            || inputItem== Items.NETHERITE_INGOT || inputItem== Items.NETHERITE_BLOCK
                            || inputItem instanceof BlockItem blockItem && blockItem.getBlock() instanceof PillarBlock && (Registries.ITEM.getId(inputItem).toString().contains("_log") || Registries.ITEM.getId(inputItem).toString().contains("_stem"))
                            || inputItem instanceof BlockItem && (Registries.ITEM.getId(inputItem).toString().contains("_log") || Registries.ITEM.getId(inputItem).toString().contains("_stem") || Registries.ITEM.getId(inputItem).toString().contains("_wood"))
                    ){
                        count++;
                    }

                    if(inputItem == Items.HAY_BLOCK){
                        countHay++;
                    }
                }
            }

            long time = this.owner.getWorld().getLevelProperties().getTimeOfDay();
            int day = (int)(time / 24000L)+1;
            int structureUnderDay;
            if(FabricLoader.getInstance().isModLoaded("aliveandwell")){
                structureUnderDay = 48;
            }else {
                structureUnderDay = 1;
            }


            if(item != BlockInit.FLINT_CRAFTING_TABLE.asItem() && count > 0 || item==Items.ENCHANTED_GOLDEN_APPLE
                    || name.contains("aliveandwell:enchanted_golden_carrot")
                    || day<=structureUnderDay && (name.equals("soulsweapons:boss_compass")
                    || name.equals("bosses_of_mass_destruction:void_lily"))){
                this.craftingResult.setStack(0, ItemStack.EMPTY);
            }else if(countHay>0 && item == Items.WHEAT){
                this.craftingResult.setStack(0, ItemStack.EMPTY);
            } else {
                CraftingScreenHandler.updateResult(this, this.owner.getWorld(), this.owner, ((PlayerScreenHandlerAccessor)(Object)this).getCraftingInput(), this.craftingResult, (RecipeEntry)null);
            }
        }
    }
}
