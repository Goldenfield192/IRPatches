package com.goldenfield192.irpatches.mixins.features.rs_turntable;

import cam72cam.immersiverailroading.Config;
import cam72cam.immersiverailroading.items.ItemLargeWrench;
import cam72cam.immersiverailroading.tile.TileRail;
import cam72cam.mod.entity.Player;
import com.goldenfield192.irpatches.IRPConfig;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.lang.reflect.Field;

@Mixin(ItemLargeWrench.class)
public class MixinItemLargeWrench {
    @Redirect(method = "onClickBlock", at = @At(value = "INVOKE", target = "Lcam72cam/immersiverailroading/tile/TileRail;setTablePosition(F)V"), remap = false)
    public void inject0(TileRail instance, float angle, @Local(argsOnly = true) Player player){
        if(!IRPConfig.IRPTurnTable){
            return;
        }

        try {
            Field f = TileRail.class.getDeclaredField("tableIndex");
            f.setAccessible(true);
            int tableIndex = f.getInt(instance);
            if(player.isCrouching()){
                tableIndex--;
                tableIndex += (Config.ConfigBalance.AnglePlacementSegmentation * 4);
            } else {
                tableIndex++;
            }
            tableIndex = tableIndex % (Config.ConfigBalance.AnglePlacementSegmentation * 4);
            f.setInt(instance, tableIndex);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
