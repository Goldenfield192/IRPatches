package com.goldenfield192.irpatches.mixins.features.transfer_table;

import cam72cam.immersiverailroading.library.TrackItems;
import cam72cam.immersiverailroading.render.rail.RailBuilderRender;
import cam72cam.immersiverailroading.util.RailInfo;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(RailBuilderRender.class)
public class MixinRailBuilderRender {
    @ModifyArgs(method = "renderRailBuilder", at = @At(value = "INVOKE", target = "Lcam72cam/mod/render/opengl/VBO;bind(Lcam72cam/mod/render/opengl/RenderState;Z)Lcam72cam/mod/render/opengl/VBO$Binding;"), remap = false)
    private static void mod(Args args, @Local RailInfo info){
        args.set(1, (boolean)args.get(1)&&info.settings.type == TrackItems.valueOf("TRANSFER_TABLE"));
    }
}
