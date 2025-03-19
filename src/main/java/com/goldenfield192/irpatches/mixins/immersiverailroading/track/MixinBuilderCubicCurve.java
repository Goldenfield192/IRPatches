package com.goldenfield192.irpatches.mixins.immersiverailroading.track;

import cam72cam.immersiverailroading.track.*;
import cam72cam.immersiverailroading.util.RailInfo;
import cam72cam.immersiverailroading.util.VecUtil;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.world.World;
import com.goldenfield192.irpatches.accessor.IRailSettingsAccessor;
import com.goldenfield192.irpatches.accessor.IVec3dAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collections;
import java.util.List;

@Mixin(BuilderCubicCurve.class)
public abstract class MixinBuilderCubicCurve extends BuilderIterator {
    private MixinBuilderCubicCurve(RailInfo info, World world, Vec3i pos) {
        super(info, world, pos);
    }

    @Inject(method = "getPath", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"), remap = false, locals = LocalCapture.CAPTURE_FAILSOFT)
    public void inject(double stepSize, CallbackInfoReturnable<List> cir, List<PosStep> res, CubicCurve curve, List<Vec3d> points,
                       int i, Vec3d p, float yaw, float pitch, Vec3d var10, Vec3d var11){
        PosStep step = res.get(res.size() - 1);

        float roll;
        IRailSettingsAccessor settingsAccessor = (IRailSettingsAccessor) info.settings;
        if (points.size() == 1) {
            roll = (settingsAccessor.IRPatch$getFarEndTilt() + settingsAccessor.IRPatch$getNearEndTilt()) / 2;
        } else if (i == points.size()-1) {
            roll = settingsAccessor.IRPatch$getFarEndTilt();
        } else if (i == 0) {
            roll = settingsAccessor.IRPatch$getNearEndTilt();
        } else {
            float percent = (i+1f) / points.size();
            roll = settingsAccessor.IRPatch$getFarEndTilt() * percent + settingsAccessor.IRPatch$getNearEndTilt() * (1 - percent);
        }
        roll = 90;
        ((IVec3dAccessor)step).IRPatch$setRoll(roll);
    }
}
