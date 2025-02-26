package com.equilibrium.client.render.entity.renderer;

import com.equilibrium.entity.mob.BaseSlimeEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.SlimeEntityRenderer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.util.Identifier;

import static com.equilibrium.MITEequilibrium.MOD_ID;

public class PuddingSlimeEntityRenderer extends BaseSlimeRenderer {

    private static final Identifier TEXTURE = Identifier.of(MOD_ID,"textures/entity/pudding.png");
    public PuddingSlimeEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    public Identifier getTexture(BaseSlimeEntity baseSlimeEntity) {
        return TEXTURE;
    }

}
