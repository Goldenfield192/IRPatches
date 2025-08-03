package com.goldenfield192.irpatches.util;

import cam72cam.immersiverailroading.track.BuilderBase;
import cam72cam.immersiverailroading.track.BuilderTurnTable;
import cam72cam.immersiverailroading.track.TrackGag;
import cam72cam.immersiverailroading.track.TrackRail;
import cam72cam.immersiverailroading.util.RailInfo;
import cam72cam.mod.math.Rotation;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.world.World;

import java.util.Collections;
import java.util.List;

public class BuilderTransferTable extends BuilderBase {
    private Vec3i offset;

    public BuilderTransferTable(RailInfo info, World world, Vec3i pos) {
        super(info.withSettings(b -> b.length = Math.min(info.settings.length, BuilderTurnTable.maxLength(info.settings.gauge))), world, pos);

        offset = new Vec3i(0, 1, info.settings.length/2);
        offset = offset.rotate(Rotation.from(info.placementInfo.facing()));

        this.setParentPos(offset.down());
        TrackRail main = new TrackRail(this, offset.down());
        tracks.add(main);

        int max = info.settings.length % 2 == 0 ? info.settings.length/2 : info.settings.length/2 + 1;

        int half = (int) Math.floor((info.settings.gauge.value() * 1.1 + 0.5) / 2);
        //In length
        for(int i = -info.settings.length/2; i < max; i++){
            for(int j = -half; j < 15; j++){
                TrackGag gag = new TrackGag(this, offset.add(
                        new Vec3i(i, 0, j).rotate(Rotation.from(info.placementInfo.facing().rotate(Rotation.CLOCKWISE_90)))));
                tracks.add(gag);
            }
        }
    }

    @Override
    public List<VecYawPitch> getRenderData() {
        return Collections.singletonList(new VecYawPitch(0,0,0,0));
    }
}
