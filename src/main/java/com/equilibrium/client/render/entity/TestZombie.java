package com.equilibrium.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.ZombieEntityModel;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class TestZombie extends AbstractZombieEntityRenderer<ZombieEntity, ZombieEntityModel<ZombieEntity>> {


    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/player/wide/steve.png");
    public Identifier getTexture(ZombieEntity zombieEntity) {
        return TEXTURE;
    }
    public TestZombie(EntityRendererFactory.Context context) {
        this(context, EntityModelLayers.ZOMBIE, EntityModelLayers.ZOMBIE_INNER_ARMOR, EntityModelLayers.ZOMBIE_OUTER_ARMOR);

    }
    public TestZombie(EntityRendererFactory.Context ctx, EntityModelLayer layer, EntityModelLayer legsArmorLayer, EntityModelLayer bodyArmorLayer) {
        super(
                ctx, new ZombieEntityModel<>(ctx.getPart(layer)), new ZombieEntityModel<>(ctx.getPart(legsArmorLayer)), new ZombieEntityModel<>(ctx.getPart(bodyArmorLayer))
        );
    }

}
