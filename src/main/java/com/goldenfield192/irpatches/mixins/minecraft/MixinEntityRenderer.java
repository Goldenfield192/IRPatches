package com.goldenfield192.irpatches.mixins.minecraft;

import com.goldenfield192.irpatches.common.OnboardCamera;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {
    @ModifyConstant(method = "updateRenderer", constant = @Constant(floatValue = 4.0F))
    public float inject1(float constant){
        return (float) OnboardCamera.distance;
    }

    @ModifyConstant(method = "orientCamera", constant = @Constant(floatValue = 4.0F))
    public float inject2(float constant){
        return (float) OnboardCamera.distance;
    }
}
