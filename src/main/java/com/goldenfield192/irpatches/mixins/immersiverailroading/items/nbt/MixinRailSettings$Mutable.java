package com.goldenfield192.irpatches.mixins.immersiverailroading.items.nbt;

import cam72cam.immersiverailroading.items.nbt.RailSettings;
import cam72cam.immersiverailroading.library.*;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.serialization.TagCompound;
import com.goldenfield192.irpatches.accessor.IRailSettingsAccessor;
import com.goldenfield192.irpatches.accessor.IRailSettingsMutableAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RailSettings.Mutable.class)
public class MixinRailSettings$Mutable implements IRailSettingsMutableAccessor {
    @Shadow(remap = false)
    public Gauge gauge;
    @Shadow(remap = false)
    public String track;
    @Shadow(remap = false)
    public TrackItems type;
    @Shadow(remap = false)
    public int length;
    @Shadow(remap = false)
    public float degrees;
    @Shadow(remap = false)
    public float curvosity;
    @Shadow(remap = false)
    public TrackPositionType posType;
    @Shadow(remap = false)
    public TrackSmoothing smoothing;
    @Shadow(remap = false)
    public TrackDirection direction;
    @Shadow(remap = false)
    public ItemStack railBed;
    @Shadow(remap = false)
    public ItemStack railBedFill;
    @Shadow(remap = false)
    public boolean isPreview;
    @Shadow(remap = false)
    public boolean isGradeCrossing;
    @Unique
    public float IRPatch$ctrl1Roll;
    @Unique
    public float IRPatch$ctrl2Roll;

    @Override
    public void IRPatch$setNearEnd(float degree) {
        this.IRPatch$ctrl2Roll = degree;
    }

    @Override
    public void IRPatch$setFarEnd(float degree) {
        this.IRPatch$ctrl1Roll = degree;
    }

    @Override
    public float IRPatch$getNearEndTilt() {
        return IRPatch$ctrl2Roll;
    }

    @Override
    public float IRPatch$getFarEndTilt() {
        return IRPatch$ctrl1Roll;
    }

    @Inject(method = "<init>(Lcam72cam/immersiverailroading/items/nbt/RailSettings;)V", at = @At("TAIL"), remap = false)
    public void inject0(RailSettings settings, CallbackInfo ci) {
        this.IRPatch$setFarEnd(((IRailSettingsAccessor) settings).IRPatch$getFarEndTilt());
        this.IRPatch$setNearEnd(((IRailSettingsAccessor) settings).IRPatch$getNearEndTilt());
    }

    @Inject(method = "<init>(Lcam72cam/mod/serialization/TagCompound;)V", at = @At("TAIL"), remap = false)
    public void inject1(TagCompound data, CallbackInfo ci) {
        this.IRPatch$setFarEnd(0);
        this.IRPatch$setNearEnd(0);
    }

    @Inject(method = "immutable", at = @At("HEAD"), remap = false, cancellable = true)
    public void inject2(CallbackInfoReturnable<RailSettings> cir) {
        IRailSettingsAccessor settings = (IRailSettingsAccessor) new RailSettings(
                gauge,
                track,
                type,
                length,
                degrees,
                curvosity,
                posType,
                smoothing,
                direction,
                railBed,
                railBedFill,
                isPreview,
                isGradeCrossing
        );
        settings.IRPatch$setNearEnd(this.IRPatch$ctrl2Roll);
        settings.IRPatch$setFarEnd(this.IRPatch$ctrl1Roll);
        cir.setReturnValue((RailSettings) settings);
    }
}
