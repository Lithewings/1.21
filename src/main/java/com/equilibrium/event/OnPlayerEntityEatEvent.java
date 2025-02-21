package com.equilibrium.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

public interface OnPlayerEntityEatEvent {
    Event<OnPlayerEntityEatEvent> EVENT = EventFactory.createArrayBacked(OnPlayerEntityEatEvent.class,
            (listeners) -> (player) -> {
                for (OnPlayerEntityEatEvent listener : listeners) {
                    ActionResult result = listener.interact(player);

                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    ActionResult interact(PlayerEntity player);


}
