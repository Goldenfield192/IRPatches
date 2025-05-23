package com.goldenfield192.irpatches.mixins.minecraft;

import com.goldenfield192.irpatches.util.OnboardCamera;
import com.goldenfield192.irpatches.IRPConfig;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Surrogate;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {
    @ModifyConstant(method = "updateRenderer", constant = @Constant(floatValue = 4.0F))
    public float inject1(float constant) {
        return (IRPConfig.EnableAdvancedCamera && IRPConfig.OnboardCameraCollideWithBlock) ?
               (float) OnboardCamera.zoom :
               4f;
    }

    @ModifyConstant(method = "orientCamera", constant = @Constant(floatValue = 4.0F))
    public float inject2(float constant) {
        return (IRPConfig.EnableAdvancedCamera && IRPConfig.OnboardCameraCollideWithBlock) ?
               (float) OnboardCamera.zoom :
               4f;
    }
}
