package com.goldenfield192.irpatches.mixins.immersiverailroading.entity;

import cam72cam.immersiverailroading.entity.LocomotiveDiesel;
import cam72cam.immersiverailroading.registry.LocomotiveDieselDefinition;
import cam72cam.immersiverailroading.util.BurnUtil;
import cam72cam.mod.fluid.Fluid;
import com.goldenfield192.irpatches.common.umc.ExtraDefinition;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mixin(LocomotiveDiesel.class)
public abstract class MixinLocomotiveDiesel {
    @Shadow(remap = false)
    public abstract LocomotiveDieselDefinition getDefinition();

    @Inject(method = "getFluidFilter", at = @At("HEAD"), remap = false, cancellable = true)
    public void getFluidFilter(CallbackInfoReturnable<List<Fluid>> cir) {
        List<Pair<String, Integer>> overrides = ExtraDefinition.get(this.getDefinition()).burnables;
        if (overrides == null) {
            cir.setReturnValue(BurnUtil.burnableFluids());
            return;
        }
        cir.setReturnValue(overrides.stream()
                                    .map(pair -> Fluid.getFluid(pair.getKey()))
                                    .filter(Objects::nonNull)
                                    .collect(Collectors.toList()));
    }

    @Redirect(method = "onTick", at = @At(value = "INVOKE", target = "Lcam72cam/immersiverailroading/util/BurnUtil;getBurnTime(Lcam72cam/mod/fluid/Fluid;)I"), remap = false)
    public int mixinGetBurnTime(Fluid fluid) {
        List<Pair<String, Integer>> overrides = ExtraDefinition.get(this.getDefinition()).burnables;
        if (overrides != null && overrides.stream().anyMatch(pair -> pair.getLeft().equals(fluid.ident))) {
            return overrides.stream().filter(pair -> pair.getLeft().equals(fluid.ident)).findFirst().get().getRight();
        }
        return 0;
    }
}
