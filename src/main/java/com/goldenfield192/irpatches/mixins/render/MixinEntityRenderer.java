package com.goldenfield192.irpatches.mixins.render;

import cam72cam.mod.entity.ModdedEntity;
import cam72cam.mod.render.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = EntityRenderer.class, remap = false)
public class MixinEntityRenderer {
//    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
//    public void mixinTranslucent(RenderManager factory, CallbackInfo ci){
//
//    }

    @Redirect(method = "renderLargeEntities", at = @At(value = "INVOKE_ASSIGN", target = "Lcam72cam/mod/render/GlobalRender;isTransparentPass()Z"), remap = false)
    private static boolean redirect(){
        return false;
    }

    @Inject(method = "doRender", at = @At(value = "INVOKE_ASSIGN", target = "Lcam72cam/mod/render/opengl/RenderState;rotate(DDDD)Lcam72cam/mod/render/opengl/RenderState;", ordinal = 0), remap = false, locals = LocalCapture.PRINT)
    public void mixinDoRender(ModdedEntity stock, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci){

    }
}
