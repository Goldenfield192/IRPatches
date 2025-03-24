package com.goldenfield192.irpatches.mixins.immersiverailroading.model;

import cam72cam.immersiverailroading.entity.EntityMoveableRollingStock;
import cam72cam.immersiverailroading.library.ModelComponentType;
import cam72cam.immersiverailroading.model.ModelState;
import cam72cam.immersiverailroading.model.StockModel;
import cam72cam.immersiverailroading.model.components.ComponentProvider;
import cam72cam.immersiverailroading.model.components.ModelComponent;
import cam72cam.immersiverailroading.model.part.*;
import cam72cam.immersiverailroading.registry.EntityRollingStockDefinition;
import com.goldenfield192.irpatches.common.umc.DrivingAssemblyLoader;
import com.goldenfield192.irpatches.common.umc.ExtraDefinition;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.InvocationTargetException;

@Mixin(StockModel.class)
public abstract class MixinStockModel{
    @Shadow(remap = false)
    protected ModelState base;

    @Shadow(remap = false)
    protected Bogey bogeyRear;

    @Shadow(remap = false)
    protected Bogey bogeyFront;

    @Shadow(remap = false)
    protected Frame frame;

    @Shadow(remap = false) @Final private SwaySimulator sway;

    @Shadow(remap = false) protected ModelState rocking;

    @Shadow(remap = false) protected DrivingAssembly drivingWheels;

    @Shadow(remap = false) private ModelComponent shell;

    @Shadow(remap = false) protected ModelState frontRocking;

    @Shadow(remap = false) protected ModelState rearRocking;

    @Shadow(remap = false) protected abstract boolean unifiedBogies();

    @Redirect(method = "postRender", at = @At(value = "INVOKE", target = "Lcam72cam/immersiverailroading/model/part/SwaySimulator;getRollDegrees(Lcam72cam/immersiverailroading/entity/EntityMoveableRollingStock;F)D"), remap = false)
    public double redirect(SwaySimulator instance, EntityMoveableRollingStock stock, float partialTicks){
        return 0f;
    }

    @Inject(method = "parseComponents", at = @At("HEAD"), remap = false, cancellable = true)
    public void mixinParseComponents(ComponentProvider provider, EntityRollingStockDefinition def, CallbackInfo ci){
        int multiplier = ExtraDefinition.get(def).leftFirstMultiplier;

        this.frame = new Frame(provider, rocking, rocking, def.defID);

        try {
            drivingWheels = DrivingAssemblyLoader.get(def.getValveGear(), provider, rocking, null, 0, multiplier,
                                                frame != null ? frame.wheels : null,
                                                bogeyFront != null ? bogeyFront.wheels : null,
                                                bogeyRear != null ? bogeyRear.wheels : null);
        } catch(InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        this.shell = provider.parse(ModelComponentType.SHELL);
        rocking.include(shell);
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    public void injectConstructor(EntityRollingStockDefinition def, CallbackInfo ci, @Local(ordinal = 0) ComponentProvider provider){
        this.bogeyFront = Bogey.get(provider, frontRocking, unifiedBogies(), ModelComponentType.ModelPosition.FRONT);
        this.bogeyRear = Bogey.get(provider, rearRocking, unifiedBogies(), ModelComponentType.ModelPosition.REAR);
    }
}
