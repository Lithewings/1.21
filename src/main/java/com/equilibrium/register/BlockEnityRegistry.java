package com.equilibrium.register;


import com.equilibrium.MITEequilibrium;

import com.equilibrium.blockentity.TheFurnaceEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BlockEnityRegistry {

    public static BlockEntityType<TheFurnaceEntity> THE_FURNACE = register(
            "the_furnace",
            FabricBlockEntityTypeBuilder.create(TheFurnaceEntity::new,
                    BlockInit.getFurnaces().toArray(new Block[0])
            ).build(null));

    public static void init() {
    }

    private static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType<T> type) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(MITEequilibrium.MOD_ID, name), type);
    }
}
