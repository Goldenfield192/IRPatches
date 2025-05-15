package com.goldenfield192.irpatches.mixins.immersiverailroading.items;

import cam72cam.immersiverailroading.items.ItemLargeWrench;
import cam72cam.immersiverailroading.tile.TileRailBase;
import cam72cam.mod.entity.Player;
import cam72cam.mod.item.ClickResult;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.util.Facing;
import cam72cam.mod.world.World;
import com.goldenfield192.irpatches.accessor.ITileRailBaseAccessor;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemLargeWrench.class)
public class MixinItemLargeWrench {
    @Inject(method = "onClickBlock", at = @At(value = "INVOKE", target = "Lcam72cam/immersiverailroading/tile/TileRailBase;setAugment(Lcam72cam/immersiverailroading/library/Augment;)V"), remap = false)
    public void inject(Player player, World world, Vec3i pos, Player.Hand hand, Facing facing, Vec3d hit, CallbackInfoReturnable<ClickResult> cir, @Local TileRailBase te){
        ((ITileRailBaseAccessor) te).setCGFilter(null);
    }
}
