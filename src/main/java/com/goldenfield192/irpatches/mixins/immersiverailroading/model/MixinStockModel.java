package com.goldenfield192.irpatches.mixins.immersiverailroading.model;

import cam72cam.immersiverailroading.entity.EntityMoveableRollingStock;
import cam72cam.immersiverailroading.library.ModelComponentType;
import cam72cam.immersiverailroading.model.ModelState;
import cam72cam.immersiverailroading.model.StockModel;
import cam72cam.immersiverailroading.model.components.ComponentProvider;
import cam72cam.immersiverailroading.model.components.ModelComponent;
import cam72cam.immersiverailroading.model.part.Bogey;
import cam72cam.immersiverailroading.model.part.DrivingAssembly;
import cam72cam.immersiverailroading.model.part.Frame;
import cam72cam.immersiverailroading.model.part.SwaySimulator;
import cam72cam.immersiverailroading.registry.EntityRollingStockDefinition;
import com.goldenfield192.irpatches.accessor.IStockRollAccessor;
import com.goldenfield192.irpatches.common.umc.DrivingAssemblyLoader;
import com.goldenfield192.irpatches.common.umc.ExtraDefinition;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import util.Matrix4;

import java.lang.reflect.InvocationTargetException;

@Mixin(StockModel.class)
public abstract class MixinStockModel {
    @Shadow(remap = false)
    protected ModelState base;

    @Shadow(remap = false)
    protected Bogey bogeyRear;

    @Shadow(remap = false)
    protected Bogey bogeyFront;

    @Shadow(remap = false)
    protected Frame frame;
    @Shadow(remap = false)
    protected ModelState rocking;
    @Shadow(remap = false)
    protected DrivingAssembly drivingWheels;
    @Shadow(remap = false)
    protected ModelState frontRocking;
    @Shadow(remap = false)
    protected ModelState rearRocking;
    @Shadow(remap = false)
    protected ModelState front;
    @Shadow(remap = false)
    protected ModelState rear;
    @Shadow(remap = false)
    @Final
    private SwaySimulator sway;
    @Shadow(remap = false)
    private ModelComponent shell;

    @Shadow(remap = false)
    protected abstract boolean unifiedBogies();

    @Redirect(method = "postRender", at = @At(value = "INVOKE", target = "Lcam72cam/immersiverailroading/model/part/SwaySimulator;getRollDegrees(Lcam72cam/immersiverailroading/entity/EntityMoveableRollingStock;F)D"), remap = false)
    public double redirect(SwaySimulator instance, EntityMoveableRollingStock stock, float partialTicks) {
        return 0f;
    }

    @Inject(method = "parseComponents", at = @At("HEAD"), remap = false, cancellable = true)
    public void mixinParseComponents(ComponentProvider provider, EntityRollingStockDefinition def, CallbackInfo ci) {
        int multiplier = ExtraDefinition.get(def).leftFirstMultiplier;

        this.frame = new Frame(provider, rocking, rocking, def.defID);

        try {
            drivingWheels = DrivingAssemblyLoader.get(def.getValveGear(), provider, rocking, null, 0, multiplier,
                                                      frame != null ?
                                                      frame.wheels :
                                                      null,
                                                      bogeyFront != null ?
                                                      bogeyFront.wheels :
                                                      null,
                                                      bogeyRear != null ?
                                                      bogeyRear.wheels :
                                                      null);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        this.shell = provider.parse(ModelComponentType.SHELL);
        rocking.include(shell);
        ci.cancel();
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    public void injectConstructor(EntityRollingStockDefinition def, CallbackInfo ci, @Local(ordinal = 0) ComponentProvider provider) {
        this.bogeyFront = Bogey.get(provider, addTrackRoll(front), unifiedBogies(),
                                    ModelComponentType.ModelPosition.FRONT);
        this.bogeyRear = Bogey.get(provider, addTrackRoll(rear), unifiedBogies(),
                                   ModelComponentType.ModelPosition.REAR);
    }

    @Unique
    public ModelState addTrackRoll(ModelState base) {
        return base.push(builder -> builder.add((ModelState.Animator) (stock, v) -> {
            IStockRollAccessor accessor = (IStockRollAccessor) stock;
            return new Matrix4().rotate((accessor.getFrontRoll() + accessor.getRearRoll()) / 2, 1, 0, 0);
        }));
    }
}
