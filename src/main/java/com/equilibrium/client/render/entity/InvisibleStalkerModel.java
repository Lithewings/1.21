package com.equilibrium.client.render.entity;

import com.equilibrium.client.render.entity.model.TransparentZombieEntityModelAbstractZombieModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.Identifier;

public class InvisibleStalkerModel extends AbstractZombieEntityRenderer<ZombieEntity, TransparentZombieEntityModelAbstractZombieModel<ZombieEntity>> {
    private static final Identifier TEXTURE = Identifier.of("miteequilibrium","textures/entity/invisible_stalker.png");

    public Identifier getTexture(ZombieEntity zombieEntity) {
        return TEXTURE;
    }

    public InvisibleStalkerModel(EntityRendererFactory.Context context) {
        this( context , EntityModelLayers.ZOMBIE, EntityModelLayers.ZOMBIE_INNER_ARMOR, EntityModelLayers.ZOMBIE_OUTER_ARMOR);
    }

    public InvisibleStalkerModel(EntityRendererFactory.Context ctx, EntityModelLayer layer, EntityModelLayer legsArmorLayer, EntityModelLayer bodyArmorLayer) {
        super(
                ctx, new TransparentZombieEntityModelAbstractZombieModel<>(ctx.getPart(layer)),new TransparentZombieEntityModelAbstractZombieModel<>(ctx.getPart(layer)), new TransparentZombieEntityModelAbstractZombieModel<>(ctx.getPart(layer))
        );

    }





    }

