package com.goldenfield192.irpatches.mixins.immersiverailroading.entity.physics;

import cam72cam.immersiverailroading.entity.EntityCoupleableRollingStock;
import cam72cam.immersiverailroading.entity.physics.SimulationState;
import cam72cam.immersiverailroading.tile.TileRailBase;
import cam72cam.immersiverailroading.util.VecUtil;
import cam72cam.mod.math.Vec3d;
import com.goldenfield192.irpatches.accessor.IStockRollAccessor;
import com.goldenfield192.irpatches.accessor.ITileRailBaseAccessor;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import trackapi.lib.ITrack;

@Mixin(SimulationState.class)
public class MixinSimulationState implements IStockRollAccessor {
    @Shadow public float yawFront;
    @Shadow public float yawRear;
    @Shadow public Vec3d couplerPositionRear;
    @Shadow public Vec3d couplerPositionFront;
    @Unique
    public float rollFront;
    @Unique
    public float rollRear;

    @Inject(method = "<init>(Lcam72cam/immersiverailroading/entity/EntityCoupleableRollingStock;)V", at = @At("TAIL"), remap = false)
    public void injectConstructor0(EntityCoupleableRollingStock stock, CallbackInfo ci){
        rollFront = ((IStockRollAccessor)stock).getFrontRoll();
        rollRear = ((IStockRollAccessor)stock).getRearRoll();
    }

    @Inject(method = "<init>(Lcam72cam/immersiverailroading/entity/physics/SimulationState;)V", at = @At("TAIL"), remap = false)
    public void injectConstructor1(SimulationState prev, CallbackInfo ci){
        rollFront = ((IStockRollAccessor)prev).getFrontRoll();
        rollRear = ((IStockRollAccessor)prev).getRearRoll();
    }

    @Inject(method = "moveAlongTrack", at = @At(value = "INVOKE_ASSIGN", target = "Lcam72cam/immersiverailroading/util/VecUtil;fromWrongYaw(DF)Lcam72cam/mod/math/Vec3d;", ordinal = 1), remap = false)
    public void inject0(double distance, CallbackInfo ci, @Local(ordinal = 0) ITrack trackFront, @Local(ordinal = 1) ITrack trackRear){
        Vec3d positionFront = couplerPositionFront;
        Vec3d positionRear = couplerPositionRear;

        boolean frontDirection = trackFront instanceof TileRailBase
                                 ? ((ITileRailBaseAccessor) trackFront).getDirectionAlong(positionFront, VecUtil.fromWrongYaw(distance, yawFront))
                                 : false;
        boolean rearDirection = trackRear instanceof TileRailBase
                                ? ((ITileRailBaseAccessor) trackRear).getDirectionAlong(positionRear, VecUtil.fromWrongYaw(distance, yawRear))
                                : false;

        rollFront = trackFront instanceof TileRailBase
                    ? ((ITileRailBaseAccessor) trackFront).getNextRoll(positionFront, VecUtil.fromWrongYaw(distance, yawFront))
                    : 0;
        rollFront *= frontDirection ? -1 : 1;
        rollRear = trackRear instanceof TileRailBase
                   ? ((ITileRailBaseAccessor) trackRear).getNextRoll(positionRear, VecUtil.fromWrongYaw(distance, yawRear))
                   : 0;
        rollRear *= rearDirection ? -1 : 1;
    }


    @Override
    public float getFrontRoll() {
        return rollFront;
    }

    @Override
    public void setFrontRoll(float val) {
        this.rollFront = val;
    }

    @Override
    public float getRearRoll() {
        return rollRear;
    }

    @Override
    public void setRearRoll(float val) {
        this.rollRear = val;
    }
}
