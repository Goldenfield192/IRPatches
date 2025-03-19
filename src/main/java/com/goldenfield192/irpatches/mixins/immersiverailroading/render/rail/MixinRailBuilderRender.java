package com.goldenfield192.irpatches.mixins.immersiverailroading.render.rail;

import cam72cam.immersiverailroading.render.rail.RailBuilderRender;
import cam72cam.immersiverailroading.track.BuilderBase;
import cam72cam.immersiverailroading.util.RailInfo;
import cam72cam.mod.render.opengl.RenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(RailBuilderRender.class)
public class MixinRailBuilderRender {
    @Inject(method = "renderRailBuilder", at = @At(value = "INVOKE_ASSIGN", target = "Lutil/Matrix4;rotate(DDDD)Lutil/Matrix4;", ordinal = 1), remap = false, locals = LocalCapture.PRINT)
    private static void inject(RailInfo info, List<BuilderBase.VecYawPitch> renderData, RenderState state, CallbackInfo ci)
                               /*@Local Matrix4 m, @Local BuilderBase.VecYawPitch piece*/{
//        m.rotate(Math.toRadians(((IVec3dAccessor)piece).IRPatch$getRoll()), 0, 0, 1);
    }
}
