package com.equilibrium;

import net.minecraft.item.ItemStack;

public interface ITimeCraftPlayer {

	void craftTime$setCrafting(boolean is_crafting);

	boolean craftTime$isCrafting();

	void craftTime$setCraftTime(float craft_time);

	float craftTime$getCraftTime();

	void craftTime$setCraftPeriod(float craft_period);

	float craftTime$getCraftPeriod();
	
	void craftTime$stopCraft();
	
	void craftTime$startCraftWithNewPeriod(float craft_period);

	boolean craftTime$tick(ItemStack resultStack);

}
