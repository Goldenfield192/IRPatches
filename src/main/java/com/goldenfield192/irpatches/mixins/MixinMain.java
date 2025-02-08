package com.goldenfield192.irpatches.mixins;

import cam72cam.immersiverailroading.ImmersiveRailroading;
import cam72cam.mod.ModEvent;
import com.goldenfield192.irpatches.text.TranslationManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(value = ImmersiveRailroading.class, remap = false)
public class MixinMain {
    @Inject(method = "commonEvent", at = @At("TAIL"), remap = false)
    public void mixinInit(ModEvent event, CallbackInfo ci){
        try {
            if(event == ModEvent.INITIALIZE){
                TranslationManager.invoke();
            }
        }catch (IOException ignored){}
    }
}
