package com.goldenfield192.irpatches.mixins.features.transfer_table;

import cam72cam.immersiverailroading.library.TrackItems;
import cam72cam.immersiverailroading.physics.MovementTrack;
import cam72cam.immersiverailroading.tile.TileRail;
import cam72cam.immersiverailroading.util.VecUtil;
import cam72cam.mod.math.Rotation;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.world.World;
import com.goldenfield192.irpatches.accessor.IRailSettingsAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MovementTrack.class)
public class MixinMovementTrack {
    @Inject(method = "nextPositionDirect", at = @At(value = "INVOKE_ASSIGN", target = "Lcam72cam/immersiverailroading/library/Gauge;scale()D"), remap = false, cancellable = true)
    private static void inject(World world, Vec3d currentPosition, TileRail rail, Vec3d delta, CallbackInfoReturnable<Vec3d> cir){
        double railHeight = rail.info.getTrackHeight();
        double heightOffset = railHeight * rail.info.settings.gauge.scale();

        if (rail.info.settings.type == TrackItems.valueOf("TRANSFER_TABLE")) {
            IRailSettingsAccessor accessor = (IRailSettingsAccessor) rail.info.settings;
            double tablePos = rail.getParentTile().info.tablePos;

            int halfGauge = (int) Math.floor((rail.info.settings.gauge.value() * 1.1 + 0.5) / 2);
            int width = accessor.getTransferTableEntryDistance() * (accessor.getTransferTableEntryNum() - 1) + halfGauge + 2;
            Vec3i mainOffset = new Vec3i(-width / 2, 1, rail.info.settings.length/2);
            Vec3d start = new Vec3d(rail.getPos().subtract(mainOffset.rotate(Rotation.from(rail.info.placementInfo.facing()))));
            double xValue;
            switch (rail.info.placementInfo.facing()){
                case SOUTH:
                    xValue = -tablePos - rail.info.placementInfo.placementPosition.x % 1 - 1;
                    break;
                case NORTH:
                    xValue = -tablePos + rail.info.placementInfo.placementPosition.x % 1;
                    break;
                case EAST:
                    xValue = -tablePos + rail.info.placementInfo.placementPosition.z % 1;
                    break;
                case WEST:
                    xValue = -tablePos - rail.info.placementInfo.placementPosition.z % 1 - 1;
                    break;
                default:
                    //WTH
                    cir.setReturnValue(null);
                    return;
            }
            start = start.add(new Vec3d(xValue,  2 + heightOffset, rail.info.settings.length / 2).rotateYaw(
                            -rail.info.placementInfo.facing().getAngle() + 180));
            currentPosition = currentPosition.add(delta);

            double fromCenter = currentPosition.distanceTo(start);

            Vec3d forward = start.add(VecUtil.fromWrongYaw(fromCenter, -rail.info.placementInfo.facing().getAngle() + 180));
            Vec3d backward = start.add(VecUtil.fromWrongYaw(fromCenter, -rail.info.placementInfo.facing().getAngle()));

            if (forward.distanceToSquared(currentPosition) < backward.distanceToSquared(currentPosition)) {
                cir.setReturnValue(forward);
            } else {
                cir.setReturnValue(backward);
            }
            if(forward.distanceToSquared(currentPosition) == backward.distanceToSquared(currentPosition)){
                cir.setReturnValue(currentPosition.add(delta));
            }
            cir.cancel();
        }
    }
}
