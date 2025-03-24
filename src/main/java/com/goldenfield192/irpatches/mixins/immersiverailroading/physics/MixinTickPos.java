package com.goldenfield192.irpatches.mixins.immersiverailroading.physics;

import cam72cam.immersiverailroading.entity.physics.SimulationState;
import cam72cam.immersiverailroading.physics.TickPos;
import cam72cam.immersiverailroading.util.Speed;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.serialization.TagCompound;
import com.goldenfield192.irpatches.accessor.IStockRollAccessor;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TickPos.class)
public abstract class MixinTickPos implements IStockRollAccessor {
    @Shadow(remap = false)
    public int tickID;
    @Shadow(remap = false)
    public Speed speed;
    @Shadow(remap = false)
    public Vec3d position;
    @Shadow(remap = false)
    public float frontYaw;
    @Shadow(remap = false)
    public float rearYaw;
    @Shadow(remap = false)
    public float rotationYaw;
    @Shadow(remap = false)
    public float rotationPitch;
    @Shadow(remap = false)
    public boolean isOffTrack;

    @Shadow(remap = false)
    private static double skewScalar(double curr, double next, float ratio) {
        return 0;
    }

    @Shadow(remap = false)
    private static float skewAngle(float curr, float next, float ratio) {
        return 0;
    }

    public float frontRoll;
    public float rearRoll;

    @Inject(method = "<init>(Lcam72cam/immersiverailroading/entity/physics/SimulationState;)V", at = @At("TAIL"), remap = false)
    public void injectConstructor0(SimulationState state, CallbackInfo ci){
        this.frontRoll = ((IStockRollAccessor)state).getFrontRoll();
        this.rearRoll = ((IStockRollAccessor)state).getRearRoll();
    }

    @Inject(method = "toTag", at = @At("RETURN"), remap = false)
    public void save(CallbackInfoReturnable<TagCompound> cir, @Local TagCompound data){
        TagCompound irp = new TagCompound();
        irp.setFloat("frontRoll", this.frontRoll);
        irp.setFloat("rearRoll", this.rearRoll);
        data.set("irp", irp);
    }

    @Inject(method = "<init>(Lcam72cam/mod/serialization/TagCompound;)V", at = @At("TAIL"), remap = false)
    public void load(TagCompound data, CallbackInfo ci){
        TagCompound irp = data.get("irp");
        if(irp != null){
            this.frontRoll = irp.getFloat("frontRoll");
            this.rearRoll = irp.getFloat("rearRoll");
        }
    }

    @Inject(method = "clone()Lcam72cam/immersiverailroading/physics/TickPos;", at = @At("HEAD"), remap = false, cancellable = true)
    public void inject0(CallbackInfoReturnable<TickPos> cir){
        IStockRollAccessor tickPos = (IStockRollAccessor) new TickPos(this.tickID, this.speed, this.position, this.frontYaw, this.rearYaw, this.rotationYaw, this.rotationPitch, this.isOffTrack);
        tickPos.setFrontRoll(this.frontRoll);
        tickPos.setRearRoll(this.rearRoll);
        cir.setReturnValue((TickPos) tickPos);
    }

    @Inject(method = "skew", at = @At("RETURN"), remap = false, cancellable = true)
    private static void inject1(TickPos current, TickPos next, double tick, CallbackInfoReturnable<TickPos> cir, @Local float ratio) {
        IStockRollAccessor accessor = (IStockRollAccessor) new TickPos(
                current.tickID,
                current.speed,
                new Vec3d(
                        skewScalar(current.position.x, next.position.x, ratio),
                        skewScalar(current.position.y, next.position.y, ratio),
                        skewScalar(current.position.z, next.position.z, ratio)
                ),
                skewAngle(current.frontYaw, next.frontYaw, ratio),
                skewAngle(current.rearYaw, next.rearYaw, ratio),
                skewAngle(current.rotationYaw, next.rotationYaw, ratio),
                skewAngle(current.rotationPitch, next.rotationPitch, ratio),
                current.isOffTrack
        );
        accessor.setFrontRoll(skewAngle(((IStockRollAccessor) current).getFrontRoll(), ((IStockRollAccessor) next).getFrontRoll(), ratio));
        accessor.setRearRoll(skewAngle(((IStockRollAccessor) current).getRearRoll(), ((IStockRollAccessor) next).getRearRoll(), ratio));
        cir.setReturnValue((TickPos) accessor);
    }

    @Override
    public float getFrontRoll() {
        return frontRoll;
    }

    @Override
    public void setFrontRoll(float val) {
        this.frontRoll = val;
    }

    @Override
    public float getRearRoll() {
        return rearRoll;
    }

    @Override
    public void setRearRoll(float val) {
        this.rearRoll = val;
    }
}
