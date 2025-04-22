package com.goldenfield192.irpatches.mixins.immersiverailroading.model;

import cam72cam.immersiverailroading.entity.Freight;
import cam72cam.immersiverailroading.model.FreightModel;
import cam72cam.mod.render.opengl.RenderState;
import com.goldenfield192.irpatches.accessor.IStockRollAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FreightModel.class)
public class MixinFreightModel {
    @Inject(method = "postRender(Lcam72cam/immersiverailroading/entity/Freight;Lcam72cam/mod/render/opengl/RenderState;F)V",
            at = @At(value = "INVOKE", target = "Lcam72cam/immersiverailroading/model/part/CargoItems;postRender(Lcam72cam/immersiverailroading/entity/Freight;Lcam72cam/mod/render/opengl/RenderState;)V"), remap = false)
    public void inject(Freight stock, RenderState state, float partialTicks, CallbackInfo ci){
        IStockRollAccessor accessor = (IStockRollAccessor)stock;
        state.rotate((accessor.getFrontRoll() + accessor.getRearRoll())/2, 1, 0, 0);
    }
}
