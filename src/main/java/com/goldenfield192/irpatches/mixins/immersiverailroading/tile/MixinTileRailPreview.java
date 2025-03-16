package com.goldenfield192.irpatches.mixins.immersiverailroading.tile;

import cam72cam.immersiverailroading.items.nbt.RailSettings;
import cam72cam.immersiverailroading.library.TrackItems;
import cam72cam.immersiverailroading.tile.TileRailPreview;
import cam72cam.immersiverailroading.util.PlacementInfo;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import com.goldenfield192.irpatches.common.umc.VecUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(TileRailPreview.class)
public abstract class MixinTileRailPreview {
    @Shadow(remap = false) private PlacementInfo placementInfo;

    @Shadow(remap = false) private PlacementInfo customInfo;

    @Shadow(remap = false) private ItemStack item;

    @Shadow(remap = false) public abstract void markDirty();

    @Inject(method = "setCustomInfo",
            at = @At(value = "INVOKE_ASSIGN", target = "Lcam72cam/immersiverailroading/items/nbt/RailSettings;from(Lcam72cam/mod/item/ItemStack;)Lcam72cam/immersiverailroading/items/nbt/RailSettings;"),
            remap = false,
            locals = LocalCapture.CAPTURE_FAILSOFT,
            cancellable = true)
    public void injectCustomInfo(PlacementInfo info, CallbackInfo ci, RailSettings settings){
        float yaw = settings.type == TrackItems.TURN ? placementInfo.yaw / 2 : placementInfo.yaw;
        if(settings.type ==TrackItems.TURN
                || settings.type == TrackItems.STRAIGHT
                || settings.type == TrackItems.SLOPE){
            Vec3d placeOffset = new Vec3d(
                    customInfo.placementPosition.x - placementInfo.placementPosition.x,
                    0,
                    customInfo.placementPosition.z - placementInfo.placementPosition.z
            );
            Vec3d unit = new Vec3d(0, 0, 1).rotateYaw(yaw);
            int shadowLength = (int) Math.round(VecUtils.dotMultiply(placeOffset, unit));
            int length;

            switch (settings.type) {
                case TURN:
                    double sin = Math.sin(Math.toRadians(settings.degrees / 2));
                    length = sin != 0d
                             ? Math.max(0, (int) ((shadowLength / 2d) / sin)) + 1
                             : 1;
                    break;
                case STRAIGHT:
                case SLOPE:
                default:
                    length = Math.max(0, shadowLength) + 1;
                    break;
            }
            settings = settings.with(b -> b.length = length);
        }

        settings.write(item);
        this.markDirty();
        ci.cancel();
    }
}
