package com.goldenfield192.irpatches.mixins;

import cam72cam.immersiverailroading.ImmersiveRailroading;
import cam72cam.mod.ModEvent;
import cam72cam.mod.event.CommonEvents;
import com.goldenfield192.irpatches.common.StateChangeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Inject into IR loading stage instead of making a new mod
 */
@Mixin(value = ImmersiveRailroading.class)
public class MixinMain {
    @Inject(method = "commonEvent", at = @At("TAIL"), remap = false)
    public void mixinCommonEvent(ModEvent event, CallbackInfo ci){
        if(event ==ModEvent.SETUP){
            CommonEvents.World.TICK.subscribe(StateChangeManager::onTick);
        }
    }
}
