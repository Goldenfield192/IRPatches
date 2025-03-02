package com.goldenfield192.irpatches.mixins.render;

import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.opengl.RenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(OBJModel.Binder.class)
public class MixinOBJModel {
    @Inject(method = "apply", at = @At("TAIL"), remap = false, locals = LocalCapture.CAPTURE_FAILSOFT)
    public void mixinTransparent(CallbackInfo info, RenderState state){
        state.alpha_test(true);
    }
}
