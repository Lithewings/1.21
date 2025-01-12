package com.equilibrium.mixin.crafttime;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin {
    @Final
    @Shadow public final DefaultedList<Slot> slots = DefaultedList.of();

    @Inject(method = "internalOnSlotClick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/screen/slot/SlotActionType;SWAP:Lnet/minecraft/screen/slot/SlotActionType;"
            ),
            cancellable = true)
    private void internalOnSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        if(!player.getWorld().isClient){
            Slot slot3 = this.slots.get(slotIndex);
            if(slot3 instanceof CraftingResultSlot && ((ScreenHandler)(Object)this instanceof CraftingScreenHandler || (ScreenHandler)(Object)this instanceof PlayerScreenHandler)){
                if (actionType == SlotActionType.THROW && slotIndex >= 0) {

                    int j = button == 0 ? 1 : slot3.getStack().getCount();
                    ItemStack itemStack = slot3.takeStackRange(j, Integer.MAX_VALUE, player);

                    String nameItem = Registries.ITEM.getId(itemStack.getItem()).toString();
                //给特定物品添加标签：不会消失
                if (nameItem.contains("netherite_scrap") ||nameItem.contains("blaze_rod") ||nameItem.contains("elytra")) {
                    if (nameItem.contains("minecraft:")) {
                        NbtComponent data = (NbtComponent)itemStack.get(DataComponentTypes.CUSTOM_DATA);
                        if (data != null) {
                            NbtCompound value = data.copyNbt();
                            if (!value.contains("aliveandwell")){
                                setNbt(itemStack);
                            }
                        }else {
                            setNbt(itemStack);
                        }
                    }
                }
                if(nameItem.contains("argent_energy") ||nameItem.contains("argent_block")){
                    if(nameItem.contains("doom:")) {
                        NbtComponent data = (NbtComponent)itemStack.get(DataComponentTypes.CUSTOM_DATA);
                        if (data != null) {
                            NbtCompound value = data.copyNbt();
                            if (!value.contains("aliveandwell")){
                                setNbt(itemStack);
                            }
                        }else {
                            setNbt(itemStack);
                        }
                    }
                }

                    if (player.getInventory().insertStack(itemStack)){
                        player.getInventory().insertStack(itemStack);
                    }else {
                        player.dropStack(itemStack);
                    }
                    ci.cancel();
                }
            }
        }
    }

    @Unique
    public void setNbt(ItemStack itemStack) {
        NbtComponent data = (NbtComponent)itemStack.get(DataComponentTypes.CUSTOM_DATA);
        NbtCompound nbt;

        if (data != null) {
            nbt = data.copyNbt();
        }else {
            nbt = new NbtCompound();
        }

        nbt.putString("aliveandwell","aliveandwell");
        NbtComponent nbtComponent = NbtComponent.of(nbt);

        itemStack.set(DataComponentTypes.CUSTOM_DATA, nbtComponent);
    }
}
