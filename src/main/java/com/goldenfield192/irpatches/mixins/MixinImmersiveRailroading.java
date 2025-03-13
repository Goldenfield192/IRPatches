package com.goldenfield192.irpatches.mixins;

import cam72cam.immersiverailroading.ImmersiveRailroading;
import cam72cam.mod.ModEvent;
import cam72cam.mod.config.ConfigFile;
import cam72cam.mod.event.ClientEvents;
import com.goldenfield192.irpatches.common.IRPConfig;
import com.goldenfield192.irpatches.common.ManualGUIHelper;
import com.goldenfield192.irpatches.document.ManualGui;
import com.goldenfield192.irpatches.document.manual.ManualHoverRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ImmersiveRailroading.class)
public class MixinImmersiveRailroading {
    @Inject(method = "commonEvent", at = @At("TAIL"), remap = false)
    public void mixinCommonEvent(ModEvent event, CallbackInfo ci){
        switch(event){
            case INITIALIZE:
                ConfigFile.sync(IRPConfig.class);
                break;
        }
    }

    @Inject(method = "clientEvent", at = @At("TAIL"), remap = false)
    public void mixinClientEvent(ModEvent event, CallbackInfo ci){
        switch(event){
            case CONSTRUCT:
                ManualGUIHelper.register();
                break;
            case SETUP:
                ClientEvents.TICK.subscribe(ManualGui::onClientTick);
                break;
        }
    }

    @Inject(method = "lambda$clientEvent$4", at = @At(value = "INVOKE_ASSIGN", target = "Lcam72cam/mod/entity/Player;getRiding()Lcam72cam/mod/entity/Entity;"), remap = false, cancellable = true)
    private static void mixinMouseGui(ClientEvents.MouseGuiEvent evt, CallbackInfoReturnable<Boolean> cir){
        ManualHoverRenderer.updateMousePosition(evt);

        if(ManualGui.onClick(evt)){
            cir.setReturnValue(true);
        }
    }
}
