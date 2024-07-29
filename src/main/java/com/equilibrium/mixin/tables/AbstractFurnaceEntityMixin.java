package com.equilibrium.mixin.tables;

import com.equilibrium.config.CommonConfig;

import com.equilibrium.register.BlockInit;
import com.equilibrium.worklevel.FurnaceIngredients;
import com.google.common.collect.Maps;
import net.minecraft.SharedConstants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Util;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.Map;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceEntityMixin extends BlockEntity {

    @Unique
    int level0 = 0;//物品
    @Unique
    int level = 0;//燃料
    @Shadow
    int burnTime;

    @Shadow
    protected abstract boolean isBurning();

    @Shadow
    protected DefaultedList<ItemStack> inventory;

    private AbstractFurnaceEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    @Inject(at = @At("RETURN"), method = "<init>")
    public void Constructor(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, RecipeType<? extends AbstractCookingRecipe> recipeType, CallbackInfo info) {

    }

    @Inject(at = @At("HEAD"), method = "getFuelTime", cancellable = true)
    public void getFuelTime(ItemStack fuel, CallbackInfoReturnable<Integer> ca) {
        //此处world必须判断是否为null，否则熔炉数据无法保存。
        if (this.getWorld() != null) {
            Block block = this.world.getBlockState(this.pos).getBlock();

            //物品
            ItemStack itemStack = (ItemStack) this.inventory.get(0);
            Item item0 = itemStack.getItem();
            String name0 = Registries.ITEM.getId(item0).toString();
            //燃料
            Item item = fuel.getItem();
            String name = Registries.ITEM.getId(item).toString();
            level0 = 0;//物品
            level = 0;//燃料

            if(FurnaceIngredients.modItem_ingredients.keySet().contains(name0)){
                level0 = FurnaceIngredients.modItem_ingredients.get(name0).workLevel;
            }else {
                //不是原版的矿石为一级，可能不准确
                if (name0.contains("ore") && !name0.contains("deepslate") && !name0.contains("minecraft")) {
                    level0 = 1;
                }else  if (name0.contains("aliveandwell") && name0.contains("deep_raw") && !name0.contains("minecraft:")) {
                    level0 = 2;
                }
                else if (name0.contains("raw_copper") && name0.contains("minecraft:")) {
                    level0 = 1;
                }
                else if (name0.contains("raw_iron") && name0.contains("minecraft:")) {
                    level0 = 1;
                }
                else if (name0.contains("raw_gold") && name0.contains("minecraft:")) {
                    level0 = 1;
                }
                else if (name0.contains("iron_block") || name0.contains("copper_block")) {
                    level0 = 2;
                }
                else if (name0.contains("deepslate") && name0.contains("ore") && !name0.contains("minecraft")) {
                    level0 = 2;
                }
                else if (name0.equals("gobber2:gobber2_ore") ) {
                    level0 = 2;
                }
                else if (name0.equals("minecraft:iron_ingot") ) {
                    level0 = 2;
                }
                else if (name0.equals("gobber2:gobber2_ore_nether") ) {
                    level0 = 3;
                }
                else if (name0.contains("_mithril") && name0.contains("aliveandwell:")) {
                    level0 = 2;
                }
                else if (name0.contains("_adamantium") && name0.contains("aliveandwell:")) {
                    level0 = 3;
                }
                else if(CommonConfig.itemFurnaceLevelMap.containsKey(name0)){
                    level0 = CommonConfig.itemFurnaceLevelMap.get(name0);
                }

            }
//            //物品
//            for (String name01 : FurnaceIngredients.modItem_ingredients.keySet()) {
//                if (name01.equals(name0)) {
//                    level0 = FurnaceIngredients.modItem_ingredients.get(name01).workLevel;
//                }
//
//
//            }

            if(FurnaceIngredients.modFuel_ingredients.containsKey(name)){
                level = FurnaceIngredients.modFuel_ingredients.get(name).workLevel;
            } else if(name.contains("coal") && !name.contains("charcoal")){
                level = 1;
            } else level = CommonConfig.fuelItemLevelMap.getOrDefault(name, 0);

            if (name.contains("coal") && !name.contains("charcoal")) {
                ca.setReturnValue(801);
            }else if(!(item==Items.LAVA_BUCKET || item==Items.BLAZE_ROD) && !name.contains("minecraft")){
                ca.setReturnValue(150);//其他一切不是原版的燃料为150.
            }

            //燃料等级大于该熔炉等级不可燃烧:原版熔炉和高炉为一级熔炉
            if (block == BlockInit.CLAY_FURNACE) {
                //燃料等级不够，或者燃料等级小于物品等级
                if (level > 0 || level0 > level) {
                    if (this.isBurning()) {
                        this.burnTime = 0;
                    }
                    ca.setReturnValue(0);
                }
            }

            if (block == Blocks.FURNACE || block==Blocks.BLAST_FURNACE) {
                //燃料等级不够，或者燃料等级小于物品等级
                if (level > 1 || level0 > level) {
                    if (this.isBurning()) {
                        this.burnTime = 0;
                    }
                    ca.setReturnValue(0);
                }
            }
            //燃料等级《小于该熔炉等级》或《大于该熔炉等级》不可燃烧
            if (block == BlockInit.OBSIDIAN_FURNACE) {
                if (level > 2 || level0 > level) {
                    if (this.isBurning()) {
                        this.burnTime = 0;
                    }
                    ca.setReturnValue(0);
                }
            }
            //燃料等级小于该熔炉等级不可燃烧
            if (block == BlockInit.NETHERRACK_FURNACE) {
                if (level0 > level) {
                    if (this.isBurning()) {
                        this.burnTime = 0;
                    }
                    ca.setReturnValue(0);
                }
            }
//            //工业电炉无法燃烧戈伯矿
//            if(Registry.BLOCK.getId(block).toString().equals("modern_industrialization:bronze_furnace")
//            || Registry.BLOCK.getId(block).toString().equals("modern_industrialization:steel_furnace")
//            || Registry.BLOCK.getId(block).toString().equals("modern_industrialization:electric_furnace")){
//                if (name.equals("gobber2:gobber2_ore") || name.equals("gobber2:gobber2_ore_nether") || name.equals("minecraft:ancient_debris")) {
//                    ca.setReturnValue(0);
//                }
//            }

        }
    }

    @Inject(at = @At("HEAD"), method = "getCookTime", cancellable = true)
    private static void getCookTime(World world, AbstractFurnaceBlockEntity furnace, CallbackInfoReturnable<Integer> ca)  {
        Item item;
        if (world != null) {
            Block block = world.getBlockState(furnace.getPos()).getBlock();
            AbstractFurnaceEntityMixin mixin = (AbstractFurnaceEntityMixin) (Object) furnace;
            //物品
            ItemStack itemStack0 = (ItemStack) mixin.inventory.get(0);
            Item item0 = itemStack0.getItem();
            String name0 = Registries.ITEM.getId(item0).toString();

            //燃料
            ItemStack itemStack = mixin.inventory.get(1);
            item = itemStack.getItem();
            String name = Registries.ITEM.getId(item).toString();

            int time =160;

            if (itemStack0.get(DataComponentTypes.FOOD) != null) {
                time = 99;
            }

            //不是原版的矿石为一级，可能不准确
            if (name0.contains("_ore") && !name0.contains("deepslate")) {
                time=200;//煤800
            }else if (name0.contains("aliveandwell") && name0.contains("deep_raw") && !name0.contains("minecraft:")) {
                time=802; //煤801
            }
            else if (name0.contains("iron_block") || name0.contains("copper_block")) {
                time=802; //煤801
            }
            else if (name0.contains("deepslate") && name0.contains("_ore")) {
                time=802; //煤801
            }
            else  if (name0.equals("minecraft:iron_ingot")) {
                time=802; //煤801
            }
            else if (name0.contains("minecraft:ancient_debris")) {
                time=8010; //烈焰棒10000
            }
            else if (name0.contains("gobber2_ore") && name0.contains("gobber2:")) {
                time=8022;//岩浆桶1800
            }
            else if (name0.contains("_mithril") && name0.contains("aliveandwell:")) {
                time=802;//煤801
            }
            else if (name0.contains("_adamantium") && name0.contains("aliveandwell:")) {
                time=8022;//岩浆桶1800
            }
            else if(CommonConfig.itemCooktimeMap.containsKey(name0)){
                time = CommonConfig.itemCooktimeMap.get(name0);
            }


            //燃料等级大于该熔炉等级不可燃烧
            if (block == Blocks.FURNACE || block == BlockInit.CLAY_FURNACE) {
                ca.setReturnValue(time);
            }
            //燃料等级《小于该熔炉等级》或《大于该熔炉等级》不可燃烧
            if (block == BlockInit.OBSIDIAN_FURNACE) {
                ca.setReturnValue(time/5);
            }
            //燃料等级小于该熔炉等级不可燃烧
            if (block == BlockInit.NETHERRACK_FURNACE) {
                ca.setReturnValue(time/10);
            }

//            //工业电炉无法燃烧戈伯矿
//            if(Registry.BLOCK.getId(block).toString().equals("modern_industrialization:bronze_furnace")
//                    || Registry.BLOCK.getId(block).toString().equals("modern_industrialization:steel_furnace")
//                    || Registry.BLOCK.getId(block).toString().equals("modern_industrialization:electric_furnace")){
//                if (name.equals("gobber2:gobber2_ore") || name.equals("gobber2:gobber2_ore_nether") || name.equals("minecraft:ancient_debris")) {
//                    ca.setReturnValue(0);
//                }
//            }
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static Map<Item, Integer> createFuelTimeMap() {
        Map<Item, Integer> map = Maps.newLinkedHashMap();
        addFuel(map, (ItemConvertible) Items.LAVA_BUCKET, 8021);
        addFuel(map, (ItemConvertible) Items.BLAZE_ROD, 80221);
//        addFuel(map, (ItemConvertible) Blocks.COAL_BLOCK, 800);
        addFuel(map, (ItemConvertible) Items.COAL, 801);
        addFuel(map, (ItemConvertible) Items.CHARCOAL, 199);
        addFuel(map, (ItemConvertible) Items.TORCH, 198);
        addFuel(map, (TagKey) ItemTags.LOGS, 150);
        addFuel(map, (TagKey) ItemTags.PLANKS, 150);
        addFuel(map, (TagKey) ItemTags.WOODEN_STAIRS, 150);
        addFuel(map, (TagKey) ItemTags.WOODEN_SLABS, 100);
        addFuel(map, (TagKey) ItemTags.WOODEN_TRAPDOORS, 150);
        addFuel(map, (TagKey) ItemTags.WOODEN_PRESSURE_PLATES, 150);
        addFuel(map, (ItemConvertible) Blocks.OAK_FENCE, 150);
        addFuel(map, (ItemConvertible) Blocks.BIRCH_FENCE, 150);
        addFuel(map, (ItemConvertible) Blocks.SPRUCE_FENCE, 150);
        addFuel(map, (ItemConvertible) Blocks.JUNGLE_FENCE, 150);
        addFuel(map, (ItemConvertible) Blocks.DARK_OAK_FENCE, 150);
        addFuel(map, (ItemConvertible) Blocks.ACACIA_FENCE, 150);
        addFuel(map, (ItemConvertible) Blocks.MANGROVE_FENCE, 150);
        addFuel(map, (ItemConvertible) Blocks.OAK_FENCE_GATE, 150);
        addFuel(map, (ItemConvertible) Blocks.BIRCH_FENCE_GATE, 150);
        addFuel(map, (ItemConvertible) Blocks.SPRUCE_FENCE_GATE, 150);
        addFuel(map, (ItemConvertible) Blocks.JUNGLE_FENCE_GATE, 150);
        addFuel(map, (ItemConvertible) Blocks.DARK_OAK_FENCE_GATE, 150);
        addFuel(map, (ItemConvertible) Blocks.ACACIA_FENCE_GATE, 150);
        addFuel(map, (ItemConvertible) Blocks.MANGROVE_FENCE_GATE, 150);
        addFuel(map, (ItemConvertible) Blocks.NOTE_BLOCK, 150);
        addFuel(map, (ItemConvertible) Blocks.BOOKSHELF, 150);
        addFuel(map, (ItemConvertible) Blocks.LECTERN, 150);
        addFuel(map, (ItemConvertible) Blocks.JUKEBOX, 150);
        addFuel(map, (ItemConvertible) Blocks.CHEST, 150);
        addFuel(map, (ItemConvertible) Blocks.TRAPPED_CHEST, 150);
        addFuel(map, (ItemConvertible) Blocks.CRAFTING_TABLE, 150);
        addFuel(map, (ItemConvertible) Blocks.DAYLIGHT_DETECTOR, 150);
        addFuel(map, (TagKey) ItemTags.BANNERS, 150);
        addFuel(map, (ItemConvertible) Items.BOW, 150);
        addFuel(map, (ItemConvertible) Items.FISHING_ROD, 150);
        addFuel(map, (ItemConvertible) Blocks.LADDER, 150);
        addFuel(map, (TagKey) ItemTags.SIGNS, 80);
        addFuel(map, (ItemConvertible) Items.WOODEN_SHOVEL, 80);
        addFuel(map, (ItemConvertible) Items.WOODEN_SWORD, 80);
        addFuel(map, (ItemConvertible) Items.WOODEN_HOE, 80);
        addFuel(map, (ItemConvertible) Items.WOODEN_AXE, 80);
        addFuel(map, (ItemConvertible) Items.WOODEN_PICKAXE, 80);
        addFuel(map, (TagKey) ItemTags.WOODEN_DOORS, 80);
        addFuel(map, (TagKey) ItemTags.BOATS, 180);
//        addFuel(map, (TagKey) ItemTags.WOOL, 150);
        addFuel(map, (TagKey) ItemTags.WOODEN_BUTTONS, 150);
        addFuel(map, (ItemConvertible) Items.STICK, 150);
        addFuel(map, (TagKey) ItemTags.SAPLINGS, 150);
        addFuel(map, (ItemConvertible) Items.BOWL, 150);
//        addFuel(map, (TagKey) ItemTags.WOOL_CARPETS, 67);
        addFuel(map, (ItemConvertible) Blocks.DRIED_KELP_BLOCK, 1001);
        addFuel(map, (ItemConvertible) Items.CROSSBOW, 150);
        addFuel(map, (ItemConvertible) Blocks.BAMBOO, 50);
        addFuel(map, (ItemConvertible) Blocks.DEAD_BUSH, 150);
        addFuel(map, (ItemConvertible) Blocks.SCAFFOLDING, 50);
        addFuel(map, (ItemConvertible) Blocks.LOOM, 150);
        addFuel(map, (ItemConvertible) Blocks.BARREL, 150);
        addFuel(map, (ItemConvertible) Blocks.CARTOGRAPHY_TABLE, 150);
        addFuel(map, (ItemConvertible) Blocks.FLETCHING_TABLE, 150);
        addFuel(map, (ItemConvertible) Blocks.SMITHING_TABLE, 150);
        addFuel(map, (ItemConvertible) Blocks.COMPOSTER, 150);
        addFuel(map, (ItemConvertible) Blocks.AZALEA, 150);
        addFuel(map, (ItemConvertible) Blocks.FLOWERING_AZALEA, 150);
        addFuel(map, (ItemConvertible) Blocks.MANGROVE_ROOTS, 150);
        return map;
    }

    @Shadow
    private static boolean isNonFlammableWood(Item item) {
        return item.getRegistryEntry().isIn(ItemTags.NON_FLAMMABLE_WOOD);
    }

    @Shadow
    private static void addFuel(Map<Item, Integer> fuelTimes, TagKey<Item> tag, int fuelTime) {
        Iterator var3 = Registries.ITEM.iterateEntries(tag).iterator();

        while (var3.hasNext()) {
            RegistryEntry<Item> registryEntry = (RegistryEntry) var3.next();
            if (!isNonFlammableWood((Item) registryEntry.value())) {
                fuelTimes.put((Item) registryEntry.value(), fuelTime);
            }
        }

    }

    @Shadow
    private static void addFuel(Map<Item, Integer> fuelTimes, ItemConvertible item, int fuelTime) {
        Item item2 = item.asItem();
        if (isNonFlammableWood(item2)) {
            if (SharedConstants.isDevelopment) {
                throw (IllegalStateException) Util.throwOrPause(new IllegalStateException("A developer tried to explicitly make fire resistant item " + item2.getName((ItemStack) null).getString() + " a furnace fuel. That will not work!"));
            }
        } else {
            fuelTimes.put(item2, fuelTime);
        }
    }
}

