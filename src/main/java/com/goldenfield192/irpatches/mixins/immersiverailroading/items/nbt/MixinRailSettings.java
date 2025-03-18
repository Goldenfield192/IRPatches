package com.goldenfield192.irpatches.mixins.immersiverailroading.items.nbt;

import cam72cam.immersiverailroading.items.nbt.RailSettings;
import com.goldenfield192.irpatches.accessor.IRailSettingsAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(RailSettings.class)
public class MixinRailSettings implements IRailSettingsAccessor {
    @Unique
    public float iRPatch$ctrl1Roll;
    @Unique
    public float iRPatch$ctrl2Roll;

    @Override
    public void setNearEnd(float degree) {
        this.iRPatch$ctrl2Roll = degree;
    }

    @Override
    public void setFarEnd(float degree) {
        this.iRPatch$ctrl1Roll = degree;
    }

    @Override
    public float getNearEndTilt() {
        return iRPatch$ctrl2Roll;
    }

    @Override
    public float getFarEndTilt() {
        return iRPatch$ctrl1Roll;
    }
}
