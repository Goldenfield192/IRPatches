package com.goldenfield192.irpatches.mixins.immersiverailroading.util;

import cam72cam.immersiverailroading.items.nbt.RailSettings;
import cam72cam.immersiverailroading.library.TrackDirection;
import cam72cam.immersiverailroading.util.PlacementInfo;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.util.Facing;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlacementInfo.class)
public abstract class MixinPlacementInfo {
    @Shadow(remap = false)
    public abstract Facing facing();

    @Shadow(remap = false)
    @Final
    @Mutable
    public Vec3d placementPosition;

    @Inject(method = "<init>(Lcam72cam/mod/item/ItemStack;FLcam72cam/mod/math/Vec3d;)V",
            at = @At("RETURN"),
            remap = false,
            locals = LocalCapture.CAPTURE_FAILSOFT)
    public void injectVecFix(ItemStack stack, float yawHead, Vec3d hit, CallbackInfo ci, RailSettings settings, TrackDirection direction, int quarter, double hitX, double hitZ) {
        hitX = hit.x % 1.0;
        hitZ = hit.z % 1.0;
        switch(settings.posType) {
            case FIXED:
                hitX = 0.5f;
                hitZ = 0.5f;
                break;
            case PIXELS:
                hitX = ((int) (hitX * 16)) / 16f;
                hitZ = ((int) (hitZ * 16)) / 16f;
                if(hit.z < 0){
                    hitZ += 1;
                }
                if(hit.x < 0){
                    hitX += 1;
                }
                break;
            case PIXELS_LOCKED:
                hitX = ((int) (hitX * 16)) / 16f;
                hitZ = ((int) (hitZ * 16)) / 16f;

                if(quarter != 0){
                    if(hit.z < 0){
                        hitZ += 1;
                    }
                    if(hit.x < 0){
                        hitX += 1;
                    }
                    break;
                }

                switch(facing()) {
                    case EAST:
                    case WEST:
                        hitZ = 0.5f;
                        if(hit.x < 0){
                            hitX += 1;
                        }
                        break;
                    case NORTH:
                    case SOUTH:
                        hitX = 0.5f;
                        if(hit.z < 0){
                            hitZ += 1;
                        }
                        break;
                    default:
                        break;
                }
                break;
            case SMOOTH:
                if(hit.z < 0){
                    hitZ += 1;
                }
                if(hit.x < 0){
                    hitX += 1;
                }
                break;
            case SMOOTH_LOCKED:
                if(quarter != 0){
                    if(hit.z < 0){
                        hitZ += 1;
                    }
                    if(hit.x < 0){
                        hitX += 1;
                    }
                    break;
                }

                switch(facing()) {
                    case EAST:
                    case WEST:
                        hitZ = 0.5f;
                        if(hit.x < 0){
                            hitX += 1;
                        }
                        break;
                    case NORTH:
                    case SOUTH:
                        hitX = 0.5f;
                        if(hit.z < 0){
                            hitZ += 1;
                        }
                        break;
                    default:
                }
                break;
        }
        this.placementPosition = new Vec3d(new Vec3i(hit)).add(hitX, 0, hitZ);
    }
}
