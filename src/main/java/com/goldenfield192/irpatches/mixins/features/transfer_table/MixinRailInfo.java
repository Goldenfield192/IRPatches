package com.goldenfield192.irpatches.mixins.features.transfer_table;

import cam72cam.immersiverailroading.items.nbt.RailSettings;
import cam72cam.immersiverailroading.library.TrackItems;
import cam72cam.immersiverailroading.track.BuilderBase;
import cam72cam.immersiverailroading.util.RailInfo;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.world.World;
import com.goldenfield192.irpatches.util.BuilderTransferTable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RailInfo.class)
public class MixinRailInfo {
    @Shadow(remap = false) @Final public RailSettings settings;

    @Inject(method = "constructBuilder", at = @At("HEAD"), remap = false, cancellable = true)
    public void inject(World world, Vec3i pos, CallbackInfoReturnable<BuilderBase> cir){
        if(settings.type == TrackItems.valueOf("TRANSFER_TABLE")){
            cir.setReturnValue(new BuilderTransferTable((RailInfo) (Object) this, world, pos));
            cir.cancel();
        }
    }
}
