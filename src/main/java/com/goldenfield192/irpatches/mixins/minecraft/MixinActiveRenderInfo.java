package com.goldenfield192.irpatches.mixins.minecraft;

import com.goldenfield192.irpatches.util.OnboardCamera;
import com.goldenfield192.irpatches.IRPConfig;
import net.minecraft.client.renderer.ActiveRenderInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ActiveRenderInfo.class)
public class MixinActiveRenderInfo {
    @ModifyArg(method = "setup", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ActiveRenderInfo;getMaxZoom(D)D"))
    public double inject1(double p_216779_1_) {
        return (IRPConfig.EnableAdvancedCamera && IRPConfig.OnboardCameraCollideWithBlock) ?
               (float) OnboardCamera.zoom :
               p_216779_1_;
    }
}
