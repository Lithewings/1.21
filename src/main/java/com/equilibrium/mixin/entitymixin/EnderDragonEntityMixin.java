package com.equilibrium.mixin.entitymixin;

import com.equilibrium.persistent_state.StateSaverAndLoader;
import com.equilibrium.util.ServerInfoRecorder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(EnderDragonEntity.class)
public abstract class EnderDragonEntityMixin  extends MobEntity implements Monster {
    @Shadow
    public float prevWingPosition;

    protected EnderDragonEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }
    //玩家每死亡一次,末影龙获得一点基础护甲

    @Unique
    StateSaverAndLoader stateSaverAndLoader;

    @Unique
    private int baseArmor(){
        this.stateSaverAndLoader = StateSaverAndLoader.getServerState(ServerInfoRecorder.getServerInstance());
        return Math.max(stateSaverAndLoader.playerDeathTimes,40);
    }

    @Inject(method = "tickMovement",at = @At("HEAD"))
    public void tickMovement(CallbackInfo ci) {
        this.getAttributeInstance(EntityAttributes.GENERIC_ARMOR).setBaseValue(baseArmor());
    }
}




