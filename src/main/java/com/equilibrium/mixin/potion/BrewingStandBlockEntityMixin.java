package com.equilibrium.mixin.potion;

import com.mojang.datafixers.kinds.IdF;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.TranslatableOption;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

import static net.minecraft.component.DataComponentTypes.ITEM_NAME;
import static net.minecraft.component.DataComponentTypes.POTION_CONTENTS;
import static net.minecraft.entity.effect.StatusEffects.SPEED;
import static net.minecraft.potion.Potions.SWIFTNESS;

@Mixin(BrewingStandBlockEntity.class)
public abstract class BrewingStandBlockEntityMixin extends LockableContainerBlockEntity implements SidedInventory {
    @Shadow @Final private static int[] TOP_SLOTS;

    @Shadow private Item itemBrewing;

    protected BrewingStandBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

//    private static RegistryEntry<Potion> register(String name, Potion potion) {
//        return Registry.register(Registries.STATUS_EFFECT,Identifier.of("miteequilibrium",name), potion);
//    }
//
//    private static final RegistryEntry<Potion> MOD_SPEED = register(
//            "mod_speed", new Potion("mod_speed", new StatusEffectInstance(StatusEffects.SPEED, 16800))
//    );



    private static PotionContentsComponent modifyPotionEffect(BrewingStandBlockEntity blockEntity){
        ItemStack potion = blockEntity.getStack(0);
        if(potion.get(POTION_CONTENTS)==null)
            new PotionContentsComponent(Optional.empty(), Optional.empty(), List.of());

        //先获取药水效果
        PotionContentsComponent potionContentsComponent=potion.get(POTION_CONTENTS);
        //药水效果非空时
        if (potionContentsComponent != null) {
            //药水效果是一个列表,但药水只有一个效果,故只取第一个拿来
            StatusEffectInstance statusEffect = potionContentsComponent.getEffects().iterator().next();
            //获取到药水类型
            RegistryEntry<StatusEffect> potionType = statusEffect.getEffectType();
            //获取到药水持续时间
            int duration = statusEffect.getDuration();
            //获取药水的增益倍数
            int amplifier = statusEffect.getAmplifier();
            //创建新定义的增强药水效果
            StatusEffectInstance boostStatusEffect = new StatusEffectInstance(potionType, duration*2, amplifier, true, true);
            //创建药水效果列表
            List<StatusEffectInstance> customEffects = List.of(boostStatusEffect);
            PotionContentsComponent boostPotionContentsComponent = new PotionContentsComponent(potionContentsComponent.potion(), Optional.empty(), customEffects);
            //药水增强效果
            return boostPotionContentsComponent;
        }
        return new PotionContentsComponent(Optional.empty(), Optional.empty(), List.of());
    }

    private static String translateFromEffectToPotion(String key, Boolean splash,Boolean lingering) {
        Map<String, String> translate = new HashMap<>();

        translate.put("night_vision", "item.minecraft.potion.effect.night_vision");
        translate.put("invisibility", "item.minecraft.potion.effect.invisibility");
        translate.put("jump_boost", "item.minecraft.potion.effect.leaping");
        translate.put("fire_resistance", "item.minecraft.potion.effect.fire_resistance");
        translate.put("speed", "item.minecraft.potion.effect.swiftness");
        translate.put("slowness", "item.minecraft.potion.effect.slowness");
        translate.put("water_breathing", "item.minecraft.potion.effect.water_breathing");
        translate.put("instant_health", "item.minecraft.potion.effect.healing");
        translate.put("instant_damage", "item.minecraft.potion.effect.harming");
        translate.put("poison", "item.minecraft.potion.effect.poison");
        translate.put("regeneration", "item.minecraft.potion.effect.regeneration");
        translate.put("strength", "item.minecraft.potion.effect.strength");
        translate.put("weakness", "item.minecraft.potion.effect.weakness");
        translate.put("levitation", "item.minecraft.potion.effect.levitation");
        translate.put("luck", "item.minecraft.potion.effect.luck");
        translate.put("slow_falling", "item.minecraft.potion.effect.slow_falling");
        translate.put("infested", "item.minecraft.potion.effect.infestation");
        translate.put("oozing", "item.minecraft.potion.effect.oozing");
        translate.put("weaving", "item.minecraft.potion.effect.weaving");
        translate.put("wind_charged", "item.minecraft.potion.effect.wind_charging");


        //字符串插入
        StringBuilder stringBuilder = new StringBuilder(translate.getOrDefault(key, "item.minecraft.potion.effect.empty"));
        if(splash) {
            stringBuilder.insert(15, "splash_");
            return stringBuilder.toString();
        }
        else if (lingering) {
            stringBuilder.insert( 15, "lingering_");
            return stringBuilder.toString();
        }else
            return stringBuilder.toString();




    };






