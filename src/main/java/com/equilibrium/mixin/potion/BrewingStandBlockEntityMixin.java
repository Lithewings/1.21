package com.equilibrium.mixin.potion;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

import static net.minecraft.component.DataComponentTypes.POTION_CONTENTS;
import static net.minecraft.entity.effect.StatusEffects.SPEED;
import static net.minecraft.potion.Potions.SWIFTNESS;

@Mixin(BrewingStandBlockEntity.class)
public abstract class BrewingStandBlockEntityMixin extends LockableContainerBlockEntity implements SidedInventory {
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



//    @Inject(method = "tick",at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/BrewingStandBlockEntity;craft(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/collection/DefaultedList;)V",shift = At.Shift.AFTER))
//    private static void tick(World world, BlockPos pos, BlockState state, BrewingStandBlockEntity blockEntity, CallbackInfo ci) {
//        ItemStack potion = blockEntity.getStack(0);
//        if (potion.get(POTION_CONTENTS) != null){
//            potion.get(POTION_CONTENTS).forEachEffect();
//        }

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






    @Inject(method = "craft",at = @At("HEAD"), cancellable = true)
    private static void craft(World world, BlockPos pos, DefaultedList<ItemStack> slots, CallbackInfo ci) {
        ci.cancel();
        ItemStack itemStack = slots.get(3);
        BrewingRecipeRegistry brewingRecipeRegistry = world.getBrewingRecipeRegistry();

        for (int i = 0; i < 3; i++) {
//            ItemStack newPotion = Items.POTION.getDefaultStack();
            slots.get(i).set(POTION_CONTENTS, new PotionContentsComponent(Optional.of(SWIFTNESS), Optional.empty(), List.of(new StatusEffectInstance(SPEED, 16000, 4, true, true))));
//            ItemStack oldPotion = brewingRecipeRegistry.craft(itemStack, slots.get(i));
//            if(oldPotion.get(POTION_CONTENTS)!=null) {
//
//
//
//            }








        }

        itemStack.decrement(1);
        if (itemStack.getItem().hasRecipeRemainder()) {
            ItemStack itemStack2 = new ItemStack(itemStack.getItem().getRecipeRemainder());
            if (itemStack.isEmpty()) {
                itemStack = itemStack2;
            } else {
                ItemScatterer.spawn(world, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), itemStack2);
            }
        }

        slots.set(3, itemStack);
        world.syncWorldEvent(WorldEvents.BREWING_STAND_BREWS, pos, 0);
    }
}
