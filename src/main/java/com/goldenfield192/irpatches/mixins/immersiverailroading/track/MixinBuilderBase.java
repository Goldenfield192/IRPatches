package com.goldenfield192.irpatches.mixins.immersiverailroading.track;

import cam72cam.immersiverailroading.track.BuilderBase;
import cam72cam.immersiverailroading.track.TrackBase;
import com.goldenfield192.irpatches.accessor.IBuilderBaseAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;

@Mixin(BuilderBase.class)
public class MixinBuilderBase implements IBuilderBaseAccessor {
    @Shadow(remap = false) protected ArrayList<TrackBase> tracks;

    @Override
    public ArrayList<TrackBase> getTracks() {
        return this.tracks;
    }
}
