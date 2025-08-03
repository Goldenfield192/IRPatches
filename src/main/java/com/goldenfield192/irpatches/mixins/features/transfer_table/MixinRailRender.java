package com.goldenfield192.irpatches.mixins.features.transfer_table;

import cam72cam.immersiverailroading.library.TrackItems;
import cam72cam.immersiverailroading.render.rail.RailRender;
import cam72cam.immersiverailroading.util.RailInfo;
import cam72cam.mod.render.opengl.RenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RailRender.class)
public abstract class MixinRailRender {
    @Shadow(remap = false) @Final private RailInfo info;

    @Shadow(remap = false) public abstract void load();

    @Inject(method = "renderRailModel", at = @At("HEAD"), remap = false)
    public void inject(RenderState state, CallbackInfo ci){
        if (info.settings.type == TrackItems.TURNTABLE) {
            load();
        }
    }
}
