package com.equilibrium.entity;

import com.equilibrium.entity.mob.InvisibleStalkerEntity;
import com.equilibrium.entity.mob.TestZombieEntity;
import com.equilibrium.mixin.entitymixin.HostileAttributesMixin;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;


public class ModEntities {
    //注册实体
    public static final EntityType<TestZombieEntity> TEST_ZOMBIE = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of("miteequilibrium","test_zombie"),
            //fixed(width, height)
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER,TestZombieEntity::new).dimensions(EntityDimensions.fixed(0.75f, 1.95f)).build());


    public static final EntityType<InvisibleStalkerEntity> INVISIBLE_STALKER = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of("miteequilibrium","invisible_stalker"),
            //fixed(width, height)
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER,InvisibleStalkerEntity::new).dimensions(EntityDimensions.fixed(0.75f, 1.95f)).build());







    //注册属性
    public static void registerModEntities(){
        FabricDefaultAttributeRegistry.register(TEST_ZOMBIE, TestZombieEntity.createZombieAttributes());
        FabricDefaultAttributeRegistry.register(INVISIBLE_STALKER, HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23F)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0)
                .add(EntityAttributes.GENERIC_ARMOR, 2.0)
                        .add(EntityAttributes.GENERIC_MAX_HEALTH,20)
                .add(EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS));
    };


    }


