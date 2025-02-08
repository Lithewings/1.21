package com.equilibrium.enchantments;

import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public class EnchantedFoodEnchantment implements EnchantmentEntityEffect {
    public static final MapCodec<WaterBucketEnchantment> CODEC = MapCodec.unit(WaterBucketEnchantment::new);
    /**
     * @param world
     * @param level
     * @param context
     * @param user
     * @param pos
     */
    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {

    }

    /**
     * @return
     */
    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}
