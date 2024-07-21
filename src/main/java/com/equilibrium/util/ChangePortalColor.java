package com.equilibrium.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;

public class ChangePortalColor extends Block {
    public static final BooleanProperty CHANGE_PORTAL_COLOR_INTO_BLUE = BooleanProperty.of("change_portal_color_into_blue");

    public ChangePortalColor(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(CHANGE_PORTAL_COLOR_INTO_BLUE, false));
    }



    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(CHANGE_PORTAL_COLOR_INTO_BLUE);
    }
}
