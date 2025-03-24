package com.goldenfield192.irpatches.mixins.immersiverailroading.model.part;

import cam72cam.immersiverailroading.entity.EntityMoveableRollingStock;
import com.goldenfield192.irpatches.accessor.IStockRollAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "cam72cam/immersiverailroading/model/part/SwaySimulator$Effect")
public class MixinSwaySimulator$Effect {
    @Shadow(remap = false)
    @Final
    private EntityMoveableRollingStock stock;

    @Inject(method = "getRollDegrees", at = @At("HEAD"), remap = false, cancellable = true)
    public void inject0(float partialTicks, CallbackInfoReturnable<Double> cir){
        IStockRollAccessor accessor = (IStockRollAccessor) this.stock;
        cir.setReturnValue((accessor.getRearRoll() + accessor.getFrontRoll()) / 2d);
    }
}