    @Inject(method = "tick",at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/BrewingStandBlockEntity;craft(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/collection/DefaultedList;)V",shift = At.Shift.AFTER))
    private static void tick(World world, BlockPos pos, BlockState state, BrewingStandBlockEntity blockEntity, CallbackInfo ci) {
        if(!(blockEntity.itemBrewing==Items.REDSTONE || (blockEntity.itemBrewing==Items.GLOWSTONE_DUST)))
                return;
            for(int i =0 ;i<3;i++){
                ItemStack potion = blockEntity.getStack(i);
                PotionContentsComponent potionContentsComponent =potion.get(POTION_CONTENTS);

                if (potionContentsComponent != null && potionContentsComponent.hasEffects()) {
                    Iterator<StatusEffectInstance> iterator = potionContentsComponent.getEffects().iterator();
                    StatusEffectInstance statusEffectInstance = iterator.next();
                    String s = potion.getTranslationKey();
                    boolean splash = s.contains("minecraft.splash_potion");
                    boolean lingering = s.contains("minecraft.lingering_potion");
                    if (iterator.hasNext())
                        //神龟药水不改动
                        return;
                    if (lingering)
                        //滞留药水不改动
                        return;
                    int duration = statusEffectInstance.getDuration();
                    int amplify = statusEffectInstance.getAmplifier();
                    //去掉"minecraft:"
                    String effectName = statusEffectInstance.getEffectType().getIdAsString().substring(10);


                    if (blockEntity.itemBrewing == Items.REDSTONE) {
                        duration = duration * 2;
                    }
                    RegistryEntry<StatusEffect> StatusEffect = statusEffectInstance.getEffectType();
                    if (blockEntity.itemBrewing == Items.GLOWSTONE_DUST)
                        //不对最后的等级进行增益,只动增益时间
                        duration = (int) (duration * 1.5);
//                    for (PlayerEntity playerEntity : world.getPlayers()) {
//                        playerEntity.sendMessage(Text.of("这是一个有效果的药水"));
//                        playerEntity.sendMessage(Text.of("它的效果是" + effectName));
//                    }

                    StatusEffectInstance newstatusEffectInstance = new StatusEffectInstance(StatusEffect, duration, amplify, false, true, true);
                    PotionContentsComponent newPotionContentsComponent = new PotionContentsComponent(Optional.empty(), Optional.empty(), List.of(newstatusEffectInstance));
                    blockEntity.getStack(i).set(POTION_CONTENTS, newPotionContentsComponent);
                    blockEntity.getStack(i).set(ITEM_NAME, Text.translatable(translateFromEffectToPotion(effectName, splash, lingering)));
                }
        }
    }

//            ItemStack newPotion = Items.POTION.getDefaultStack();
//            blockEntity.setStack(0, newPotion);
//            blockEntity.getStack(0).get(POTION_CONTENTS).customEffects().add(new StatusEffectInstance(SPEED, 16000, 4, true, true));
//            blockEntity.getStack(0).set(POTION_CONTENTS, new PotionContentsComponent(Optional.of(SWIFTNESS), Optional.empty(), List.of(new StatusEffectInstance(SPEED, 16000, 4, true, true))));



;
//            blockEntity.getStack(0).set(POTION_CONTENTS, new PotionContentsComponent(Optional.of(SWIFTNESS), Optional.empty(), List.of(new StatusEffectInstance(SPEED, 11451, 3, true, true))));

        //空白内容类
//        PotionContentsComponent potionContentsComponent =new PotionContentsComponent(Optional.empty(), Optional.empty(), List.of());
//        blockEntity.getStack(0).setCount(0);
//        ItemStack newPotion = Items.POTION.getDefaultStack();
//        newPotion.set(POTION_CONTENTS, new PotionContentsComponent(Optional.of(SWIFTNESS), Optional.empty(), List.of(new StatusEffectInstance(SPEED, 16000,4, true, true))));
//        blockEntity.setStack(0,newPotion);
//        blockEntity.getStack(0).set(POTION_CONTENTS, new PotionContentsComponent(Optional.of(SWIFTNESS), Optional.empty(), List.of(new StatusEffectInstance(SPEED, 11451, 3, true, true))));




//        if(modifyPotionEffect(blockEntity)!=null) {
//            potionContentsComponent = modifyPotionEffect(blockEntity);
//            blockEntity.getStack(0).set(POTION_CONTENTS, potionContentsComponent);
//        }

//
//          if(modifyPotionEffect(blockEntity)!=null) {
////              PotionContentsComponent boostPotionContentsComponent =modifyPotionEffect(blockEntity);
//              blockEntity.getStack(0).set(POTION_CONTENTS, new PotionContentsComponent(Optional.empty(), Optional.empty(), List.of()));
//          }



//        StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.SPEED, 9322,1, false,true,true);
//        PotionContentsComponent potionContentsComponent = new PotionContentsComponent(SWIFTNESS).with(statusEffectInstance);
//
//          blockEntity.getStack(0).set(POTION_CONTENTS,
//                  potionContentsComponent
//          );
//        blockEntity.getStack(0).get(POTION_CONTENTS).customEffects();
//        MITEequilibrium.LOGGER.info("0 slot info :"+blockEntity.getStack(0).get(POTION_CONTENTS).potion());






    @Inject(method = "craft",at = @At("HEAD"))
    private static void craft(World world, BlockPos pos, DefaultedList<ItemStack> slots, CallbackInfo ci) {
//        ci.cancel();
//        ItemStack itemStack = slots.get(3);
//        BrewingRecipeRegistry brewingRecipeRegistry = world.getBrewingRecipeRegistry();
//
//        for (int i = 0; i < 3; i++) {
////            ItemStack newPotion = Items.POTION.getDefaultStack();
//            slots.get(i).set(POTION_CONTENTS, new PotionContentsComponent(Optional.of(SWIFTNESS), Optional.empty(), List.of(new StatusEffectInstance(SPEED, 16000, 4, true, true))));
////            ItemStack oldPotion = brewingRecipeRegistry.craft(itemStack, slots.get(i));
////            if(oldPotion.get(POTION_CONTENTS)!=null) {
////
////
////
////            }
//
//
//
//
//
//
//
//
//        }
//
//        itemStack.decrement(1);
//        if (itemStack.getItem().hasRecipeRemainder()) {
//            ItemStack itemStack2 = new ItemStack(itemStack.getItem().getRecipeRemainder());
//            if (itemStack.isEmpty()) {
//                itemStack = itemStack2;
//            } else {
//                ItemScatterer.spawn(world, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), itemStack2);
//            }
//        }
//
//        slots.set(3, itemStack);
//        world.syncWorldEvent(WorldEvents.BREWING_STAND_BREWS, pos, 0);
//    }
    }}
