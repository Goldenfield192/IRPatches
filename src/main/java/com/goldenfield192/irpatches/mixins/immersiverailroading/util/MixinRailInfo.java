package com.goldenfield192.irpatches.mixins.immersiverailroading.util;

import cam72cam.immersiverailroading.items.nbt.RailSettings;
import cam72cam.immersiverailroading.util.RailInfo;
import com.goldenfield192.irpatches.accessor.IRailSettingsAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.include.com.google.common.collect.ObjectArrays;

import java.util.Arrays;

@Mixin(RailInfo.class)
public class MixinRailInfo {
    @Shadow(remap = false)
    @Final
    public RailSettings settings;

    @Redirect(method = "generateID", at = @At(value = "INVOKE", target = "Ljava/util/Arrays;toString([Ljava/lang/Object;)Ljava/lang/String;"), remap = false)
    public String inject(Object[] objects) {
        Object[] tilt = {((IRailSettingsAccessor) settings).getFarEndTilt(), ((IRailSettingsAccessor) settings).getNearEndTilt(), ((IRailSettingsAccessor) settings).getBumpiness()};
        objects = ObjectArrays.concat(objects, tilt, Object.class);
        return Arrays.toString(objects);
    }
}
