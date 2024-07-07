package com.equilibrium;

import com.equilibrium.util.WorldMoonPhasesSelector;
import net.minecraft.world.dimension.NetherPortal;

import java.util.Scanner;

import static com.equilibrium.util.WorldMoonPhasesSelector.getDay;

public class test {
    public static void main(String[] args) {
        for (int i =0;i<129;i++){
            WorldMoonPhasesSelector.setMoonType(24000*i);
        }





    }
}
