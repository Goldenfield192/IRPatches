package com.goldenfield192.irpatches.mixins.immersiverailroading.model.part;

import cam72cam.immersiverailroading.model.part.PressureValve;
import cam72cam.mod.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PressureValve.class)
public class MixinPressureValve {
    @Redirect(method = "effects", at = @At(value = "INVOKE", target = "Lcam72cam/mod/math/Vec3d;subtract(Lcam72cam/mod/math/Vec3d;)Lcam72cam/mod/math/Vec3d;"), remap = false)
    public Vec3d redirect(Vec3d instance, Vec3d other){
        return instance;
    }
}
