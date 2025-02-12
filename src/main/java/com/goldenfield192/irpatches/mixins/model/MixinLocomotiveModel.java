package com.goldenfield192.irpatches.mixins.model;

import cam72cam.immersiverailroading.library.ModelComponentType;
import cam72cam.immersiverailroading.library.ValveGearConfig;
import cam72cam.immersiverailroading.model.LocomotiveModel;
import cam72cam.immersiverailroading.model.ModelState;
import cam72cam.immersiverailroading.model.components.ComponentProvider;
import cam72cam.immersiverailroading.model.part.DrivingAssembly;
import cam72cam.immersiverailroading.model.part.WheelSet;
import cam72cam.immersiverailroading.registry.LocomotiveDefinition;
import com.goldenfield192.irpatches.common.DrivingAssemblyLoader;
import com.goldenfield192.irpatches.common.ExtraDefinitionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.lang.reflect.InvocationTargetException;

@Mixin(LocomotiveModel.class)
public class MixinLocomotiveModel{
    @Shadow(remap = false)
    protected ModelState frontLocomotive;

    @Shadow(remap = false)
    protected ModelState rearLocomotive;

    @SuppressWarnings("all")
    @Redirect(method = "parseComponents(Lcam72cam/immersiverailroading/model/components/ComponentProvider;Lcam72cam/immersiverailroading/registry/LocomotiveDefinition;)V",
              at = @At(value = "INVOKE", target = "Lcam72cam/immersiverailroading/model/part/DrivingAssembly;get(Lcam72cam/immersiverailroading/library/ValveGearConfig;Lcam72cam/immersiverailroading/model/components/ComponentProvider;Lcam72cam/immersiverailroading/model/ModelState;Lcam72cam/immersiverailroading/library/ModelComponentType$ModelPosition;F[Lcam72cam/immersiverailroading/model/part/WheelSet;)Lcam72cam/immersiverailroading/model/part/DrivingAssembly;", ordinal = 0),
              remap = false)
    public DrivingAssembly mixinDrivingWheelsFrontInit(ValveGearConfig type, ComponentProvider localProvider, ModelState state, ModelComponentType.ModelPosition pos, float angle, WheelSet[] backup, ComponentProvider provider, LocomotiveDefinition def){
        int multiplier = (int) ExtraDefinitionManager.stockDef.get(def.defID).get("leftFirstMultiplier");
        try {
            return DrivingAssemblyLoader.get(type, localProvider, this.frontLocomotive, ModelComponentType.ModelPosition.FRONT, 0, multiplier, new WheelSet[0]);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("all")
    @Redirect(method = "parseComponents(Lcam72cam/immersiverailroading/model/components/ComponentProvider;Lcam72cam/immersiverailroading/registry/LocomotiveDefinition;)V",
            at = @At(value = "INVOKE", target = "Lcam72cam/immersiverailroading/model/part/DrivingAssembly;get(Lcam72cam/immersiverailroading/library/ValveGearConfig;Lcam72cam/immersiverailroading/model/components/ComponentProvider;Lcam72cam/immersiverailroading/model/ModelState;Lcam72cam/immersiverailroading/library/ModelComponentType$ModelPosition;F[Lcam72cam/immersiverailroading/model/part/WheelSet;)Lcam72cam/immersiverailroading/model/part/DrivingAssembly;", ordinal = 1),
            remap = false)
    public DrivingAssembly mixinDrivingWheelsRearInit(ValveGearConfig type, ComponentProvider localProvider, ModelState state, ModelComponentType.ModelPosition pos, float angle, WheelSet[] backup, ComponentProvider provider, LocomotiveDefinition def){
        int multiplier = (int) ExtraDefinitionManager.stockDef.get(def.defID).get("leftFirstMultiplier");
        try {
            return DrivingAssemblyLoader.get(type, localProvider, this.rearLocomotive, ModelComponentType.ModelPosition.REAR, 45.0F, multiplier, new WheelSet[0]);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
