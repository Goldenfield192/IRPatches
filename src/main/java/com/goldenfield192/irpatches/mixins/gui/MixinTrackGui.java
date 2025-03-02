package com.goldenfield192.irpatches.mixins.gui;

import cam72cam.immersiverailroading.gui.TrackGui;
import cam72cam.immersiverailroading.items.nbt.RailSettings;
import cam72cam.immersiverailroading.library.TrackItems;
import cam72cam.immersiverailroading.track.BuilderTurnTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

@Mixin(TrackGui.class)
public class MixinTrackGui {
    @Shadow(remap = false)
    private RailSettings.Mutable settings;

    @ModifyArg(method = "init",
               at = @At(value = "INVOKE", target = "Lcam72cam/mod/gui/screen/TextField;setValidator(Ljava/util/function/Predicate;)V", ordinal = 0),
               remap = false)
    public Predicate<String> mixinMaxRadius(Predicate<String> filter){
        return s -> {
            if (s == null || s.isEmpty()) {
                return true;
            }
            int val;
            try {
                val = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                return false;
            }
            int max = Integer.MAX_VALUE;
            if (settings.type == TrackItems.TURNTABLE) {
                max = BuilderTurnTable.maxLength(settings.gauge);
            }
            if (val > 0 && val <= max) {
                settings.length = val;
                return true;
            }
            return false;
        };
    }
}
