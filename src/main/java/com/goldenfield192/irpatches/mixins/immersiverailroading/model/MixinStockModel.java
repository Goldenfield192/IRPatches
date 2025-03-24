package com.goldenfield192.irpatches.mixins.immersiverailroading.model;

import cam72cam.immersiverailroading.entity.EntityMoveableRollingStock;
import cam72cam.immersiverailroading.library.ValveGearConfig;
import cam72cam.immersiverailroading.model.ModelState;
import cam72cam.immersiverailroading.model.StockModel;
import cam72cam.immersiverailroading.model.components.ComponentProvider;
import cam72cam.immersiverailroading.model.part.*;
import cam72cam.immersiverailroading.registry.EntityRollingStockDefinition;
import com.goldenfield192.irpatches.common.umc.DrivingAssemblyLoader;
import com.goldenfield192.irpatches.common.umc.ExtraDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

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

    @Redirect(method = "postRender", at = @At(value = "INVOKE", target = "Lcam72cam/immersiverailroading/model/part/SwaySimulator;getRollDegrees(Lcam72cam/immersiverailroading/entity/EntityMoveableRollingStock;F)D"), remap = false)
    public double redirect(SwaySimulator instance, EntityMoveableRollingStock stock, float partialTicks){
        return 0f;
    }

    @SuppressWarnings("all")
    @Redirect(method = "parseComponents",
              at = @At(value = "INVOKE", target = "Lcam72cam/immersiverailroading/model/part/DrivingAssembly;get(Lcam72cam/immersiverailroading/library/ValveGearConfig;Lcam72cam/immersiverailroading/model/components/ComponentProvider;Lcam72cam/immersiverailroading/model/ModelState;F[Lcam72cam/immersiverailroading/model/part/WheelSet;)Lcam72cam/immersiverailroading/model/part/DrivingAssembly;"),
              remap = false)
    public DrivingAssembly mixinParseComponents(ValveGearConfig type, ComponentProvider localProvider, ModelState state, float angleOffset, WheelSet[] backups, ComponentProvider provider, EntityRollingStockDefinition def){
        int multiplier = (int) ExtraDefinition.get(def).leftFirstMultiplier;
        try {
            return DrivingAssemblyLoader.get(def.getValveGear(), localProvider, this.base, null, 0, multiplier, new WheelSet[]{this.frame != null ? this.frame.wheels : null, this.bogeyFront != null ? this.bogeyFront.wheels : null, this.bogeyRear != null ? this.bogeyRear.wheels : null});
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
