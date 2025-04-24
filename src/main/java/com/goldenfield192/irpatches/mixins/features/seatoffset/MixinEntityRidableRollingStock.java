package com.goldenfield192.irpatches.mixins.features.seatoffset;

import cam72cam.immersiverailroading.entity.EntityBuildableRollingStock;
import cam72cam.immersiverailroading.entity.EntityRidableRollingStock;
import cam72cam.mod.entity.custom.IRidable;
import cam72cam.mod.math.Vec3d;
import com.goldenfield192.irpatches.accessor.IStockRollAccessor;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import util.Matrix4;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Mixin(EntityRidableRollingStock.class)
public abstract class MixinEntityRidableRollingStock
        extends EntityBuildableRollingStock
        implements IRidable {
    @Shadow(remap = false) private Map<String, UUID> seatedPassengers;

    //Fix seat offset in pitch and roll
    @Inject(method = "getSeatPosition", at = @At("HEAD"), remap = false)
    public void injectSeat(UUID passenger, CallbackInfoReturnable<Vec3d> cir, @Share("mat")LocalRef<Matrix4> matrix4LocalRef){
        Matrix4 matrix4 = new Matrix4();
        IStockRollAccessor accessor = (IStockRollAccessor) this;
        matrix4.rotate(Math.toRadians(accessor.getFrontRoll() + accessor.getRearRoll()/ 2), 0, 0, -1);
        matrix4LocalRef.set(matrix4);
    }

    @Redirect(method = "getSeatPosition",
            at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;findFirst()Ljava/util/Optional;", ordinal = 1),
            remap = false)
    public Optional<Vec3d> redirect(Stream<Vec3d> instance, @Share("mat")LocalRef<Matrix4> matrix4LocalRef){
        Optional<Vec3d> vec3d = instance.findFirst();
        return vec3d.map(d -> matrix4LocalRef.get().apply(d));
    }
}
