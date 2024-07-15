package com.equilibrium;


import com.equilibrium.client.render.entity.InvisibleStalker;
import com.equilibrium.client.render.entity.TestZombie;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

import static com.equilibrium.entity.ModEntities.INVISIBLE_STALKER;
import static com.equilibrium.entity.ModEntities.TEST_ZOMBIE;


public class MITEequilibriumClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
    //将注册的实体和模型结合起来


        EntityRendererRegistry.register(TEST_ZOMBIE, TestZombie::new);
        EntityRendererRegistry.register(INVISIBLE_STALKER, InvisibleStalker::new);







    }
}
