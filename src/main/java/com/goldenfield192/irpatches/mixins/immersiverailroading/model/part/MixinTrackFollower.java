package com.goldenfield192.irpatches.mixins.immersiverailroading.model.part;

import cam72cam.immersiverailroading.entity.EntityMoveableRollingStock;
import cam72cam.immersiverailroading.library.Gauge;
import cam72cam.immersiverailroading.model.part.TrackFollower;
import cam72cam.immersiverailroading.physics.MovementTrack;
import cam72cam.immersiverailroading.thirdparty.trackapi.ITrack;
import cam72cam.immersiverailroading.tile.TileRailBase;
import cam72cam.immersiverailroading.util.VecUtil;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.world.World;
import com.goldenfield192.irpatches.accessor.IStockRollAccessor;
import com.goldenfield192.irpatches.accessor.ITileRailBaseAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import util.Matrix4;

@Mixin(TrackFollower.class)
public class MixinTrackFollower {
    @Shadow(remap = false) @Final private boolean front;
    @Shadow(remap = false) @Final private EntityMoveableRollingStock stock;
    @Shadow(remap = false) @Final private Matrix4 matrix;
    @Shadow(remap = false) private Vec3d pos;
    @Shadow(remap = false) @Final private float offset;
    @Shadow(remap = false) @Final private float min;
    @Unique
    private float roll;

    @Inject(method = "getMatrix", at = @At(value = "INVOKE_ASSIGN", target = "Lutil/Matrix4;rotate(DDDD)Lutil/Matrix4;", ordinal = 0), remap = false)
    public void inject0(CallbackInfoReturnable<Matrix4> cir){
        //TODO Frame wheel and steam
        float offsetRoll = (front ?
                            ((IStockRollAccessor)stock).getFrontRoll() :
                            ((IStockRollAccessor)stock).getRearRoll());
        matrix.rotate(Math.toRadians(offsetRoll),1,0,0);
    }

    @Inject(method = "getMatrix", at = @At(value = "INVOKE_ASSIGN", target = "Lcam72cam/mod/math/Vec3d;subtract(Lcam72cam/mod/math/Vec3d;)Lcam72cam/mod/math/Vec3d;", ordinal = 0), remap = false)
    public void inject1(CallbackInfoReturnable<Matrix4> cir){
        float offsetYaw = (front ?
                           stock.getFrontYaw() :
                           stock.getRearYaw());
        Vec3d offsetPos = pos.add(VecUtil.fromWrongYawPitch(offset, stock.getRotationYaw(), stock.getRotationPitch()));
        double toMinPoint = min - offset;
        roll = nextRoll(stock.getWorld(), stock.gauge, offsetPos, stock.getRotationYaw(), offsetYaw, toMinPoint);
    }

    public float nextRoll(World world, Gauge gauge, Vec3d currentPosition, float rotationYaw, float bogeyYaw, double distance) {
        ITrack rail = MovementTrack.findTrack(world, currentPosition, rotationYaw, gauge.value());
        if (rail == null) {
            return 0;
        }
        Vec3d result = rail.getNextPosition(currentPosition, VecUtil.fromWrongYaw(distance, bogeyYaw));
        float r = rail instanceof TileRailBase ? ((ITileRailBaseAccessor) rail).getNextRoll(currentPosition, VecUtil.fromWrongYaw(distance, bogeyYaw)) : 0;
        if (result == null) {
            return 0;
        }
        return r;
    }
}
