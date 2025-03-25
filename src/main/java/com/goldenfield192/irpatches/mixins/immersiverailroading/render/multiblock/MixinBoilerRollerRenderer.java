package com.goldenfield192.irpatches.mixins.immersiverailroading.render.multiblock;

import cam72cam.immersiverailroading.render.multiblock.BoilerRollerRender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

/**
 * Fix a mistake of boiler roller's render offset
 */
@Mixin(BoilerRollerRender.class)
public class MixinBoilerRollerRenderer {
    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lcam72cam/mod/render/opengl/RenderState;translate(DDD)Lcam72cam/mod/render/opengl/RenderState;", ordinal = 1), remap = false)
    public void modArgs(Args args) {
        args.set(0, -3.5);
    }
}
