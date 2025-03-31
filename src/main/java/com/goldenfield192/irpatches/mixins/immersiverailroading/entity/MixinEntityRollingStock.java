package com.goldenfield192.irpatches.mixins.immersiverailroading.entity;

import cam72cam.immersiverailroading.entity.EntityMoveableRollingStock;
import cam72cam.immersiverailroading.entity.EntityRollingStock;
import cam72cam.immersiverailroading.library.Gauge;
import cam72cam.mod.entity.CustomEntity;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.util.SingleCache;
import com.goldenfield192.irpatches.accessor.IStockRollAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import util.Matrix4;

@Mixin(EntityRollingStock.class)
public class MixinEntityRollingStock extends CustomEntity {
    @Shadow(remap = false) private SingleCache<Vec3d, Matrix4> modelMatrix;

    @Shadow(remap = false) public Gauge gauge;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void inject(CallbackInfo ci){
        EntityRollingStock self = (EntityRollingStock)(Object)this;
        this.modelMatrix = new SingleCache<>(v -> new Matrix4()
                .translate(this.getPosition().x, this.getPosition().y, this.getPosition().z)
                .rotate(Math.toRadians(180 - this.getRotationYaw()), 0, 1, 0)
                .rotate(Math.toRadians(this.getRotationPitch()), 1, 0, 0)
                .rotate(Math.toRadians(-90), 0, 1, 0)
                .rotate(Math.toRadians(
                        (((IStockRollAccessor) (EntityMoveableRollingStock) self).getFrontRoll() +
                         ((IStockRollAccessor) (EntityMoveableRollingStock) self).getRearRoll()) / 2), 1, 0, 0)
                .scale(this.gauge.scale(), this.gauge.scale(), this.gauge.scale()));
    }
}
