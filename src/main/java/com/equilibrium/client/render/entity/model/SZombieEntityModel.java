package com.equilibrium.client.render.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AbstractZombieModel;
import net.minecraft.entity.mob.ZombieEntity;

@Environment(EnvType.CLIENT)
public class SZombieEntityModel<T extends ZombieEntity> extends TransparentAbstractZombieModel<T> {
    public SZombieEntityModel(ModelPart modelPart) {
        super(modelPart);

    }
    public boolean isAttacking(T zombieEntity) {
        return zombieEntity.isAttacking();
    }

}
