package com.goldenfield192.irpatches.mixins.features.random_rail;

import cam72cam.immersiverailroading.model.TrackModel;
import com.goldenfield192.irpatches.accessor.ITrackModelAccessor;
import com.goldenfield192.irpatches.util.ExtraTrackDefinition;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TrackModel.class)
public class MixinTrackModel implements ITrackModelAccessor {
    private ExtraTrackDefinition.ExtraTrackModel model;

    @Override
    public boolean hasExtraDefinition() {
        return model != null;
    }

    @Override
    public ExtraTrackDefinition.ExtraTrackModel getExtra() {
        return model;
    }

    @Override
    public void setExtra(ExtraTrackDefinition.ExtraTrackModel model) {
        this.model = model;
    }
}
