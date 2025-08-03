package com.goldenfield192.irpatches.mixins.features.transfer_table;

import cam72cam.immersiverailroading.library.TrackItems;
import cam72cam.immersiverailroading.render.item.TrackBlueprintItemModel;
import cam72cam.immersiverailroading.util.RailInfo;
import cam72cam.mod.render.opengl.RenderState;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TrackBlueprintItemModel.class)
public class MixinTrackBluePrintItemModel {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lcam72cam/mod/render/opengl/RenderState;scale(DDD)Lcam72cam/mod/render/opengl/RenderState;"), remap = false)
    private static RenderState mod(RenderState instance, double x, double y, double z, @Local RailInfo info) {
        double multiplier = info.settings.type == TrackItems.valueOf("TRANSFER_TABLE") ? 0.25 : 1;
        return instance.scale(x*multiplier, y*multiplier, z*multiplier);
    }
}
