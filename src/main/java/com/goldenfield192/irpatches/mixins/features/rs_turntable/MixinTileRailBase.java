package com.goldenfield192.irpatches.mixins.features.rs_turntable;

import cam72cam.immersiverailroading.Config;
import cam72cam.immersiverailroading.library.TrackItems;
import cam72cam.immersiverailroading.tile.TileRail;
import cam72cam.immersiverailroading.tile.TileRailBase;
import com.goldenfield192.irpatches.IRPConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

@Mixin(TileRailBase.class)
public abstract class MixinTileRailBase {
    @Unique
    private boolean hasRedstone;

    @Inject(method = "update", at = @At(value = "HEAD") ,remap = false)
    public void inject(CallbackInfo ci){
        if(IRPConfig.LegacyTurnTable){
            return;
        }

        TileRailBase self = (TileRailBase) (Object) this;
        if(self.getWorld().isServer && self instanceof TileRail && self.getParentTile().info.settings.type == TrackItems.TURNTABLE){
            if(self.getWorld().getRedstone(self.getPos()) != 0){
                this.hasRedstone = true;
            }

            if(self.getWorld().getRedstone(self.getPos()) == 0 && this.hasRedstone){
                try {
                    TileRail tile = self.getParentTile();
                    Field f = TileRail.class.getDeclaredField("tableIndex");
                    f.setAccessible(true);
                    int tableIndex = f.getInt(tile);
                    tableIndex++;
                    tableIndex = tableIndex % (Config.ConfigBalance.AnglePlacementSegmentation * 4);
                    f.setInt(tile, tableIndex);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }
                this.hasRedstone = false;
            }
        }
    }
}
