package com.goldenfield192.irpatches.mixins.features.transfer_table;

import cam72cam.immersiverailroading.entity.physics.SimulationState;
import cam72cam.immersiverailroading.library.TrackItems;
import cam72cam.immersiverailroading.physics.MovementTrack;
import cam72cam.immersiverailroading.thirdparty.trackapi.ITrack;
import cam72cam.immersiverailroading.tile.TileRailBase;
import cam72cam.immersiverailroading.util.VecUtil;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.util.DegreeFuncs;
import cam72cam.mod.util.FastMath;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SimulationState.class)
public class MixinSimulationState {
    @Shadow(remap = false) public SimulationState.Configuration config;
    @Shadow(remap = false) public float yaw;
    @Shadow(remap = false) public float pitch;
    @Shadow(remap = false) public Vec3d position;
    @Shadow(remap = false) public float yawFront;
    @Shadow(remap = false) public float yawRear;

    @Inject(method = "moveAlongTrack", at = @At("HEAD"), remap = false, cancellable = true)
    public void inject(double distance, CallbackInfo ci){
        Vec3d positionFront = VecUtil.fromWrongYawPitch(config.offsetFront, yaw, pitch).add(position);
        Vec3d positionRear = VecUtil.fromWrongYawPitch(config.offsetRear, yaw, pitch).add(position);

        // Find tracks
        ITrack trackFront = MovementTrack.findTrack(config.world, positionFront, yawFront, config.gauge.value());
        ITrack trackRear = MovementTrack.findTrack(config.world, positionRear, yawRear, config.gauge.value());
        if (trackFront == null || trackRear == null) {
            ci.cancel();
            return;
        }

        boolean isTransferTable = false;
        if (Math.abs(distance) < 0.0001) {
            TileRailBase frontBase = trackFront instanceof TileRailBase ? (TileRailBase) trackFront : null;
            TileRailBase rearBase  = trackRear instanceof TileRailBase ? (TileRailBase) trackRear : null;
            isTransferTable = frontBase != null &&
                    (
                            frontBase.getParentTile() != null &&
                                    frontBase.getParentTile().info.settings.type == TrackItems.valueOf("TRANSFER_TABLE")
                    );
            isTransferTable = isTransferTable || rearBase != null &&
                    (
                            rearBase.getParentTile() != null &&
                                    rearBase.getParentTile().info.settings.type == TrackItems.valueOf("TRANSFER_TABLE")
                    );
            boolean isTurnTable = frontBase != null &&
                    (
                            //frontBase.getTicksExisted() < 100 ||
                            frontBase.getParentTile() != null &&
                                    frontBase.getParentTile().info.settings.type == TrackItems.TURNTABLE
                    );
            isTurnTable = isTurnTable || rearBase != null &&
                    (
                            //rearBase.getTicksExisted() < 100 ||
                            rearBase.getParentTile() != null &&
                                    rearBase.getParentTile().info.settings.type == TrackItems.TURNTABLE
                    );

            if (isTurnTable || !isTransferTable) {
                return;
            }
        }

        boolean isReversed = distance < 0;
        if (isReversed) {
            distance = -distance;
            yawFront += 180;
            yawRear += 180;
        }

        Vec3d nextFront = trackFront.getNextPosition(positionFront, VecUtil.fromWrongYaw(distance, yawFront));
        Vec3d nextRear = trackRear.getNextPosition(positionRear, VecUtil.fromWrongYaw(distance, yawRear));

        if (!nextFront.equals(positionFront) && !nextRear.equals(positionRear)) {
            yawFront = VecUtil.toWrongYaw(nextFront.subtract(positionFront));
            yawRear = VecUtil.toWrongYaw(nextRear.subtract(positionRear));

            // TODO flatten this vector calculation
            Vec3d deltaCenter = nextFront.subtract(position).scale(config.offsetRear)
                                         .subtract(nextRear.subtract(position).scale(config.offsetFront))
                                         .scale(-1/(config.offsetFront-config.offsetRear));

            Vec3d bogeyDelta = nextFront.subtract(nextRear);
            yaw = VecUtil.toWrongYaw(bogeyDelta);
            pitch = (float) Math.toDegrees(FastMath.atan2(bogeyDelta.y, nextRear.distanceTo(nextFront)));
            // TODO Rescale fixes issues with curves losing precision, but breaks when correcting stock positions
            position = position.add(deltaCenter/*.normalize().scale(distance)*/);
        }

        if (isReversed) {
            yawFront += 180;
            yawRear += 180;
        }

        if (isTransferTable) {
            yawFront = yaw;
            yawRear = yaw;
        }

        // Fix bogeys pointing in opposite directions
        if (DegreeFuncs.delta(yawFront, yaw) > 90 || DegreeFuncs.delta(yawFront, yawRear) > 90) {
            yawFront = yaw;
            yawRear = yaw;
        }
        ci.cancel();
    }
}
