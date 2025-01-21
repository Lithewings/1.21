package com.equilibrium.mixin.structure;

import com.equilibrium.persistent_state.StateSaverAndLoader;
import com.equilibrium.util.ServerInfoRecorder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.gen.chunk.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.gen.chunk.placement.SpreadType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;

import static com.equilibrium.util.ServerInfoRecorder.getStructureGenerateValidity;

@Mixin(RandomSpreadStructurePlacement.class)
public class RandomSpreadStructurePlacementMixin {
//    @Shadow
//    @Final
//    private int spacing = 256;
//    @Shadow
//    @Final
//    private int separation = 254;




    private boolean getStructureGenerateValidity(){
        MinecraftServer server = ServerInfoRecorder.getServerInstance();
        if(server!=null){
            StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(server);
            return serverState.isPickAxeCrafted;
        }
        else return false;


    }

    @Shadow
    @Final
    private int spacing = !getStructureGenerateValidity()?4095:2;

    @Shadow
    @Final
    private int separation=spacing-1;


}
