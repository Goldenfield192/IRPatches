package com.goldenfield192.irpatches.mixins.features.transfer_table;

import cam72cam.immersiverailroading.items.ItemLargeWrench;
import cam72cam.immersiverailroading.library.TrackItems;
import cam72cam.immersiverailroading.tile.TileRail;
import cam72cam.immersiverailroading.util.VecUtil;
import cam72cam.mod.entity.Player;
import cam72cam.mod.item.ClickResult;
import cam72cam.mod.math.Rotation;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.util.Facing;
import cam72cam.mod.world.World;
import com.goldenfield192.irpatches.accessor.IRailSettingsAccessor;
import com.goldenfield192.irpatches.accessor.ITileRailAccessor;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemLargeWrench.class)
public class MixinItemLargeWrench {
    @Inject(method = "onClickBlock", at = @At(value = "INVOKE", target = "Lcam72cam/immersiverailroading/tile/TileRailBase;getReplacedTile()Lcam72cam/immersiverailroading/tile/TileRailBase;"), remap = false, cancellable = true)
    public void inject(Player player, World world, Vec3i pos, Player.Hand hand, Facing facing, Vec3d hit, CallbackInfoReturnable<ClickResult> cir,
                       @Local TileRail parent){
        if (parent != null && parent.info.settings.type == TrackItems.valueOf("TRANSFER_TABLE")) {

            IRailSettingsAccessor accessor = (IRailSettingsAccessor) parent.info.settings;
            int halfGauge = (int) Math.floor((parent.info.settings.gauge.value() * 1.1 + 0.5) / 2);
            int width = accessor.getTransferTableEntryDistance() * (accessor.getTransferTableEntryNum() - 1) + halfGauge + 2;
            Vec3i mainOffset = new Vec3i(-width / 2, 1, parent.info.settings.length/2);
            mainOffset = mainOffset.rotate(Rotation.from(parent.info.placementInfo.facing()));

            ((ITileRailAccessor)parent).setTransferTablePos(pos.subtract(parent.getPos().subtract(mainOffset)).rotate(Rotation.from(parent.info.placementInfo.facing().getOpposite())));
            cir.setReturnValue(ClickResult.PASS);
            cir.cancel();
        }
    }
}
