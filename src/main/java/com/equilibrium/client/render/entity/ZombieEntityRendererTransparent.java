package com.equilibrium.client.render.entity;

import com.equilibrium.client.render.entity.model.SZombieEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ZombieEntityRendererTransparent extends TransparentZombieEntityRenderer<ZombieEntity, SZombieEntityModel<ZombieEntity>> {


    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/player/wide/steve.png");
    public Identifier getTexture(ZombieEntity zombieEntity) {
        return TEXTURE;
    }
    public ZombieEntityRendererTransparent(EntityRendererFactory.Context context) {
        this(context, EntityModelLayers.ZOMBIE, EntityModelLayers.ZOMBIE_INNER_ARMOR, EntityModelLayers.ZOMBIE_OUTER_ARMOR);

    }
    public ZombieEntityRendererTransparent(EntityRendererFactory.Context ctx, EntityModelLayer layer, EntityModelLayer legsArmorLayer, EntityModelLayer bodyArmorLayer) {
        super(
                ctx, new SZombieEntityModel<>(ctx.getPart(layer)), new SZombieEntityModel<>(ctx.getPart(legsArmorLayer)), new SZombieEntityModel<>(ctx.getPart(bodyArmorLayer))
        );
    }

}
