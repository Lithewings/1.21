package com.equilibrium.mixin.tables;

import com.equilibrium.config.CommonConfig;
import com.equilibrium.craft_time_register.BlockInit;
import com.equilibrium.craft_time_worklevel.CraftingIngredients;
import com.equilibrium.item.Metal;
import com.equilibrium.item.Tools;
import com.equilibrium.tags.ModItemTags;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Mixin(CraftingScreenHandler.class)
public abstract class CraftingScreenHandlerMixin extends AbstractRecipeScreenHandler<CraftingRecipeInput, CraftingRecipe> {
	@Final
	@Shadow
	private ScreenHandlerContext context;
	@Final
	@Shadow
    private CraftingResultInventory result;
	@Final
	@Shadow
	private RecipeInputInventory input;
	@Final
	@Shadow
	private  PlayerEntity player;






	public CraftingScreenHandlerMixin(ScreenHandlerType<?> screenHandlerType, int i) {
		super(screenHandlerType, i);
	}


	@Inject(at = @At("HEAD"), method = "canUse", cancellable = true)
	public void canUse(PlayerEntity player, CallbackInfoReturnable<Boolean> info) {
		context.run((world, blockPos) ->{
			if(world.getBlockState(blockPos).getBlock() instanceof CraftingTableBlock){
				info.setReturnValue(true);
			}
				});
		info.cancel();
	}


	@Shadow
	private boolean filling;






	@Inject(method = "onContentChanged",at = @At(value = "HEAD"), cancellable = true)
	public void onContentChanged(Inventory inventory, CallbackInfo ci) {
		ci.cancel();
		this.result.clear();

		if (!this.filling) {
			this.context.run((world, pos) -> {

				//确定合成台的合成等级
				int craftTableLevel = 0;
				if (world.getBlockState(pos).getBlock() == BlockInit.FLINT_CRAFTING_TABLE) {
					craftTableLevel = 1;
				} else if (world.getBlockState(pos).getBlock() == BlockInit.COPPER_CRAFTING_TABLE) {
					craftTableLevel = 2;
				} else if (world.getBlockState(pos).getBlock() == BlockInit.IRON_CRAFTING_TABLE) {
					craftTableLevel = 3;
				} else if (world.getBlockState(pos).getBlock() == BlockInit.DIAMOND_CRAFTING_TABLE) {
					craftTableLevel = 4;
				} else if (world.getBlockState(pos).getBlock() == BlockInit.NETHERITE_CRAFTING_TABLE) {
					craftTableLevel = 5;
				} else
					craftTableLevel = 0;


				//确定9个输入物品的合成等级

				List<Integer> list = new ArrayList<>();

				for (int i = 0; i < 10; i++) {
					int craftLevel = 0;
					ItemStack itemStack = this.input.getStack(i);
					//this.player.sendMessage(Text.of((itemStack).toString()));
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
				int maxCraftLevel =  Collections.max(list);

				boolean condition1=this.input.getStack(0).isIn(ModItemTags.CRAFT_TABLE)&&this.input.getStack(1).isOf(Items.LEATHER)&&this.input.getStack(3).isOf(Items.STICK)&&this.input.getStack(4).isIn(ItemTags.LOGS);
				boolean condition2=this.input.getStack(1).isIn(ModItemTags.CRAFT_TABLE)&&this.input.getStack(2).isOf(Items.LEATHER)&&this.input.getStack(4).isOf(Items.STICK)&&this.input.getStack(5).isIn(ItemTags.LOGS);
				boolean condition3=this.input.getStack(3).isIn(ModItemTags.CRAFT_TABLE)&&this.input.getStack(4).isOf(Items.LEATHER)&&this.input.getStack(6).isOf(Items.STICK)&&this.input.getStack(7).isIn(ItemTags.LOGS);
				boolean condition4=this.input.getStack(4).isIn(ModItemTags.CRAFT_TABLE)&&this.input.getStack(5).isOf(Items.LEATHER)&&this.input.getStack(7).isOf(Items.STICK)&&this.input.getStack(8).isIn(ItemTags.LOGS);
				//是否在合成工作台

				if(condition1||condition2||condition3||condition4)
					maxCraftLevel--;
				//等级是否合法?
				boolean isLevelValid=maxCraftLevel<=craftTableLevel;


				if(isLevelValid){
				}
				else
					return;


				updateResult(this, world, this.player, this.input, this.result, (RecipeEntry) null);
//				this.player.sendMessage(this.result.getStack(0).getName());
			});
		}
	}




	@Shadow
	protected static void updateResult(
			ScreenHandler handler,
			World world,
			PlayerEntity player,
			RecipeInputInventory craftingInventory,
			CraftingResultInventory resultInventory,
			@Nullable RecipeEntry<CraftingRecipe> recipe
	) {
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

			resultInventory.setStack(0, itemStack);
			handler.setPreviousTrackedSlot(0, itemStack);
			serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(handler.syncId, handler.nextRevision(), 0, itemStack));
		}
	}


	@Shadow public abstract boolean matches(RecipeEntry<CraftingRecipe> recipe);

	@Inject(method = "updateResult",at = @At(value = "HEAD"),cancellable = true)
	private static void updateResults(
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
					itemStack = Tools.IRON_SHOVEL.getDefaultStack();

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
