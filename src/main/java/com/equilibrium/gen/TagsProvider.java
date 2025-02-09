package com.equilibrium.gen;

import com.equilibrium.item.Armors;
import com.equilibrium.item.Metal;
import com.equilibrium.tags.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import org.jetbrains.annotations.Nullable;

import javax.swing.text.html.HTML;
import java.util.concurrent.CompletableFuture;

import static com.equilibrium.tags.ModItemTags.registerModItemTags;

public class TagsProvider extends FabricTagProvider.ItemTagProvider {


    /**
     * Construct an {@link ItemTagProvider} tag provider <b>without</b> an associated {@link BlockTagProvider} tag provider.
     *
     * @param output            The {@link FabricDataOutput} instance
     * @param completableFuture
     */
    public TagsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        registerModItemTags();
        getOrCreateTagBuilder(ItemTags.HEAD_ARMOR_ENCHANTABLE).
                add(Armors.COPPER_HELMET);
        getOrCreateTagBuilder(ItemTags.LEG_ARMOR_ENCHANTABLE).
                add(Armors.COPPER_LEGGINGS);
        getOrCreateTagBuilder(ItemTags.CHEST_ARMOR_ENCHANTABLE).
                add(Armors.COPPER_CHEST_PLATE);
        getOrCreateTagBuilder(ItemTags.FOOT_ARMOR_ENCHANTABLE).
                add(Armors.COPPER_BOOTS);









    }
}
