package com.equilibrium.client.render.entity;

import com.equilibrium.client.render.entity.model.TransparentZombieEntityModelAbstractZombieModel;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.Identifier;

import static com.equilibrium.MITEequilibrium.MOD_ID;

public class InvisibleStalkerModel extends AbstractTransparentZombieEntityRenderer<ZombieEntity, TransparentZombieEntityModelAbstractZombieModel<ZombieEntity>> {
    private static final Identifier TEXTURE = Identifier.of(MOD_ID,"textures/entity/invisible_stalker.png");

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
    //该生物不渲染碰撞箱:见EntityRenderDispatcherMixinForRenderHitBox
}

