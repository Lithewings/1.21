package com.equilibrium.client.render.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.entity.mob.ZombieEntity;

@Environment(EnvType.CLIENT)
public class TransparentZombieEntityModelAbstractZombieModel<T extends ZombieEntity> extends TransparentAbstractZombieModel<T> {
    public TransparentZombieEntityModelAbstractZombieModel(ModelPart modelPart) {
        super(modelPart);
    }

    public boolean isAttacking(T zombieEntity) {
        return zombieEntity.isAttacking();
    }
}
