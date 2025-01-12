package com.equilibrium.event.sound;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SoundEventRegistry {
	public static final Identifier finishSoundID = Identifier.of("entity.experience_orb.pickup");
    public static SoundEvent finishSound = SoundEvent.of(finishSoundID);

}
