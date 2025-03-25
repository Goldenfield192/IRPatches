package com.goldenfield192.irpatches.mixins.immersiverailroading.model.part;

import cam72cam.immersiverailroading.entity.EntityMoveableRollingStock;
import cam72cam.immersiverailroading.model.ModelState;
import cam72cam.immersiverailroading.model.components.ModelComponent;
import cam72cam.immersiverailroading.model.part.LightFlare;
import cam72cam.immersiverailroading.registry.EntityRollingStockDefinition;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.render.Light;
import cam72cam.mod.render.opengl.RenderState;
import com.goldenfield192.irpatches.common.umc.ExtraDefinition;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;

@Mixin(LightFlare.class)
public abstract class MixinLightFlare {
    @Unique
    public boolean IRPatches$useTex = true;
    @Unique
    public String IRPatch$disableOn = null;
    @Shadow(remap = false)
    @Final
    private Map<UUID, List<Light>> castLights;
    @Shadow(remap = false)
    @Final
    private Map<UUID, List<Vec3d>> castPositions;

    @Shadow(remap = false)
    public abstract void removed(EntityMoveableRollingStock stock);

    @Inject(method = "<init>", at = @At("TAIL"), remap = false, locals = LocalCapture.CAPTURE_FAILSOFT)
    public void injectConstructor(EntityRollingStockDefinition def, ModelState state, ModelComponent component, CallbackInfo ci, Matcher rgbValues, EntityRollingStockDefinition.LightDefinition config, ModelState mystate) {
        ExtraDefinition.LightDefinition light = ExtraDefinition.get(def)
                .extraLightDef.get(component.type.toString()
                                                 .replace("_X", "_" + component.id)
                                                 .replace("_POS_", "_" + component.pos + "_"));
        if(light != null) {
            this.IRPatches$useTex = light.enableTex;
            this.IRPatch$disableOn = light.disableOn;
        }
    }

    @Inject(method = "postRender", at = @At("HEAD"), remap = false, cancellable = true)
    public void inject1(EntityMoveableRollingStock stock, RenderState state, CallbackInfo ci) {
        if(!IRPatches$useTex || !IRPatch$isEnabled(stock)) {
            ci.cancel();
        }
    }

    @Inject(method = "effects", at = @At("HEAD"), remap = false, cancellable = true)
    public void inject2(EntityMoveableRollingStock stock, CallbackInfo ci) {
        if(!IRPatch$isEnabled(stock)) {
            this.removed(stock);
            ci.cancel();
        }
    }

    @Unique
    private boolean IRPatch$isEnabled(EntityMoveableRollingStock stock) {
        if(IRPatch$disableOn == null) {
            return true;
        }
        //When disableOn and speed corresponds then return false
        return IRPatch$disableOn.equals("forward") && stock.getCurrentSpeed().minecraft() < 0.0
                || IRPatch$disableOn.equals("reverse") && stock.getCurrentSpeed().minecraft() >= 0.0;
    }
}
