package com.goldenfield192.irpatches.accessor;

import cam72cam.mod.math.Vec3d;

public interface ITileRailBaseAccessor {
    String getCGFilter();

    void setCGFilter(String s);

    float getNextRoll(Vec3d currentPosition, Vec3d motion);

    boolean getDirectionAlong(Vec3d currentPosition, Vec3d motion);
}
