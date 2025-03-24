package com.goldenfield192.irpatches.mixins.immersiverailroading.render.rail;

import cam72cam.immersiverailroading.model.TrackModel;
import cam72cam.immersiverailroading.render.rail.RailBuilderRender;
import cam72cam.immersiverailroading.track.BuilderBase;
import cam72cam.immersiverailroading.util.RailInfo;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.render.opengl.VBO;
import com.goldenfield192.irpatches.accessor.IVec3dAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import util.Matrix4;

import java.util.Iterator;
import java.util.List;

@Mixin(RailBuilderRender.class)
public class MixinRailBuilderRender {
    @Inject(method = "renderRailBuilder", at = @At(value = "INVOKE_ASSIGN", target = "Lutil/Matrix4;rotate(DDDD)Lutil/Matrix4;", ordinal = 1), remap = false, locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void inject(RailInfo info, List renderData, RenderState state, CallbackInfo ci, TrackModel model,
                        VBO cached, OBJRender.Builder builder, Iterator var6, BuilderBase.VecYawPitch piece, Matrix4 m){
        double radians = Math.toRadians(((IVec3dAccessor) piece).getRoll());
        double sin = Math.sin(radians);
        double cos = Math.cos(radians);
        double target = info.settings.gauge.scale() * info.getTrackHeight() * sin;
        m.rotate(radians, 0, 0, 1);
        m.translate(target * cos, target * sin, 0);
    }
}
