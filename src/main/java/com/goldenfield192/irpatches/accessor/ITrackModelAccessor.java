package com.goldenfield192.irpatches.accessor;

import com.goldenfield192.irpatches.util.ExtraTrackDefinition;

public interface ITrackModelAccessor {
    boolean hasExtraDefinition();
    ExtraTrackDefinition.ExtraTrackModel getExtra();
    void setExtra(ExtraTrackDefinition.ExtraTrackModel model);
}
