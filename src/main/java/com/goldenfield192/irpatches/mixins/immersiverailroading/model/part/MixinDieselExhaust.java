package com.goldenfield192.irpatches.mixins.immersiverailroading.model.part;

import cam72cam.immersiverailroading.entity.LocomotiveDiesel;
import cam72cam.immersiverailroading.model.components.ModelComponent;
import cam72cam.immersiverailroading.model.part.DieselExhaust;
import cam72cam.mod.math.Vec3d;
import com.goldenfield192.irpatches.accessor.IStockRollAccessor;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(DieselExhaust.class)
public class MixinDieselExhaust {
    @Inject(method = "effects",
            at = @At(value = "INVOKE_ASSIGN", target = "Lcam72cam/mod/math/Vec3d;add(Lcam72cam/mod/math/Vec3d;)Lcam72cam/mod/math/Vec3d;"),
            remap = false)
    public void inject0(LocomotiveDiesel stock, CallbackInfo ci,
                        @Local ModelComponent exhaust, @Local(ordinal = 0) Vec3d fakeMotion, @Share("pos") LocalRef<Vec3d> vec3dLocalRef) {
        double rotation = Math.toRadians(
                (((IStockRollAccessor) stock).getFrontRoll() + ((IStockRollAccessor) stock).getRearRoll()) / 2);
        Vec3d particlePos = stock.getModelMatrix().rotate(rotation, 1, 0, 0).apply(exhaust.center);
        vec3dLocalRef.set(particlePos);
    }

    @ModifyArgs(method = "effects", at = @At(value = "INVOKE", target = "Lcam72cam/immersiverailroading/render/SmokeParticle$SmokeParticleData;<init>(Lcam72cam/mod/world/World;Lcam72cam/mod/math/Vec3d;Lcam72cam/mod/math/Vec3d;IFFDLcam72cam/mod/resource/Identifier;)V"), remap = false)
    public void mod(Args args, @Share("pos") LocalRef<Vec3d> vec3dLocalRef) {
        args.set(1, vec3dLocalRef.get());
    }
}
