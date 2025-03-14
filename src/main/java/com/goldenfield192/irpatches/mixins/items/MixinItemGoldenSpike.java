package com.goldenfield192.irpatches.mixins.items;

import cam72cam.immersiverailroading.tile.TileRailPreview;
import cam72cam.immersiverailroading.util.PlacementInfo;
import cam72cam.mod.entity.Player;
import cam72cam.mod.item.ClickResult;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.util.Facing;
import cam72cam.mod.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(cam72cam.immersiverailroading.items.ItemGoldenSpike.class)
public class MixinItemGoldenSpike {
    @Inject(method = "onClickBlock",
            at = @At(value = "INVOKE", target = "Lcam72cam/immersiverailroading/tile/TileRailPreview;setCustomInfo(Lcam72cam/immersiverailroading/util/PlacementInfo;)V"),
            remap = false,
            locals = LocalCapture.CAPTURE_FAILSOFT)
    public void ClickBlock(Player player, World world, Vec3i pos, Player.Hand hand, Facing facing, Vec3d hit, CallbackInfoReturnable<ClickResult> cir, ItemStack held, Vec3i tepos, TileRailPreview tr) {
        //If x or z < 0 there are some weird things with smooth...
        Vec3d finalPos = hit.subtract(0, hit.y, 0).add(pos).subtract(tepos);
        if(finalPos.x < 0){
            finalPos = finalPos.add(1,0,0);
        }
        if(finalPos.z < 0){
            finalPos = finalPos.add(0,0,1);
        }
        tr.setCustomInfo(new PlacementInfo(tr.getItem(), player.getYawHead(), finalPos));
        cir.setReturnValue(ClickResult.ACCEPTED);
    }
}
