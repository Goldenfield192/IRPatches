package com.goldenfield192.irpatches.mixins.umc.math;

import cam72cam.mod.math.Vec3d;
import com.goldenfield192.irpatches.accessor.IVec3dAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Vec3d.class)
public abstract class MixinVec3d implements IVec3dAccessor {
    @Shadow(remap = false) @Final public double x;
    @Shadow(remap = false) @Final public double y;
    @Shadow(remap = false) @Final public double z;
    //I know this is ugly and should be avoided......but if so we have to change TrackAPI
    @Unique
    public float IRPatch$roll;

    @Override
    public void setRoll(float roll) {
        this.IRPatch$roll = roll;
    }

    @Override
    public float getRoll() {
        return IRPatch$roll;
    }

    @Inject(method = "add(Lcam72cam/mod/math/Vec3d;)Lcam72cam/mod/math/Vec3d;", at = @At("HEAD"), remap = false, cancellable = true)
    public void injectAdd(Vec3d other, CallbackInfoReturnable<Vec3d> cir){
        Vec3d vec3d = new Vec3d(this.x +other.x, this.y + other.y, this.z + other.z);
        ((IVec3dAccessor)vec3d).setRoll(((IVec3dAccessor)other).getRoll());
        cir.setReturnValue(vec3d);
    }

    @Inject(method = "subtract(Lcam72cam/mod/math/Vec3d;)Lcam72cam/mod/math/Vec3d;", at = @At("HEAD"), remap = false, cancellable = true)
    public void injectSubtract(Vec3d other, CallbackInfoReturnable<Vec3d> cir){
        Vec3d vec3d = new Vec3d(this.x - other.x, this.y - other.y, this.z - other.z);
        ((IVec3dAccessor)vec3d).setRoll(((IVec3dAccessor)other).getRoll());
        cir.setReturnValue(vec3d);
    }

    @Inject(method = "scale", at = @At("HEAD"), remap = false, cancellable = true)
    public void injectScale(double scale, CallbackInfoReturnable<Vec3d> cir){
        Vec3d vec3d = new Vec3d(this.x * scale, this.y * scale, this.z * scale);
        ((IVec3dAccessor)vec3d).setRoll(((IVec3dAccessor)this).getRoll());
        cir.setReturnValue(vec3d);
    }
}
