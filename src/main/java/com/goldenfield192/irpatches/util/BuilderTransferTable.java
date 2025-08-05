package com.goldenfield192.irpatches.util;

import cam72cam.immersiverailroading.track.*;
import cam72cam.immersiverailroading.util.RailInfo;
import cam72cam.mod.math.Rotation;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.world.World;
import com.goldenfield192.irpatches.accessor.IRailSettingsAccessor;

import java.util.ArrayList;
import java.util.List;

public class BuilderTransferTable extends BuilderBase {
    private Vec3i mainOffset;

    public BuilderTransferTable(RailInfo info, World world, Vec3i pos) {
        super(info.withSettings(b -> b.length = Math.min(info.settings.length, BuilderTurnTable.maxLength(info.settings.gauge))), world, pos);

        int vertMin = -info.settings.length / 2;
        int vertMax = info.settings.length % 2 == 0 ? info.settings.length/2 : info.settings.length/2 + 1;
        IRailSettingsAccessor accessor = (IRailSettingsAccessor) info.settings;

        int halfGauge = (int) Math.floor((info.settings.gauge.value() * 1.1 + 0.5) / 2);
        int width = accessor.getTransferTableEntryDistance() * (accessor.getTransferTableEntryNum() - 1) + halfGauge + 2;

        mainOffset = new Vec3i(-width / 2, 1, info.settings.length/2);
        mainOffset = mainOffset.rotate(Rotation.from(info.placementInfo.facing()));

        this.setParentPos(mainOffset.down());
        TrackRail main = new TrackRail(this, mainOffset.down());
        tracks.add(main);
        for(int i = vertMin; i < vertMax; i++){
            for(int j = -halfGauge - 1 - width / 2; j < width - width / 2; j++){
                TrackGag gag = new TrackGag(this, mainOffset.add(
                        new Vec3i(-j, 0, i).rotate(Rotation.from(info.placementInfo.facing()))));
                TrackGag gag1 = new TrackGag(this, mainOffset.add(
                        new Vec3i(-j, -1, i).rotate(Rotation.from(info.placementInfo.facing()))));
                gag.solidNotRequired = true;
                gag.setHeight(0.000001f);
                if(i == vertMin || i == vertMax - 1 || j == -halfGauge -1 -width / 2 || j == width - 1 -width / 2) {
                    gag1.setBedHeight(1);
                    gag1.setFlexible();
                    gag.setHeight(0);
                    gag.setFlexible();
                }
                tracks.add(gag);
                tracks.add(gag1);
            }
        }

//        this.first = new Vec3i(info.placementInfo.placementPosition);
//        this.rot = Rotation.from(info.placementInfo.facing().getOpposite());
    }

    @Override
    public List<VecYawPitch> getRenderData() {
        List<VecYawPitch> list = new ArrayList<>();
        IRailSettingsAccessor accessor = (IRailSettingsAccessor) info.settings;

        if (info.itemHeld) {
            for (int i = 0; i < info.settings.length; i += info.settings.length - 1) {
                for (int j = 0; j < accessor.getTransferTableEntryNum(); j++) {
                    Vec3i vec = new Vec3i(-j * accessor.getTransferTableEntryDistance(), 1, i)
                            .rotate(Rotation.from(info.placementInfo.facing()));
                    list.add(new VecYawPitch(vec.x, vec.y, vec.z, info.placementInfo.facing().getAngle()));
                }
            }
        }

        Vec3d vec = new Vec3d(-info.tablePos, 1, info.settings.length / 2).rotateYaw(-info.placementInfo.facing().getAngle() + 180);
        list.add(new VecYawPitch(vec.x, vec.y, vec.z, info.placementInfo.facing().getAngle(), 0, info.settings.length, "RAIL_RIGHT", "RAIL_LEFT"));
        return list;
    }

    @Override
    public List<TrackBase> getTracksForRender() {
        return this.tracks;
    }
}
