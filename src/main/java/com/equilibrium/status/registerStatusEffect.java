package com.equilibrium.status;


import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class registerStatusEffect {
    public static final RegistryEntry<StatusEffect> PHYTONUTRIENT = register("phytonutrient",new PhytonutrientStatusEffect());




    private static RegistryEntry<StatusEffect> register(String id, StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of("miteequilibrium",id), statusEffect);
    }





    public static void registerStatusEffects(){

    }
}
