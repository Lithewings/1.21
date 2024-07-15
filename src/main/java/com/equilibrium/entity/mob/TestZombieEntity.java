package com.equilibrium.entity.mob;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.world.World;

public class TestZombieEntity extends ZombieEntity {
    public TestZombieEntity(EntityType<? extends TestZombieEntity> entityType, World world) {
        super(entityType, world);
    }

}


