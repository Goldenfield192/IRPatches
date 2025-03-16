package com.goldenfield192.irpatches.mixins.immersiverailroading.tile;

import cam72cam.immersiverailroading.tile.TileRail;
import com.goldenfield192.irpatches.common.umc.IRPConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TileRail.class)
public class MixinTileRail {
    @Inject(method = "getRenderDistance", at = @At("HEAD"), remap = false, cancellable = true)
    public void inject(CallbackInfoReturnable<Double> cir){
        cir.setReturnValue((double) IRPConfig.TrackRenderDistance);
    }
}
