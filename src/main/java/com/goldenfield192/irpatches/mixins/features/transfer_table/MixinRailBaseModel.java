package com.goldenfield192.irpatches.mixins.features.transfer_table;

import cam72cam.immersiverailroading.IRItems;
import cam72cam.immersiverailroading.library.TrackItems;
import cam72cam.immersiverailroading.render.block.RailBaseModel;
import cam72cam.immersiverailroading.util.RailInfo;
import cam72cam.mod.MinecraftClient;
import cam72cam.mod.entity.Player;
import cam72cam.mod.item.ItemStack;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(RailBaseModel.class)
public class MixinRailBaseModel {
    @ModifyArg(method = "lambda$getModel$2", at = @At(value = "INVOKE", target = "Lcam72cam/immersiverailroading/render/rail/RailRender;get(Lcam72cam/immersiverailroading/util/RailInfo;)Lcam72cam/immersiverailroading/render/rail/RailRender;"), remap = false)
    private static RailInfo mod(RailInfo info, @Share("info")LocalRef<RailInfo> infoLocalRef){
        if (info.settings.type == TrackItems.valueOf("TRANSFER_TABLE")) {
            ItemStack held = MinecraftClient.getPlayer().getHeldItem(Player.Hand.PRIMARY);
            if (held.is(IRItems.ITEM_TRACK_BLUEPRINT) || held.is(IRItems.ITEM_GOLDEN_SPIKE)) {
                info = info.with(b -> b.itemHeld = true);
            }
        }
        return info;
    }
}
