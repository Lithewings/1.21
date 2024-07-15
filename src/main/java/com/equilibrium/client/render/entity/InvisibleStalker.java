package com.equilibrium.client.render.entity;

import com.equilibrium.client.render.entity.model.SZombieEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.ZombieEntityModel;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.Identifier;

public class InvisibleStalker extends TransparentZombieEntityRenderer<ZombieEntity, ZombieEntityModel<ZombieEntity>> {
    private static final Identifier TEXTURE = Identifier.of("miteequilibrium","textures/entity/invisible_stalker.png");

    public Identifier getTexture(ZombieEntity zombieEntity) {
        return TEXTURE;
    }

    public InvisibleStalker(EntityRendererFactory.Context context) {
        this(context, EntityModelLayers.ZOMBIE, EntityModelLayers.ZOMBIE_INNER_ARMOR, EntityModelLayers.ZOMBIE_OUTER_ARMOR);
    }

    public InvisibleStalker(EntityRendererFactory.Context ctx, EntityModelLayer layer, EntityModelLayer legsArmorLayer, EntityModelLayer bodyArmorLayer) {
        super(
                ctx, new ZombieEntityModel<>(ctx.getPart(layer)), new ZombieEntityModel<>(ctx.getPart(legsArmorLayer)), new ZombieEntityModel<>(ctx.getPart(bodyArmorLayer))
        );

    }





    }

