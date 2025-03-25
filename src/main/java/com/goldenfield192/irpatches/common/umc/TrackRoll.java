package com.goldenfield192.irpatches.common.umc;

import cam72cam.immersiverailroading.ImmersiveRailroading;
import cam72cam.immersiverailroading.library.TrackItems;
import cam72cam.immersiverailroading.tile.TileRail;
import cam72cam.immersiverailroading.track.IIterableTrack;
import cam72cam.immersiverailroading.track.PosStep;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.world.World;
import com.goldenfield192.irpatches.accessor.IVec3dAccessor;

import java.util.List;

public class TrackRoll {
    public static float getRollMovementTrack(World world, Vec3d currentPosition, TileRail rail, Vec3d delta) {
        if(rail == null) {
            return 0;
        }
        double railHeight = rail.info.getTrackHeight();
        double heightOffset = railHeight * rail.info.settings.gauge.scale();

        if(rail.info.settings.type == TrackItems.CROSSING) {
            return 0;
        } else if(rail.info.settings.type == TrackItems.TURNTABLE) {
            return 0;
        } else if(rail.info.getBuilder(world) instanceof IIterableTrack) {
            List<PosStep> positions = ((IIterableTrack) rail.info.getBuilder(world)).getPath(
                    0.25 * rail.info.settings.gauge.scale());
            Vec3d center = rail.info.placementInfo.placementPosition.add(rail.getPos()).add(0, heightOffset, 0);
            Vec3d target = currentPosition.add(delta);
            Vec3d relative = target.subtract(center);

            if(positions.isEmpty()) {
                ImmersiveRailroading.error("Invalid track path %s", rail.info.uniqueID);
                return 0; // keep in same place for debugging
            }
            if(positions.size() == 1) {
                // track with length == 1
                PosStep pos = positions.get(0);
                return ((IVec3dAccessor) pos).getRoll();
            }

            int left = 0;
            double leftDistance = positions.get(left).distanceToSquared(relative);
            int right = positions.size() - 1;
            double rightDistance = positions.get(right).distanceToSquared(relative);
            while(right - left > 1) {
                if(leftDistance > rightDistance) {
                    left = (int) Math.ceil(left + (right - left) / 3f);
                    leftDistance = positions.get(left).distanceToSquared(relative);
                } else {
                    right = (int) Math.floor(right + (left - right) / 3f);
                    rightDistance = positions.get(right).distanceToSquared(relative);
                }
            }
            if(right == left) {
                ImmersiveRailroading.warn("Correcting track pathing tree...");
                // Hack for edge case
                if(right == positions.size() - 1) {
                    left -= 1;
                } else {
                    right += 1;
                }
            }

            PosStep leftPos = positions.get(left);
            PosStep rightPos = positions.get(right);

            if(leftDistance < 0.000001) {
                return ((IVec3dAccessor) leftPos).getRoll();
            }
            if(rightDistance < 0.000001) {
                return ((IVec3dAccessor) rightPos).getRoll();
            }

            Vec3d between = rightPos.subtract(leftPos);
            Vec3d offset = between.scale(Math.sqrt(leftDistance) / between.length());
            // Weird edge case where we need to move in the opposite direction since we are given a position past the end of pathing
            Vec3d point = center.add(leftPos);
            Vec3d result = point.add(offset);
            Vec3d resultOpposite = point.subtract(offset);
            return (((IVec3dAccessor) rightPos).getRoll() + ((IVec3dAccessor) leftPos).getRoll()) / 2;
        } else {
            return 0;
        }
    }
}
