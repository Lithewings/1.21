package com.equilibrium.client.render.entity.renderer;

import com.equilibrium.client.render.entity.renderer.ModSkeletonEntityRenderer;
import com.equilibrium.entity.mob.LongDeadEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

import static com.equilibrium.MITEequilibrium.MOD_ID;

public class LongDeadEntityRenderer extends ModSkeletonEntityRenderer<LongDeadEntity> {
    public LongDeadEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }
    private static final Identifier TEXTURE = Identifier.of(MOD_ID,"textures/entity/longdead.png");

    public Identifier getTexture(LongDeadEntity longDeadEntity) {
        return TEXTURE;
    }
}
