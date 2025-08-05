package com.goldenfield192.irpatches.mixins.features.transfer_table;

import cam72cam.immersiverailroading.entity.EntityCoupleableRollingStock;
import cam72cam.immersiverailroading.entity.physics.Simulation;
import cam72cam.immersiverailroading.library.TrackItems;
import cam72cam.immersiverailroading.tile.TileRail;
import cam72cam.immersiverailroading.tile.TileRailBase;
import cam72cam.immersiverailroading.util.RailInfo;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import com.goldenfield192.irpatches.IRPConfig;
import com.goldenfield192.irpatches.accessor.IRailSettingsAccessor;
import com.goldenfield192.irpatches.accessor.ITileRailAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(TileRail.class)
public class MixinTileRail extends TileRailBase implements ITileRailAccessor {
    @Shadow(remap = false) private int tableIndex;

    @Shadow(remap = false) public RailInfo info;

    @Override
    public void setTransferTablePos(Vec3i offset) {
        IRailSettingsAccessor settings = (IRailSettingsAccessor) info.settings;
        this.tableIndex = Math.max(0, Math.min(settings.getTransferTableEntryNum() - 1, Math.round((float) Math.abs(offset.x) / settings.getTransferTableEntryDistance())));
    }

    @Inject(method = "update", at = @At("TAIL"), remap = false)
    private void inject(CallbackInfo ci){
        if (getWorld().isServer && info != null && info.settings.type == TrackItems.valueOf("TRANSFER_TABLE")) {
            IRailSettingsAccessor accessor = (IRailSettingsAccessor) info.settings;
            float desiredPosition = tableIndex * accessor.getTransferTableEntryDistance();
            double speed = 0.1 * IRPConfig.TransferTableSpeedMultiplier;
            if (desiredPosition != info.tablePos) {
                if (Math.abs(desiredPosition - info.tablePos) < speed * 2) {
                    info = info.with(b -> b.tablePos = desiredPosition);
                } else {
                    double delta = desiredPosition - info.tablePos < 0 ? -speed : speed;
                    info = info.with(b -> b.tablePos += delta);
                }
                this.markDirty();
                int maxRange = (int) (Math.max(info.settings.length, accessor.getTransferTableEntryNum()*accessor.getTransferTableEntryDistance()) * 0.6);
                List<EntityCoupleableRollingStock> ents = getWorld().getEntities((EntityCoupleableRollingStock stock) -> stock.getPosition().distanceTo(new Vec3d(getPos())) < maxRange, EntityCoupleableRollingStock.class);
                for(EntityCoupleableRollingStock stock : ents) {
                    stock.states.forEach(state -> state.dirty = true);
                    Simulation.forceQuickUpdates = true;
                }
            }
        }
    }
}
