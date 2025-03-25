package com.goldenfield192.irpatches.mixins.immersiverailroading.model.part;

import cam72cam.immersiverailroading.ConfigGraphics;
import cam72cam.immersiverailroading.entity.EntityMoveableRollingStock;
import cam72cam.mod.ModCore;
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

    @Shadow(remap = false) private double swayMagnitude;

    @Inject(method = "getRollDegrees", at = @At("HEAD"), remap = false, cancellable = true)
    public void inject0(float partialTicks, CallbackInfoReturnable<Double> cir){
        IStockRollAccessor accessor = (IStockRollAccessor) this.stock;
        double track = (accessor.getRearRoll() + accessor.getFrontRoll()) / 2d;
        if (Math.abs(this.stock.getCurrentSpeed().metric() * this.stock.gauge.scale()) >= 4.0) {
            double sway = Math.cos(Math.toRadians(
                    ((float)this.stock.getTickCount() + partialTicks) * 13.0F)) * this.swayMagnitude / 5.0 * this.stock.getDefinition().getSwayMultiplier() * ConfigGraphics.StockSwayMultiplier;
            double tilt = this.stock.getDefinition().getTiltMultiplier() * (double)(this.stock.getPrevRotationYaw() - this.stock.getRotationYaw()) * (double)(this.stock.getCurrentSpeed().minecraft() > 0.0 ? 1 : -1);
            track += sway + tilt;
        }
        cir.setReturnValue(track);
    }
}
