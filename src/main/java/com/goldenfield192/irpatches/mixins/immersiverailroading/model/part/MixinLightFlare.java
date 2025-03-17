package com.goldenfield192.irpatches.mixins.immersiverailroading.model.part;

import cam72cam.immersiverailroading.entity.EntityRollingStock;
import cam72cam.immersiverailroading.model.ModelState;
import cam72cam.immersiverailroading.model.components.ModelComponent;
import cam72cam.immersiverailroading.model.part.LightFlare;
import cam72cam.immersiverailroading.registry.EntityRollingStockDefinition;
import com.goldenfield192.irpatches.common.umc.ExtraDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.regex.Matcher;

@Mixin(LightFlare.class)
public class MixinLightFlare<T extends EntityRollingStock> {
    @Unique
    public boolean IRPatches$useTex = true;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false, locals = LocalCapture.CAPTURE_FAILSOFT)
    public void injectConstructor(EntityRollingStockDefinition def, ModelState state, ModelComponent component, CallbackInfo ci, Matcher rgbValues, EntityRollingStockDefinition.LightDefinition config, ModelState mystate){
        ExtraDefinition.LightDefinition light = ExtraDefinition.get(def)
                .extraLightDef.get(component.type.toString()
                .replace("_X", "_" + component.id)
                .replace("_POS_", "_" + component.pos + "_"));
        if(light != null){
            this.IRPatches$useTex = light.enableTex;
        }
    }

    @Inject(method = "postRender", at = @At("HEAD"), remap = false, cancellable = true)
    public void inject2(CallbackInfo ci){
        if(!IRPatches$useTex){
            ci.cancel();
        }
    }
}
