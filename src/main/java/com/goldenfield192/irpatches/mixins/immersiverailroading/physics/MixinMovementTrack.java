package com.goldenfield192.irpatches.mixins.immersiverailroading.physics;

import cam72cam.immersiverailroading.physics.MovementTrack;
import cam72cam.immersiverailroading.tile.TileRail;
import cam72cam.immersiverailroading.track.PosStep;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.world.World;
import com.goldenfield192.irpatches.accessor.IVec3dAccessor;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MovementTrack.class)
public class MixinMovementTrack {
//    @Inject(method = "nextPositionDirect", at = @At(value = "RETURN", ordinal = 4), remap = false)
//    private static void inject4(World world, Vec3d currentPosition, TileRail rail, Vec3d delta, CallbackInfoReturnable<Vec3d> cir,
//                               @Local(ordinal = 1) Vec3d result, @Local(ordinal = 2) Vec3d resultOppo, @Local(ordinal = 0) PosStep pos){
//        ((IVec3dAccessor)result).IRPatch$setRoll(((IVec3dAccessor)pos).IRPatch$getRoll());
//        ((IVec3dAccessor)resultOppo).IRPatch$setRoll(((IVec3dAccessor)pos).IRPatch$getRoll());
//    }
}
