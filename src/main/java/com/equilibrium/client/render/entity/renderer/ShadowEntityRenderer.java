package com.equilibrium.client.render.entity.renderer;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ZombieEntityRenderer;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.Identifier;

import static com.equilibrium.MITEequilibrium.MOD_ID;

public class ShadowEntityRenderer extends ZombieEntityRenderer {
    //影子潜伏者(在最黑的位置生成,破坏火把)
    public ShadowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }
    private static final Identifier TEXTURE = Identifier.of(MOD_ID,"textures/entity/shadow.png");

    @Override
    public Identifier getTexture(ZombieEntity zombieEntity) {
        return TEXTURE;
    }
}
