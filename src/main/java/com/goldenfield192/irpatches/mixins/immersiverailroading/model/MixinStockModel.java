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
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import util.Matrix4;

import java.lang.reflect.InvocationTargetException;

@Mixin(StockModel.class)
public class MixinStockModel {
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

    @Redirect(method = "postRender", at = @At(value = "INVOKE", target = "Lcam72cam/immersiverailroading/model/part/SwaySimulator;getRollDegrees(Lcam72cam/immersiverailroading/entity/EntityMoveableRollingStock;F)D"), remap = false)
    public double redirect(SwaySimulator instance, EntityMoveableRollingStock stock, float partialTicks){
        return 0f;
    }

    @Inject(method = "parseComponents", at = @At("HEAD"), remap = false, cancellable = true)
    public void mixinParseComponents(ComponentProvider provider, EntityRollingStockDefinition def, CallbackInfo ci){
        //Maybe useless?
        ModelState state1 = this.base.push(builder -> builder.add((ModelState.Animator) (stock, partialTicks) ->
                new Matrix4().rotate(Math.toRadians(sway.getRollDegrees(stock, partialTicks)), 1, 0, 0)));
        int multiplier = ExtraDefinition.get(def).leftFirstMultiplier;

        this.frame = new Frame(provider, state1, rocking, def.defID);

        try {
            drivingWheels = DrivingAssemblyLoader.get(def.getValveGear(), provider, state1, null, 0, multiplier,
                                                frame != null ? frame.wheels : null,
                                                bogeyFront != null ? bogeyFront.wheels : null,
                                                bogeyRear != null ? bogeyRear.wheels : null);
        } catch(InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        this.shell = provider.parse(ModelComponentType.SHELL);
        rocking.include(shell);
    }
}
