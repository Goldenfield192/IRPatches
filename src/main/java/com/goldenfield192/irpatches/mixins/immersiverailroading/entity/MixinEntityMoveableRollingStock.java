package com.goldenfield192.irpatches.mixins.immersiverailroading.entity;

import cam72cam.immersiverailroading.entity.EntityMoveableRollingStock;
import cam72cam.immersiverailroading.entity.EntityRidableRollingStock;
import cam72cam.immersiverailroading.physics.TickPos;
import cam72cam.mod.serialization.TagCompound;
import com.goldenfield192.irpatches.accessor.IStockRollAccessor;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityMoveableRollingStock.class)
public class MixinEntityMoveableRollingStock
       extends EntityRidableRollingStock
       implements IStockRollAccessor{
    @Unique
    private Float frontRoll;

    @Unique
    private Float rearRoll;

    @Inject(method = "onTick", at = @At(value = "INVOKE", target = "Lcam72cam/immersiverailroading/entity/EntityMoveableRollingStock;setPosition(Lcam72cam/mod/math/Vec3d;)V"), remap = false)
    public void inject0(CallbackInfo ci, @Local TickPos currentPos){
        this.frontRoll = ((IStockRollAccessor)currentPos).getFrontRoll();
        this.rearRoll = ((IStockRollAccessor)currentPos).getRearRoll();
    }

    @Inject(method = "load", at = @At("TAIL"), remap = false)
    public void mixinLoad(TagCompound data, CallbackInfo ci){
        TagCompound irp = data.get("irp");
        if(irp != null){
            this.frontRoll = irp.getFloat("frontRoll");
            this.rearRoll = irp.getFloat("rearRoll");
        }
    }

    @Override
    public void save(TagCompound data) {
        super.save(data);
        TagCompound irp = new TagCompound();
        irp.setFloat("frontRoll", this.frontRoll);
        irp.setFloat("rearRoll", this.rearRoll);
        data.set("irp", irp);
    }

    @Override
    public float getFrontRoll() {
        return this.frontRoll;
    }

    @Override
    public void setFrontRoll(float val) {
        this.frontRoll = val;
    }

    @Override
    public float getRearRoll() {
        return this.rearRoll;
    }

    @Override
    public void setRearRoll(float val) {
        this.rearRoll = val;
    }
}
