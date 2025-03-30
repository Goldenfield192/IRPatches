package com.goldenfield192.irpatches.mixins.immersiverailroading.util;

import cam72cam.immersiverailroading.entity.EntityMoveableRollingStock;
import cam72cam.immersiverailroading.library.Gauge;
import cam72cam.immersiverailroading.library.ItemComponentType;
import cam72cam.immersiverailroading.registry.EntityRollingStockDefinition;
import cam72cam.immersiverailroading.thirdparty.trackapi.ITrack;
import cam72cam.immersiverailroading.tile.TileRailBase;
import cam72cam.immersiverailroading.util.SpawnUtil;
import cam72cam.immersiverailroading.util.VecUtil;
import cam72cam.mod.entity.Player;
import cam72cam.mod.item.ClickResult;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.world.World;
import com.goldenfield192.irpatches.accessor.IStockRollAccessor;
import com.goldenfield192.irpatches.accessor.ITileRailBaseAccessor;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(SpawnUtil.class)
public class MixinSpawnUtil {
    @Inject(method = "placeStock", at = @At(value = "INVOKE_ASSIGN", target = "Lcam72cam/immersiverailroading/thirdparty/trackapi/ITrack;get(Lcam72cam/mod/world/World;Lcam72cam/mod/math/Vec3d;Z)Lcam72cam/immersiverailroading/thirdparty/trackapi/ITrack;", ordinal = 3), remap = false)
    private static void inject(Player player, Player.Hand hand, World worldIn, Vec3i pos, EntityRollingStockDefinition def,
                               List<ItemComponentType> list, CallbackInfoReturnable<ClickResult> cir,
                               @Local(ordinal = 0)EntityMoveableRollingStock moveable,
                               @Local(ordinal = 0)ITrack initte,
                               @Local(ordinal = 1)ITrack centerte,
                               @Local(ordinal = 2)ITrack frontte,
                               @Local(ordinal = 3)ITrack rearte,
                               @Local(ordinal = 1)double offset){
        //Recalculate...bad @Local
        float yaw = player.getYawHead();
        Vec3d center = moveable.getPosition();
        center = initte.getNextPosition(center, VecUtil.fromWrongYaw(-0.1, yaw));
        center = initte.getNextPosition(center, VecUtil.fromWrongYaw(0.1, yaw));
        center = initte.getNextPosition(center, VecUtil.fromWrongYaw(offset, yaw));

        Gauge gauge = moveable.gauge;

        float frontDistance = moveable.getDefinition().getBogeyFront(gauge);
        float rearDistance = moveable.getDefinition().getBogeyRear(gauge);
        Vec3d front = centerte.getNextPosition(center, VecUtil.fromWrongYaw(frontDistance, yaw));
        Vec3d rear = centerte.getNextPosition(center, VecUtil.fromWrongYaw(rearDistance, yaw));

        Vec3d stockDirection = front.subtract(rear).normalize();
        ITileRailBaseAccessor frontAccessor = frontte instanceof TileRailBase ? (ITileRailBaseAccessor) frontte : null;
        ITileRailBaseAccessor rearAccessor = rearte instanceof TileRailBase ? (ITileRailBaseAccessor) rearte : null;
        double distance = 0.1 * gauge.scale();
        
        if (frontAccessor != null) {
            boolean frontDirection = frontAccessor.getDirectionAlong(front, stockDirection);
            float rollFront = frontAccessor.getNextRoll(front, VecUtil.fromWrongYaw(distance, moveable.getFrontYaw()));
            rollFront *= frontDirection ? -1 : 1;
            ((IStockRollAccessor)moveable).setFrontRoll(rollFront);
        }

        if (rearAccessor != null) {
            boolean rearDirection = rearAccessor.getDirectionAlong(rear, stockDirection);
            float rollRear = rearAccessor.getNextRoll(rear, VecUtil.fromWrongYaw(distance, moveable.getRearYaw()));
            rollRear *= rearDirection ? -1 : 1;
            ((IStockRollAccessor)moveable).setRearRoll(rollRear);
        }
    }
}
