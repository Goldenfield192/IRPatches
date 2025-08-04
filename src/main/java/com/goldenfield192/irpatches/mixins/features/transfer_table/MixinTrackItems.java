package com.goldenfield192.irpatches.mixins.features.transfer_table;

import cam72cam.immersiverailroading.library.TrackItems;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(TrackItems.class)
public abstract class MixinTrackItems {
    @Shadow(remap = false)
    @Final
    @Mutable
    private static TrackItems[] $VALUES;

    private static final TrackItems TRANSFER_TABLE = newEnumInstance("TRANSFER_TABLE");

    @Invoker(value = "<init>", remap = false)
    public static TrackItems op$constructor(String name, int ordinal) {
        throw new AssertionError();
    }

    private static TrackItems newEnumInstance(String name) {
        ArrayList<TrackItems> values = new ArrayList<>(Arrays.asList($VALUES));
        TrackItems value = op$constructor(name, values.get(values.size() - 1).ordinal() + 1);
        values.add(value);
        $VALUES = values.toArray(new TrackItems[0]);
        return value;
    }
}
