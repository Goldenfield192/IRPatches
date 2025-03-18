package com.goldenfield192.irpatches.mixins.umc.math;

import cam72cam.mod.math.Vec3d;
import com.goldenfield192.irpatches.accessor.IVec3dAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Vec3d.class)
public class MixinVec3d implements IVec3dAccessor {
    //I know this is ugly and should be avoided......but if so we have to change TrackAPI
    @Unique
    public float IRPatch$roll;

    @Override
    public void IRPatch$setRoll(float roll) {
        this.IRPatch$roll = roll;
    }

    @Override
    public float IRPatch$getRoll() {
        return IRPatch$roll;
    }
}
