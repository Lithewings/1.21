package com.equilibrium.client.render.entity.renderer;

import com.equilibrium.client.render.entity.model.TransparentZombieEntityModelAbstractZombieModel;
import com.equilibrium.client.render.entity.renderer.BipedEntityRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.mob.ZombieEntity;

@Environment(EnvType.CLIENT)
public abstract class AbstractTransparentZombieEntityRenderer<T extends ZombieEntity, M extends TransparentZombieEntityModelAbstractZombieModel<T>> extends BipedEntityRenderer<T, M> {


    protected AbstractTransparentZombieEntityRenderer(EntityRendererFactory.Context ctx, M bodyModel, M legsArmorModel, M bodyArmorModel) {
        super(ctx, bodyModel, 0.5F);

    }


    protected boolean isShaking(T zombieEntity) {
        return super.isShaking(zombieEntity) || zombieEntity.isConvertingInWater();
    }
}
