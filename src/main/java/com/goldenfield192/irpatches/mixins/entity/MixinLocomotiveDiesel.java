package com.goldenfield192.irpatches.mixins.entity;

import cam72cam.immersiverailroading.Config;
import cam72cam.immersiverailroading.entity.LocomotiveDiesel;
import cam72cam.immersiverailroading.registry.LocomotiveDieselDefinition;
import cam72cam.mod.fluid.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import com.goldenfield192.irpatches.common.*;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mixin(LocomotiveDiesel.class)
public abstract class MixinLocomotiveDiesel {
    @Shadow(remap = false) public abstract LocomotiveDieselDefinition getDefinition();

    @Inject(method = "getFluidFilter", at = @At("HEAD"), remap = false, cancellable = true)
    public void getFluidFilter(CallbackInfoReturnable<List<Fluid>> cir){
        Config.ConfigBalance.dieselFuels.put("lava",1000);
        List<String> str = (List<String>) ExtraDefinitionManager.stockDef.get(this.getDefinition().defID).get("fuel");
        cir.setReturnValue(Config.ConfigBalance.dieselFuels.keySet().stream()
                                                           .filter(string -> {
                                                               if(str != null){
                                                                   return str.contains(string);
                                                               }
                                                               return true;
                                                           })
                                                           .map(Fluid::getFluid)
                                                           .filter(Objects::nonNull)
                                                           .collect(Collectors.toList()));
    }
}
