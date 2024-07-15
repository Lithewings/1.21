package com.equilibrium.entity.mob;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.AbstractZombieModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.CaveSpiderEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.world.World;

public class TestZombieEntity extends ZombieEntity {
    public TestZombieEntity(EntityType<? extends TestZombieEntity> entityType, World world) {
        super(entityType, world);
    }

}


