package com.equilibrium.tags;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModBlockTags {




    public static final TagKey<Block> STONE_LIKE_240 = of("stone_like_240");
    public static final TagKey<Block> STONE_LIKE_360 = of("stone_like_360");
    public static final TagKey<Block> STONE_LIKE_480 = of("stone_like_480");

    public static final TagKey<Block> LOG_120 = of("log_like_120");

    public static final TagKey<Block> NORMAL_60 = of("normal_60");

    public static final TagKey<Block> NORMAL_30 = of("normal_30");


    //1级采集等级,定义为原版可以空手采集但这里不行的方块
    public static final TagKey<Block> HARVEST_ONE = of("block_harvest_1");
    public static final TagKey<Block> HARVEST_TWO = of("block_harvest_2");
    public static final TagKey<Block> HARVEST_THREE = of("block_harvest_3");
    public static final TagKey<Block> HARVEST_FOUR = of("block_harvest_4");
    public static final TagKey<Block> HARVEST_FIVE = of("block_harvest_5");


    public static final TagKey<Block> TRANSPARENT_FOR_ZOMBIE= of("transparent_for_zombie");

    public static final TagKey<Block> ORE = of("ore");



    private static TagKey<Block> of(String id) {
        return TagKey.of(RegistryKeys.BLOCK, Identifier.of("miteequilibrium",id));
    }


    public static void registerModBlockTags(){
    }

}
