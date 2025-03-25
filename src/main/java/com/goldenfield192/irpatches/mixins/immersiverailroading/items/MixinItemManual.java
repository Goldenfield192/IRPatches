package com.goldenfield192.irpatches.mixins.immersiverailroading.items;

import cam72cam.immersiverailroading.items.ItemManual;
import cam72cam.mod.entity.Player;
import cam72cam.mod.world.World;
import com.goldenfield192.irpatches.common.IRPGUIHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemManual.class)
public class MixinItemManual {
    @Inject(method = "onClickAir", at = @At(value = "INVOKE", target = "Lcam72cam/mod/entity/Player;sendMessage(Lcam72cam/mod/text/PlayerMessage;)V", ordinal = 1), remap = false, cancellable = true)
    public void mixinGui(Player player, World world, Player.Hand hand, CallbackInfo ci) {
        IRPGUIHelper.MANUAL.open(player);
        ci.cancel();
    }
}
