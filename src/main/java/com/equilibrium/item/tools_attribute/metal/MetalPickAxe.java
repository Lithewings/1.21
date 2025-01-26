package com.equilibrium.item.tools_attribute.metal;

import com.equilibrium.event.CraftingMetalPickAxeCallback;
import com.equilibrium.item.Metal;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;



public class MetalPickAxe extends ToolItem {

    public MetalPickAxe(ToolMaterial material,Settings settings) {
        super(material,settings.component(DataComponentTypes.TOOL, createToolComponent()));
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        if(stack.getItem().getName().getString().contains("copper")||ingredient.isOf(Metal.copper_nugget))
            return true;
        else
            return false;
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        ToolComponent toolComponent = stack.get(DataComponentTypes.TOOL);
        if (toolComponent == null) {
            return false;
        } else {
            if (!world.isClient && state.getHardness(world, pos) != 0.0F) {
                stack.damage(stack.isSuitableFor(state)? 0 : 480 , miner, EquipmentSlot.MAINHAND);
            }

            return true;
        }
    }
    private static ToolComponent createToolComponent() {
        return new ToolComponent(
                List.of(ToolComponent.Rule.ofAlwaysDropping(BlockTags.PICKAXE_MINEABLE, 4F)), 1.0F, 0
        );
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        attacker.sendMessage(Text.of("击中实体的工具损伤"));
        stack.damage(300, attacker, EquipmentSlot.MAINHAND);
    }
    @Override
    public void onCraftByPlayer(ItemStack stack, World world, PlayerEntity player) {
        ActionResult result = CraftingMetalPickAxeCallback.EVENT.invoker().interact(world,player);

        if(result == ActionResult.FAIL) {
            return;
        }

    }

}
