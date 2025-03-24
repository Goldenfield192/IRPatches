package com.goldenfield192.irpatches.mixins.immersiverailroading.track;

import cam72cam.immersiverailroading.track.BuilderBase;
import cam72cam.immersiverailroading.track.BuilderIterator;
import cam72cam.immersiverailroading.track.PosStep;
import com.goldenfield192.irpatches.accessor.IVec3dAccessor;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BuilderIterator.class)
public class MixinBuilderIterator {
    @Inject(method = "getRenderData",
            at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0),
            remap = false)
    public void inject0(CallbackInfoReturnable<List<BuilderBase.VecYawPitch>> cir, @Local(ordinal = 0) List data, @Local(ordinal = 1) PosStep switchPos) {
        ((IVec3dAccessor) data.get(data.size() - 1)).setRoll(((IVec3dAccessor) switchPos).getRoll());
    }
    @Inject(method = "getRenderData",
            at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1),
            remap = false)
    public void inject1(CallbackInfoReturnable<List<BuilderBase.VecYawPitch>> cir, @Local(ordinal = 0) List data, @Local(ordinal = 1) PosStep switchPos) {
        ((IVec3dAccessor) data.get(data.size() - 1)).setRoll(((IVec3dAccessor) switchPos).getRoll());
    }
    @Inject(method = "getRenderData",
            at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 2),
            remap = false)
    public void inject2(CallbackInfoReturnable<List<BuilderBase.VecYawPitch>> cir, @Local(ordinal = 0) List data, @Local(ordinal = 1) PosStep switchPos) {
        ((IVec3dAccessor) data.get(data.size() - 1)).setRoll(((IVec3dAccessor) switchPos).getRoll());
    }
    @Inject(method = "getRenderData",
            at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 3),
            remap = false)
    public void inject3(CallbackInfoReturnable<List<BuilderBase.VecYawPitch>> cir, @Local(ordinal = 0) List data, @Local(ordinal = 1) PosStep switchPos) {
        ((IVec3dAccessor) data.get(data.size() - 1)).setRoll(((IVec3dAccessor) switchPos).getRoll());
    }
    @Inject(method = "getRenderData",
            at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 4),
            remap = false)
    public void inject4(CallbackInfoReturnable<List<BuilderBase.VecYawPitch>> cir, @Local(ordinal = 0) List data, @Local(ordinal = 1) PosStep switchPos) {
        ((IVec3dAccessor) data.get(data.size() - 1)).setRoll(((IVec3dAccessor) switchPos).getRoll());
    }
    @Inject(method = "getRenderData",
            at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 5),
            remap = false)
    public void inject5(CallbackInfoReturnable<List<BuilderBase.VecYawPitch>> cir, @Local(ordinal = 0) List data, @Local(ordinal = 1) PosStep switchPos) {
        ((IVec3dAccessor) data.get(data.size() - 1)).setRoll(((IVec3dAccessor) switchPos).getRoll());
    }
}
