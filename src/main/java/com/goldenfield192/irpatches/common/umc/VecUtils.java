package com.goldenfield192.irpatches.common.umc;

import cam72cam.mod.math.Vec3d;

public class VecUtils {
    public static double dotMultiply(Vec3d a, Vec3d b){
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }
}
