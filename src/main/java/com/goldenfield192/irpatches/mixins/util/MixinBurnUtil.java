package com.goldenfield192.irpatches.mixins.util;

import cam72cam.immersiverailroading.Config;
import cam72cam.immersiverailroading.util.BurnUtil;
import cam72cam.mod.fluid.Fluid;
import com.goldenfield192.irpatches.common.ExtraDefinitionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BurnUtil.class)
public class MixinBurnUtil {
    @Inject(method = "getBurnTime(Lcam72cam/mod/fluid/Fluid;)I", at = @At("HEAD"), remap = false, cancellable = true)
    private static void injectGetBurnTime(Fluid fluid, CallbackInfoReturnable<Integer> cir) {
        if(Config.ConfigBalance.dieselFuels.containsKey(fluid.ident)){
            cir.setReturnValue(Config.ConfigBalance.dieselFuels.get(fluid.ident));
            return;
        } else if(ExtraDefinitionManager.burnTime.containsKey(fluid.ident)){
            cir.setReturnValue(ExtraDefinitionManager.burnTime.get(fluid.ident));
            return;
        }
        cir.setReturnValue(0);
    }
}
