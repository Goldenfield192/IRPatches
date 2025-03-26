package com.goldenfield192.irpatches.mixins.immersiverailroading.net;

import cam72cam.immersiverailroading.items.nbt.RailSettings;
import cam72cam.immersiverailroading.library.TrackItems;
import cam72cam.immersiverailroading.net.ItemRailUpdatePacket;
import com.goldenfield192.irpatches.accessor.IRailSettingsMutableAccessor;
import com.goldenfield192.irpatches.common.umc.IRPConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRailUpdatePacket.class)
public class MixinItemRailUpdatePacket {
    @Shadow(remap = false)
    private RailSettings settings;

    @Inject(method = "handle", at = @At("HEAD"), remap = false)
    public void inject(CallbackInfo ci) {
        //Server side check for MaxTrackLength(unnecessary?)
        RailSettings.Mutable mutable = settings.mutable();
        if(mutable.length > IRPConfig.MaxTrackLength) {
            mutable.length = IRPConfig.MaxTrackLength;
        }
        if(mutable.type == TrackItems.TURNTABLE){
            ((IRailSettingsMutableAccessor)mutable).setFarEnd(0f);
            ((IRailSettingsMutableAccessor)mutable).setNearEnd(0f);
        }
        settings = mutable.immutable();
    }
}
