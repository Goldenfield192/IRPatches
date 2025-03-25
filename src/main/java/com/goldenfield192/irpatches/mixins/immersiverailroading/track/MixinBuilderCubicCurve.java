package com.goldenfield192.irpatches.mixins.immersiverailroading.track;

import cam72cam.immersiverailroading.items.nbt.RailSettings;
import cam72cam.immersiverailroading.library.SwitchState;
import cam72cam.immersiverailroading.library.TrackItems;
import cam72cam.immersiverailroading.track.*;
import cam72cam.immersiverailroading.util.PlacementInfo;
import cam72cam.immersiverailroading.util.RailInfo;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.world.World;
import com.goldenfield192.irpatches.accessor.IBuilderBaseAccessor;
import com.goldenfield192.irpatches.accessor.IRailSettingsAccessor;
import com.goldenfield192.irpatches.accessor.IVec3dAccessor;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(BuilderCubicCurve.class)
public abstract class MixinBuilderCubicCurve extends BuilderIterator {
    @Shadow(remap = false) private List<BuilderBase> subBuilders;

    private MixinBuilderCubicCurve(RailInfo info, World world, Vec3i pos) {
        super(info, world, pos);
    }

    @Inject(method = "<init>(Lcam72cam/immersiverailroading/util/RailInfo;Lcam72cam/mod/world/World;Lcam72cam/mod/math/Vec3i;Z)V", at = @At("TAIL"), remap = false)
    public void inject0(RailInfo info, World world, Vec3i pos, boolean endOfTrack, CallbackInfo ci, @Local(ordinal = 0) List<CubicCurve> subCurves){
        if (subCurves.size() > 1) {
            subBuilders = new ArrayList<>();
            int splits = subCurves.size();
            for (int i = 0; i < subCurves.size(); i++) {
                CubicCurve subCurve = subCurves.get(i);
                // main pos -> subCurve's start pos
                Vec3d relOff = info.placementInfo.placementPosition.add(subCurve.p1);
                Vec3i relPos = new Vec3i(relOff);
                Vec3i sPos = pos.add(relPos);
                // The block remainder of curve position, with the subCurve move to origin block included
                Vec3d delta = relOff.subtract(relPos).subtract(subCurve.p1);
                //delta = delta.subtract(new Vec3i(delta)); // Relative position within the block
                PlacementInfo startPos = new PlacementInfo(subCurve.p1.add(delta), info.placementInfo.direction, subCurve.angleStart(), subCurve.ctrl1.add(delta));
                PlacementInfo endPos   = new PlacementInfo(subCurve.p2.add(delta), info.placementInfo.direction, subCurve.angleStop(), subCurve.ctrl2.add(delta));


                int finalI = i;
                IRailSettingsAccessor accessor = (IRailSettingsAccessor) info.settings;
                RailSettings clone = getRailSetting(info.settings).with(b -> {
                    ((IRailSettingsAccessor) b).setNearEnd(
                            (accessor.getNearEndTilt() * finalI / splits)
                                    + (accessor.getFarEndTilt() * (1 - (float) finalI / splits)));
                    ((IRailSettingsAccessor) b).setFarEnd(
                            (accessor.getNearEndTilt() * (finalI + 1) / splits)
                                    + (accessor.getFarEndTilt() * (1 - (float) (finalI + 1) / splits)));
                });
                RailInfo subInfo = new RailInfo(clone.with(b -> b.type = TrackItems.CUSTOM),
                                                startPos, endPos, SwitchState.NONE, SwitchState.NONE, 0);

                BuilderCubicCurve subBuilder = new BuilderCubicCurve(subInfo, world, sPos);
                IBuilderBaseAccessor accessor1 = (IBuilderBaseAccessor) subBuilder;
                if (!subBuilders.isEmpty()) {
                    for (TrackBase track : accessor1.getTracks()) {
                        if (track instanceof TrackRail) {
                            track.overrideParent(subBuilders.get(0).getParentPos());
                        }
                    }
                } else {
                    tracks = accessor1.getTracks();
                }
                subBuilders.add(subBuilder);
            }
        }
    }

    @Inject(method = "getPath", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"), remap = false)
    public void inject(double stepSize, CallbackInfoReturnable<List> cir,
                       @Local(ordinal = 0) List res, @Local(ordinal = 1) List points, @Local(ordinal = 0) int i){
        PosStep step = (PosStep) res.get(res.size() - 1);

        float roll;
        IRailSettingsAccessor settingsAccessor = (IRailSettingsAccessor) info.settings;
        if (points.size() == 1) {
            roll = (settingsAccessor.getFarEndTilt() + settingsAccessor.getNearEndTilt()) / 2;
        } else if (i == points.size()-1) {
            roll = settingsAccessor.getFarEndTilt();
        } else if (i == 0) {
            roll = settingsAccessor.getNearEndTilt();
        } else {
            float percent = (i+1f) / points.size();
            float factor = (float) ((1.0 - Math.cos(Math.PI * percent)) / 2.0);
            roll = settingsAccessor.getNearEndTilt() + (settingsAccessor.getFarEndTilt() - settingsAccessor.getNearEndTilt()) * factor;
        }
        ((IVec3dAccessor)step).setRoll(roll);
    }

    @Unique
    private RailSettings getRailSetting(RailSettings another){

        return new RailSettings(another.gauge,
             another.track,
             another.type,
             another.length,
             another.degrees,
             another.curvosity,
             another.posType,
             another.smoothing,
             another.direction,
             another.railBed,
             another.railBedFill,
             another.isPreview,
             another.isGradeCrossing);
    }
}
