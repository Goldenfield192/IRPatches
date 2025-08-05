package com.goldenfield192.irpatches.mixins.features.random_rail;

import cam72cam.immersiverailroading.model.TrackModel;
import cam72cam.immersiverailroading.registry.TrackDefinition;
import cam72cam.immersiverailroading.util.DataBlock;
import com.goldenfield192.irpatches.util.ExtraTrackDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TrackDefinition.class)
public class MixinTrackDefinition {
    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    public void inject0(String trackID, DataBlock object, CallbackInfo ci){
        ExtraTrackDefinition.load(trackID, object);
    }

    @Inject(method = "getTrackForGauge", at = @At("HEAD"), remap = false, cancellable = true)
    public void inject1(double gauge, CallbackInfoReturnable<TrackModel> cir){
        TrackDefinition self = (TrackDefinition) (Object) this;
        ExtraTrackDefinition definition = ExtraTrackDefinition.getExtraDef(self);
        if(definition != null){
            ExtraTrackDefinition.ExtraTrackModel model = definition.getFirst(gauge);
            if(model != null){
                cir.setReturnValue(model.surface);
            }
        }
    }
}
