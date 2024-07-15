package com.equilibrium.client.render.entity;

import com.equilibrium.client.render.entity.model.SZombieEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.ZombieEntityModel;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public abstract class TransparentZombieEntityRenderer<T extends ZombieEntity, M extends ZombieEntityModel<T>> extends BipedEntityRenderer<T, M> {


    protected TransparentZombieEntityRenderer(EntityRendererFactory.Context ctx, M bodyModel, M legsArmorModel, M bodyArmorModel) {
        super(ctx, bodyModel, 0.5F);
        this.addFeature(new ArmorFeatureRenderer<>(this, legsArmorModel, bodyArmorModel, ctx.getModelManager()));
    }


    protected boolean isShaking(T zombieEntity) {
        return super.isShaking(zombieEntity) || zombieEntity.isConvertingInWater();
    }
}
