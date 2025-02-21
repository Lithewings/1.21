package com.equilibrium;


import com.equilibrium.client.render.entity.GhoulModel;
import com.equilibrium.client.render.entity.InvisibleStalkerModel;
import com.equilibrium.client.render.entity.ShadowEntityModel;
import com.equilibrium.client.render.entity.WightEntityModel;
import com.equilibrium.util.MyCommands;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

import static com.equilibrium.entity.ModEntities.*;


public class MITEequilibriumClient implements ClientModInitializer {









    @Override
    public void onInitializeClient() {










        //将注册的实体和模型结合起来


        EntityRendererRegistry.register(INVISIBLE_STALKER, InvisibleStalkerModel::new);
        EntityRendererRegistry.register(GHOUL, GhoulModel::new);
        EntityRendererRegistry.register(SHADOW, ShadowEntityModel::new);
        EntityRendererRegistry.register(WIGHT, WightEntityModel::new);



        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            MyCommands.registerClientAllCommands(dispatcher);
        });

    }


}
