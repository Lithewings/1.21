package com.equilibrium.mixin.potion;

import com.equilibrium.MITEequilibrium;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BrewingStandBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;

import static com.equilibrium.status.registerStatusEffect.PHYTONUTRIENT;
import static net.minecraft.component.DataComponentTypes.POTION_CONTENTS;
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







    @Inject(method = "tick",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"),cancellable = true)
    private static void tick(World world, BlockPos pos, BlockState state, BrewingStandBlockEntity blockEntity, CallbackInfo ci) {

        StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.SPEED, 9322,1, false,true,true);

        PotionContentsComponent potionContentsComponent = new PotionContentsComponent(SWIFTNESS).with(statusEffectInstance);

        blockEntity.getStack(0).set(POTION_CONTENTS,potionContentsComponent);
//        blockEntity.getStack(0).get(POTION_CONTENTS).customEffects();
//        MITEequilibrium.LOGGER.info("0 slot info :"+blockEntity.getStack(0).get(POTION_CONTENTS).potion());


    }





    @Inject(method = "craft",at = @At("HEAD"))
    private static void craft(World world, BlockPos pos, DefaultedList<ItemStack> slots, CallbackInfo ci) {
//        MITEequilibrium.LOGGER.info("craft() is triggered");
//        ItemStack itemStack = slots.get(3);
//        BrewingRecipeRegistry brewingRecipeRegistry = world.getBrewingRecipeRegistry();
//
//        for (int i = 0; i < 3; i++) {
//            slots.set(i, brewingRecipeRegistry.craft(itemStack, slots.get(i)));
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
    }
}
