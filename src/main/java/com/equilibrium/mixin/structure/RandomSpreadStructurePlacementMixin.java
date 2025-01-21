package com.equilibrium.mixin.structure;

import com.equilibrium.persistent_state.StateSaverAndLoader;
import com.equilibrium.util.ServerInfoRecorder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.world.gen.chunk.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.gen.chunk.placement.SpreadType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

import static java.lang.Math.random;

@Mixin(RandomSpreadStructurePlacement.class)
public class RandomSpreadStructurePlacementMixin {
    @Shadow
    @Final
    private int spacing;


    @Shadow
    @Final
    private int separation;

    @Shadow
    @Final
    private SpreadType spreadType;






    @Shadow
    public int getSpacing() {
        return this.spacing;
    }
    @Shadow
    public int getSeparation() {
        return this.separation;
    }
    @Shadow
    public SpreadType getSpreadType() {
        return this.spreadType;
    }


    private static boolean getStructureGenerateValidity(MinecraftServer server){
        StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(server);
        return serverState.isPickAxeCrafted;

    }

    @Inject(method = "getStartChunk",at = @At("HEAD"),cancellable = true)
    public void getStartChunk(long seed, int chunkX, int chunkZ, CallbackInfoReturnable<ChunkPos> cir) {
        cir.cancel();
        MinecraftServer server = ServerInfoRecorder.getServerInstance();
        boolean generate = getStructureGenerateValidity(server);
        int spacing = this.spacing;
        int separation = this.separation;
        if(-3200<chunkX&&chunkX<3200 && -3200<chunkZ&&chunkZ<3200) {
            spacing = generate?spacing:spacing+128;
            separation = (int) (spacing/1.7);
//            if(server!=null)
//                for(ServerWorld world :server.getWorlds()) {
//                    if (!world.getPlayers().isEmpty()) {
//                        world.getPlayers().getFirst().sendMessage(Text.of("spacing is " + spacing));
//                        world.getPlayers().getFirst().sendMessage(Text.of("separation is " + separation));
//                    }
//            }

        }
        int i = Math.floorDiv(chunkX, spacing);
        int j = Math.floorDiv(chunkZ, spacing);
        ChunkRandom chunkRandom = new ChunkRandom(new CheckedRandom(0L));
        Random random = new Random();
        double randomNumber = random.nextDouble();
        chunkRandom.setRegionSeed(seed, i, j, (int) (seed*randomNumber));
        int k = spacing - separation;
        int l = this.spreadType.get(chunkRandom, k);
        int m = this.spreadType.get(chunkRandom, k);

        int x = i * spacing+ l;
        int z = j * spacing + m;


        cir.setReturnValue(new ChunkPos(x,z));
    }











}
