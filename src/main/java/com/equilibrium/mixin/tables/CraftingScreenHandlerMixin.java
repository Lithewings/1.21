package com.equilibrium.mixin.tables;

import com.equilibrium.config.CommonConfig;
import com.equilibrium.register.BlockInit;
import com.equilibrium.mixin.crafttime.worklevel.CraftingIngredients;
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
import net.minecraft.screen.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public void onContentChanged(Inventory inventory) {
		this.context.run((world, pos) -> {
			CraftingRecipeInput craftingRecipeInput = input.createRecipeInput();
			ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)player;
			ItemStack itemStack = ItemStack.EMPTY;
			Optional<RecipeEntry<CraftingRecipe>> optional = world.getServer().getRecipeManager().getFirstMatch(RecipeType.CRAFTING, craftingRecipeInput, world,  (RecipeEntry)null);
			if (optional.isPresent()) {
				RecipeEntry<CraftingRecipe> recipeEntry = (RecipeEntry)optional.get();
				CraftingRecipe craftingRecipe = (CraftingRecipe)recipeEntry.value();
				if (this.result.shouldCraftRecipe(world, serverPlayerEntity, recipeEntry)) {
					itemStack = craftingRecipe.craft(craftingRecipeInput,world.getRegistryManager());
				}
			}
			Item item = itemStack.getItem();
			String name = Registries.ITEM.getId(item).toString();
			int level = 0;

			int count2 = 0;
			int count3 = 0;
			int count4 = 0;
			int count5 = 0;

			//输出结果检测
			for (String name1 : CraftingIngredients.mod_ingredients.keySet()){
				if(name1.equals(name)){
					level = CraftingIngredients.mod_ingredients.get(name1).workLevel;
				}


				if(item instanceof BlockItem blockItem){
					if(blockItem.getBlock().getDefaultState().isIn(BlockTags.LOGS) || blockItem.getBlock().getDefaultState().isIn(BlockTags.PLANKS)){
						level = 1;
					}
				}
				if(name.contains("doom:") && name.contains("argent_block")){
					level = 3;
				}
				if(name.contains("doom:") && name.contains("argent_energy")){
					level = 3;
				}
				if(name.contains("doom:") && name.contains("argent_plate")){
					level = 5;
				}
				if(name.contains("doom:") && name.contains("helmet")){
					level = 5;
				}
				if(name.contains("doom:") && name.contains("chestplate")){
					level = 5;
				}
				if(name.contains("doom:") && name.contains("leggings")){
					level = 5;
				}
				if(name.contains("doom:") && name.contains("boots")){
					level = 5;
				}
				if(name.contains("aliveandwell:") ){
					if(name.contains("mithril") ){
						level = 4;
					}
				}
				if(name.contains("aliveandwell:") ){
					if(name.contains("adamantium") ){
						level = 5;
					}
				}

				//输入物品
				int i;
				int j;
				for(i = 0; i < 3; ++i) {
					for(j = 0; j < 3; ++j) {
						Item inputItem = this.input.getStack(j + i * 3).getItem();
						String inputItemName = Registries.ITEM.getId(inputItem).toString();

						//输入物品检测：一级除了“铜工作台”，包含铜为2
						if((inputItem== Items.COPPER_INGOT )){//!name.equals("aliveandwell:copper_crafting_table") &&
							count2++;//二级物品（铜工作台）2
						}
						//输入物品检测：二级除了“铁工作台”，包含铁、金、红石的为3   !name.equals("aliveandwell:iron_crafting_table") &&
						if( (inputItem== Items.EMERALD_BLOCK || inputItem== Items.EMERALD || inputItem== Items.IRON_INGOT || inputItem== Items.IRON_BLOCK || inputItem== Items.IRON_AXE || inputItem== Items.IRON_PICKAXE || inputItem== Items.IRON_SWORD || inputItem== Items.IRON_HOE || inputItem== Items.IRON_SHOVEL|| inputItem== Items.IRON_BOOTS || inputItem== Items.IRON_CHESTPLATE || inputItem== Items.IRON_HELMET || inputItem== Items.IRON_LEGGINGS
								|| inputItem== Items.GOLD_INGOT || inputItem== Items.GOLD_BLOCK || inputItem== Items.GOLDEN_AXE || inputItem== Items.GOLDEN_PICKAXE || inputItem== Items.GOLDEN_SWORD || inputItem== Items.GOLDEN_HOE || inputItem== Items.GOLDEN_SHOVEL|| inputItem== Items.GOLDEN_BOOTS || inputItem== Items.GOLDEN_CHESTPLATE || inputItem== Items.GOLDEN_HELMET || inputItem== Items.GOLDEN_LEGGINGS
								|| inputItem== Items.REDSTONE || inputItem== Items.REDSTONE_BLOCK || inputItem== Items.OBSIDIAN
								|| (inputItemName.contains("ingot") && !inputItemName.contains("steel_ingot") && !inputItemName.contains("minecraft:") && !inputItemName.contains("flint_ingot"))
								|| (inputItemName.contains("doom:") && inputItemName.contains("argent_block"))
								|| (inputItemName.contains("doom:") && inputItemName.contains("argent_energy"))
						)){
							count3++;//三级物品（铁工作台）3
						}
						//输入物品检测：三级除了“钻石工作台”，包含绿宝石、青金石、钻石的为4  !name.equals("aliveandwell:diamond_crafting_table") && item != Items.ENCHANTING_TABLE  &&
						if( ( inputItem== Items.LAPIS_LAZULI || inputItem== Items.LAPIS_BLOCK || inputItemName.contains("item_en_gemstone")
								|| inputItem== Items.DIAMOND || inputItem== Items.DIAMOND_BLOCK || inputItem== Items.DIAMOND_AXE || inputItem== Items.DIAMOND_PICKAXE || inputItem== Items.DIAMOND_SWORD || inputItem== Items.DIAMOND_HOE || inputItem== Items.DIAMOND_SHOVEL|| inputItem== Items.DIAMOND_BOOTS || inputItem== Items.DIAMOND_CHESTPLATE || inputItem== Items.DIAMOND_HELMET || inputItem== Items.DIAMOND_LEGGINGS
								|| inputItem== Items.AMETHYST_SHARD || inputItem== Items.AMETHYST_BLOCK || inputItem== Items.CRYING_OBSIDIAN
								|| inputItemName.contains("steel_ingot")
						)){
							count4++;//四级物品（钻石工作台）4
						}
						//输入物品检测：四级除了“下界合金工作台”，包含下界合金的为5  !name.equals("aliveandwell:netherite_crafting_table") &&
						if( (inputItem== Items.NETHERITE_INGOT
								|| inputItem== Items.NETHERITE_BLOCK || inputItem== Items.NETHERITE_AXE
								|| inputItem== Items.NETHERITE_PICKAXE || inputItem== Items.NETHERITE_SWORD
								|| inputItem== Items.NETHERITE_HOE || inputItem== Items.NETHERITE_SHOVEL
								|| inputItem== Items.NETHERITE_BOOTS || inputItem== Items.NETHERITE_CHESTPLATE
								|| inputItem== Items.NETHERITE_HELMET || inputItem== Items.NETHERITE_LEGGINGS
								|| (inputItemName.contains("doom:") && inputItemName.contains("argent_plate")))
						){
							count5++;//五级物品（下界合金工作台）5
						}


						if(CommonConfig.itemCrafttableLevelMap.containsKey(inputItemName)){
							int levelConfig = CommonConfig.itemCrafttableLevelMap.get(inputItemName);
							if(levelConfig ==2){
								count2++;
							}
							if(levelConfig ==3){
								count3++;
							}
							if(levelConfig ==4){
								count4++;
							}
							if(levelConfig ==5){
								count5++;
							}
						}
					}
				}

			}

			if(item==Items.ENCHANTED_GOLDEN_APPLE || name.contains("aliveandwell:enchanted_golden_carrot")){
				return;
			}
			if(name.contains("aliveandwell:flint_and_steel")){
				updateResult(this, world, this.player, this.input, this.result, (RecipeEntry)null);
				return;
			}

			//禁用配方：甘草块-小麦
			if(item == Items.WHEAT){
				return;
			}


			long time = world.getLevelProperties().getTimeOfDay();
			int day = (int)(time / 24000L)+1;
			int structureUnderDay;
			if(FabricLoader.getInstance().isModLoaded("aliveandwell")){
				structureUnderDay = 48;
			}else {
				structureUnderDay = 1;
			}

			if(world.getBlockState(pos).getBlock() == BlockInit.FLINT_CRAFTING_TABLE){
				if(name.equals("crafttime:copper_crafting_table")){
					updateResult(this, world, this.player, this.input, this.result, (RecipeEntry)null);
				}else if(name.equals("minecraft:furnace")){
					this.result.setStack(0, ItemStack.EMPTY);
					serverPlayerEntity.sendMessage(Text.translatable("aliveandwell.crafttable.info1").formatted(Formatting.YELLOW,Formatting.BOLD));
					return;
				}else if(name.equals("soulsweapons:sting") || name.equals("soulsweapons:guts_sword") || name.equals("soulsweapons:holy_greatsword") || name.equals("soulsweapons:kirkhammer") || name.equals("soulsweapons:moonstone_axe") || name.equals("soulsweapons:moonstone_hoe")|| name.equals("soulsweapons:moonstone_pickaxe") || name.equals("soulsweapons:moonstone_shovel")){
					this.result.setStack(0, ItemStack.EMPTY);
					serverPlayerEntity.sendMessage(Text.translatable("aliveandwell.crafttable.info3").formatted(Formatting.YELLOW,Formatting.BOLD));
					return;
				}else if(day<=structureUnderDay && (name.equals("soulsweapons:boss_compass")|| name.equals("bosses_of_mass_destruction:void_lily"))){
					this.result.setStack(0, ItemStack.EMPTY);
					serverPlayerEntity.sendMessage(Text.translatable("aliveandwell.crafttable.info2").formatted(Formatting.YELLOW,Formatting.BOLD));
					return;
				}else if(level > 1 || count2>0 || count3>0 || count4>0 || count5>0){
					this.result.setStack(0, ItemStack.EMPTY);
					serverPlayerEntity.sendMessage(Text.translatable("aliveandwell.crafttable.info1").formatted(Formatting.YELLOW,Formatting.BOLD));
					return;
				}
			}
			if(world.getBlockState(pos).getBlock() == BlockInit.COPPER_CRAFTING_TABLE){
				if(name.equals("crafttime:iron_crafting_table")){
					updateResult(this, world, this.player, this.input, this.result, (RecipeEntry)null);
				}else if(name.equals("soulsweapons:sting") || name.equals("soulsweapons:guts_sword") || name.equals("soulsweapons:holy_greatsword") || name.equals("soulsweapons:kirkhammer") || name.equals("soulsweapons:moonstone_axe") || name.equals("soulsweapons:moonstone_hoe")|| name.equals("soulsweapons:moonstone_pickaxe") || name.equals("soulsweapons:moonstone_shovel")){
					this.result.setStack(0, ItemStack.EMPTY);
					serverPlayerEntity.sendMessage(Text.translatable("aliveandwell.crafttable.info3").formatted(Formatting.YELLOW,Formatting.BOLD));
					return;
				}else if(day<=structureUnderDay && (name.equals("soulsweapons:boss_compass")|| name.equals("bosses_of_mass_destruction:void_lily"))){
					this.result.setStack(0, ItemStack.EMPTY);
					serverPlayerEntity.sendMessage(Text.translatable("aliveandwell.crafttable.info2").formatted(Formatting.YELLOW,Formatting.BOLD));
					return;
				}else if(level > 2 || count3>0 || count4>0 || count5>0){
					this.result.setStack(0, ItemStack.EMPTY);
					serverPlayerEntity.sendMessage(Text.translatable("aliveandwell.crafttable.info1").formatted(Formatting.YELLOW,Formatting.BOLD));
					return;
				}
			}
			if(world.getBlockState(pos).getBlock() == BlockInit.IRON_CRAFTING_TABLE){
				if(name.equals("crafttime:diamond_crafting_table") || item == Items.ENCHANTING_TABLE){
					updateResult(this, world, this.player, this.input, this.result, (RecipeEntry)null);
					return;
				}else if(name.equals("soulsweapons:sting") || name.equals("soulsweapons:guts_sword") || name.equals("soulsweapons:holy_greatsword") || name.equals("soulsweapons:kirkhammer") || name.equals("soulsweapons:moonstone_axe") || name.equals("soulsweapons:moonstone_hoe") || name.equals("soulsweapons:moonstone_pickaxe") || name.equals("soulsweapons:moonstone_shovel")){
					this.result.setStack(0, ItemStack.EMPTY);
					serverPlayerEntity.sendMessage(Text.translatable("aliveandwell.crafttable.info3").formatted(Formatting.YELLOW,Formatting.BOLD));
					return;
				}else if(day<=structureUnderDay && (name.equals("soulsweapons:boss_compass")|| name.equals("bosses_of_mass_destruction:void_lily"))){
					this.result.setStack(0, ItemStack.EMPTY);
					serverPlayerEntity.sendMessage(Text.translatable("aliveandwell.crafttable.info2").formatted(Formatting.YELLOW,Formatting.BOLD));
					return;
				}
				else if(level > 3 || count4>0 || count5>0){
					this.result.setStack(0, ItemStack.EMPTY);
					serverPlayerEntity.sendMessage(Text.translatable("aliveandwell.crafttable.info1").formatted(Formatting.YELLOW,Formatting.BOLD));
					return;
				}
			}
			if(world.getBlockState(pos).getBlock() == BlockInit.DIAMOND_CRAFTING_TABLE){
				if(name.equals("crafttime:netherite_crafting_table")){
					updateResult(this, world, this.player, this.input, this.result, (RecipeEntry)null);
				}else if(name.equals("soulsweapons:sting") || name.equals("soulsweapons:guts_sword") || name.equals("soulsweapons:holy_greatsword") || name.equals("soulsweapons:kirkhammer") || name.equals("soulsweapons:moonstone_axe") || name.equals("soulsweapons:moonstone_hoe")|| name.equals("soulsweapons:moonstone_pickaxe") || name.equals("soulsweapons:moonstone_shovel")){
					this.result.setStack(0, ItemStack.EMPTY);
					serverPlayerEntity.sendMessage(Text.translatable("aliveandwell.crafttable.info3").formatted(Formatting.YELLOW,Formatting.BOLD));
					return;
				}else if(day<=structureUnderDay && (name.equals("soulsweapons:boss_compass")|| name.equals("bosses_of_mass_destruction:void_lily"))){
					this.result.setStack(0, ItemStack.EMPTY);
					serverPlayerEntity.sendMessage(Text.translatable("aliveandwell.crafttable.info2").formatted(Formatting.YELLOW,Formatting.BOLD));
					return;
				}else if(level > 4 || count5>0){
					this.result.setStack(0, ItemStack.EMPTY);
					serverPlayerEntity.sendMessage(Text.translatable("aliveandwell.crafttable.info1").formatted(Formatting.YELLOW,Formatting.BOLD));
					return;
				}
			}

			if(world.getBlockState(pos).getBlock() == BlockInit.NETHERITE_CRAFTING_TABLE){
				if(name.equals("soulsweapons:sting") || name.equals("soulsweapons:guts_sword") || name.equals("soulsweapons:holy_greatsword") || name.equals("soulsweapons:kirkhammer") || name.equals("soulsweapons:moonstone_axe") || name.equals("soulsweapons:moonstone_hoe")|| name.equals("soulsweapons:moonstone_pickaxe") || name.equals("soulsweapons:moonstone_shovel")){
					this.result.setStack(0, ItemStack.EMPTY);
					serverPlayerEntity.sendMessage(Text.translatable("aliveandwell.crafttable.info3").formatted(Formatting.YELLOW,Formatting.BOLD));
					return;
				}else if(day<=structureUnderDay && (name.equals("soulsweapons:boss_compass")|| name.equals("bosses_of_mass_destruction:void_lily"))){
					this.result.setStack(0, ItemStack.EMPTY);
					serverPlayerEntity.sendMessage(Text.translatable("aliveandwell.crafttable.info2").formatted(Formatting.YELLOW,Formatting.BOLD));
					return;
				}
			}

			updateResult(this, world, this.player, this.input, this.result, (RecipeEntry)null);
		});
	}

	@Shadow
    public static void updateResult(ScreenHandler handler, World world, PlayerEntity player, RecipeInputInventory craftingInventory, CraftingResultInventory resultInventory, @Nullable RecipeEntry<CraftingRecipe> recipe) {
		if (!world.isClient) {
			CraftingRecipeInput craftingRecipeInput = craftingInventory.createRecipeInput();
			ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)player;
			ItemStack itemStack = ItemStack.EMPTY;
			Optional<RecipeEntry<CraftingRecipe>> optional = world.getServer().getRecipeManager().getFirstMatch(RecipeType.CRAFTING, craftingRecipeInput, world,recipe);
			if (optional.isPresent()) {
				RecipeEntry<CraftingRecipe> recipeEntry = (RecipeEntry)optional.get();
				CraftingRecipe craftingRecipe = (CraftingRecipe)recipeEntry.value();
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
}
