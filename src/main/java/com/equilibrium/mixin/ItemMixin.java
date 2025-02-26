package com.equilibrium.mixin;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


import static net.minecraft.component.DataComponentTypes.*;
import static net.minecraft.item.Items.register;

@Mixin(Item.Settings.class)
public class ItemMixin {

    @Shadow private ComponentMap.Builder components;
    @Shadow
    private static final Interner<ComponentMap> COMPONENT_MAP_INTERNER = Interners.newStrongInterner();

    ComponentMap DEFAULT_ITEM_COMPONENTS =

            ComponentMap.builder()
            .add(MAX_STACK_SIZE, 16)
            .add(LORE, LoreComponent.DEFAULT)
            .add(ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT)
            .add(REPAIR_COST, 0)
            .add(ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT)
            .add(RARITY, Rarity.COMMON)
            .build();


    @Shadow
    public <T> Item.Settings component(ComponentType<T> type, T value) {
        if (this.components == null) {
            this.components = ComponentMap.builder().addAll(this.DEFAULT_ITEM_COMPONENTS);
        }
        this.components.add(type, value);
        return (Item.Settings) this.components.build();
    }

    @Inject(method = "getComponents",at = @At(value = "HEAD"),cancellable = true)
    private void getComponents(CallbackInfoReturnable<ComponentMap> cir) {
        cir.cancel();
        cir.setReturnValue(this.components == null ? this.DEFAULT_ITEM_COMPONENTS : COMPONENT_MAP_INTERNER.intern(this.components.build()));
    }
    @Inject(method = "maxCount",at = @At(value = "HEAD"),cancellable = true)
    public void maxCount(int maxCount, CallbackInfoReturnable<Item.Settings> cir) {
        cir.cancel();
        if(maxCount>=16)
            maxCount=maxCount/2;
        cir.setReturnValue(this.component(DataComponentTypes.MAX_STACK_SIZE, maxCount));
    }



}
