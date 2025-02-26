package com.equilibrium.entity;

import com.equilibrium.entity.mob.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static com.equilibrium.MITEequilibrium.MOD_ID;


public class ModEntities {
    //注册实体,记得在客户端那边渲染
//    public static final EntityType<TestZombieEntity> TEST_ZOMBIE = Registry.register(Registries.ENTITY_TYPE,
//            Identifier.of("miteequilibrium","test_zombie"),
//            //fixed(width, height)
//            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER,TestZombieEntity::new).dimensions(EntityDimensions.fixed(0.75f, 1.95f)).build());


    public static final EntityType<InvisibleStalkerEntity> INVISIBLE_STALKER = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(MOD_ID,"invisible_stalker"),
            //fixed(width, height)
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER,InvisibleStalkerEntity::new).dimensions(EntityDimensions.fixed(0.75f, 1.95f)).build());


    public static final EntityType<GhoulEntity> GHOUL = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(MOD_ID,"ghoul"),
            //fixed(width, height)
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, GhoulEntity::new).dimensions(EntityDimensions.fixed(0.75f, 1.95f)).build());

    public static final EntityType<ShadowEntity> SHADOW = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(MOD_ID,"shadow"),
            //fixed(width, height)
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, ShadowEntity::new).dimensions(EntityDimensions.fixed(0.75f, 1.95f)).build());


    public static final EntityType<WightEntity> WIGHT = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(MOD_ID,"wight"),
            //fixed(width, height)
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, WightEntity::new).dimensions(EntityDimensions.fixed(0.75f, 1.95f)).build());



    public static final EntityType<LongDeadEntity> LONG_DEAD = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(MOD_ID,"longdead"),
            //fixed(width, height)
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, LongDeadEntity::new).dimensions(EntityDimensions.fixed(0.75f, 1.95f)).build());


    public static final EntityType< PuddingSlimeEntity> PUDDING = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(MOD_ID,"pudding"),
            //fixed(width, height)
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, PuddingSlimeEntity::new).dimensions(EntityDimensions.fixed(0.75f, 1.95f)).build());



    //注册属性
    public static void registerModEntities(){
//        FabricDefaultAttributeRegistry.register(TEST_ZOMBIE, TestZombieEntity.createZombieAttributes());

        FabricDefaultAttributeRegistry.register(INVISIBLE_STALKER, HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23F)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 0.5)
                .add(EntityAttributes.GENERIC_ARMOR, 2.0)
                .add(EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS)
                .add(EntityAttributes.GENERIC_MAX_HEALTH,20));

        FabricDefaultAttributeRegistry.register(GHOUL, HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23F)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0)
                .add(EntityAttributes.GENERIC_ARMOR, 2.0)
                .add(EntityAttributes.GENERIC_MAX_HEALTH,20)
                .add(EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS));



        FabricDefaultAttributeRegistry.register(SHADOW, HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23F)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0)
                .add(EntityAttributes.GENERIC_ARMOR, 2.0)
                .add(EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS)
                .add(EntityAttributes.GENERIC_MAX_HEALTH,20));

        FabricDefaultAttributeRegistry.register(WIGHT, HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23F)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0)
                .add(EntityAttributes.GENERIC_ARMOR, 2.0)
                .add(EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS)
                .add(EntityAttributes.GENERIC_MAX_HEALTH,20));

        FabricDefaultAttributeRegistry.register(LONG_DEAD, HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25F)
                .add(EntityAttributes.GENERIC_ARMOR, 2.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 0.5));


       //setSize中会引用这三个属性
        FabricDefaultAttributeRegistry.register(PUDDING, HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE)
                .add(EntityAttributes.GENERIC_MAX_HEALTH));



    }




}


