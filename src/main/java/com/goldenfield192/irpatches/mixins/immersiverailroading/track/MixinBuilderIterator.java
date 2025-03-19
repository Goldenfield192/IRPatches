package com.goldenfield192.irpatches.mixins.immersiverailroading.track;

import cam72cam.immersiverailroading.library.TrackDirection;
import cam72cam.immersiverailroading.track.BuilderBase;
import cam72cam.immersiverailroading.track.BuilderIterator;
import cam72cam.immersiverailroading.track.PosStep;
import cam72cam.mod.math.Vec3d;
import com.goldenfield192.irpatches.accessor.IVec3dAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(BuilderIterator.class)
public class MixinBuilderIterator {
    @Inject(method = "getRenderData",
            at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0),
            remap = false, locals = LocalCapture.CAPTURE_FAILSOFT)
    public void inject1(CallbackInfoReturnable<List<BuilderBase.VecYawPitch>> cir, List<BuilderBase.VecYawPitch> data,
                       double scale, List<PosStep> points, boolean switchStraight, int switchSize, TrackDirection direction,
                       int i, PosStep cur, PosStep switchPos, double var11, double var13, Vec3d var15, double var16,
                       float angle, PosStep var19, PosStep var20) {
        ((IVec3dAccessor) data.get(data.size() - 1)).IRPatch$setRoll(((IVec3dAccessor) switchPos).IRPatch$getRoll());
    }

    @Inject(method = "getRenderData",
            at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1),
            remap = false, locals = LocalCapture.CAPTURE_FAILSOFT)
    public void inject2(CallbackInfoReturnable<List<BuilderBase.VecYawPitch>> cir, List<BuilderBase.VecYawPitch> data,
                       double scale, List<PosStep> points, boolean switchStraight, int switchSize, TrackDirection direction,
                       int i, PosStep cur, PosStep switchPos, double var11, double var13, Vec3d var15, double var16,
                       float angle, PosStep var19, PosStep var20) {
        ((IVec3dAccessor) data.get(data.size() - 1)).IRPatch$setRoll(((IVec3dAccessor) switchPos).IRPatch$getRoll());
    }

    @Inject(method = "getRenderData",
            at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 2),
            remap = false, locals = LocalCapture.CAPTURE_FAILSOFT)
    public void inject3(CallbackInfoReturnable<List<BuilderBase.VecYawPitch>> cir, List<BuilderBase.VecYawPitch> data,
                       double scale, List<PosStep> points, boolean switchStraight, int switchSize, TrackDirection direction,
                       int i, PosStep cur, PosStep switchPos, double var11, double var13, Vec3d var15, double var16,
                       float angle, PosStep var19, PosStep var20) {
        ((IVec3dAccessor) data.get(data.size() - 1)).IRPatch$setRoll(((IVec3dAccessor) switchPos).IRPatch$getRoll());
    }

    @Inject(method = "getRenderData",
            at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 3),
            remap = false, locals = LocalCapture.CAPTURE_FAILSOFT)
    public void inject4(CallbackInfoReturnable<List<BuilderBase.VecYawPitch>> cir, List<BuilderBase.VecYawPitch> data,
                       double scale, List<PosStep> points, boolean switchStraight, int switchSize, TrackDirection direction,
                       int i, PosStep cur, PosStep switchPos, double var11, double var13, Vec3d var15, double var16,
                       float angle, PosStep var19, PosStep var20) {
        ((IVec3dAccessor) data.get(data.size() - 1)).IRPatch$setRoll(((IVec3dAccessor) switchPos).IRPatch$getRoll());
    }

    @Inject(method = "getRenderData",
            at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 4),
            remap = false, locals = LocalCapture.CAPTURE_FAILSOFT)
    public void inject5(CallbackInfoReturnable<List<BuilderBase.VecYawPitch>> cir, List<BuilderBase.VecYawPitch> data,
                       double scale, List<PosStep> points, boolean switchStraight, int switchSize, TrackDirection direction,
                       int i, PosStep cur, PosStep switchPos, double var11, double var13, Vec3d var15, double var16,
                       float angle, PosStep var19, PosStep var20) {
        ((IVec3dAccessor) data.get(data.size() - 1)).IRPatch$setRoll(((IVec3dAccessor) switchPos).IRPatch$getRoll());
    }

    @Inject(method = "getRenderData",
            at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 5),
            remap = false, locals = LocalCapture.CAPTURE_FAILSOFT)
    public void inject6(CallbackInfoReturnable<List<BuilderBase.VecYawPitch>> cir, List<BuilderBase.VecYawPitch> data,
                       double scale, List<PosStep> points, boolean switchStraight, int switchSize, TrackDirection direction,
                       int i, PosStep cur, PosStep switchPos, double var11, double var13, Vec3d var15, double var16,
                       float angle, PosStep var19, PosStep var20) {
        ((IVec3dAccessor) data.get(data.size() - 1)).IRPatch$setRoll(((IVec3dAccessor) switchPos).IRPatch$getRoll());
    }
}
