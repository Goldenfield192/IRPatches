package com.goldenfield192.irpatches.mixins.immersiverailroading.entity;

import cam72cam.immersiverailroading.entity.FreightTank;
import cam72cam.immersiverailroading.entity.Locomotive;
import cam72cam.immersiverailroading.entity.LocomotiveDiesel;
import cam72cam.immersiverailroading.registry.LocomotiveDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Locomotive.class)
public abstract class MixinLocomotive extends FreightTank {
    @Shadow(remap = false) public abstract LocomotiveDefinition getDefinition();

    @ModifyArgs(method = "handleKeyPress", at = @At(value = "INVOKE", target = "Lcam72cam/immersiverailroading/entity/Locomotive;setThrottle(F)V", ordinal = 0), remap = false)
    public void modArg1(Args args){
        float throttle = args.get(0);
        throttle -= 0.04F;
        throttle += getNotchedThrottle();
        args.set(0, throttle);
    }

    @ModifyArgs(method = "handleKeyPress", at = @At(value = "INVOKE", target = "Lcam72cam/immersiverailroading/entity/Locomotive;setThrottle(F)V", ordinal = 2), remap = false)
    public void modArg2(Args args){
        float throttle = args.get(0);
        throttle += 0.04F;
        throttle -= getNotchedThrottle();
        args.set(0, throttle);
    }

    @Unique
    private float getNotchedThrottle(){
        if(((Locomotive)(Object)this) instanceof LocomotiveDiesel){
            return 1F / ((LocomotiveDiesel)(Object)this).getDefinition().getThrottleNotches();
        }
        return 0.04F;
    }
}
