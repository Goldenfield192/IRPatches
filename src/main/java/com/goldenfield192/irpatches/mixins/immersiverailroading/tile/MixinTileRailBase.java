package com.goldenfield192.irpatches.mixins.immersiverailroading.tile;

import cam72cam.immersiverailroading.entity.EntityRollingStock;
import cam72cam.immersiverailroading.items.nbt.RailSettings;
import cam72cam.immersiverailroading.library.Augment;
import cam72cam.immersiverailroading.library.SwitchState;
import cam72cam.immersiverailroading.library.TrackItems;
import cam72cam.immersiverailroading.physics.MovementTrack;
import cam72cam.immersiverailroading.thirdparty.trackapi.BlockEntityTrackTickable;
import cam72cam.immersiverailroading.tile.TileRail;
import cam72cam.immersiverailroading.tile.TileRailBase;
import cam72cam.immersiverailroading.track.BuilderBase;
import cam72cam.immersiverailroading.track.BuilderCubicCurve;
import cam72cam.immersiverailroading.track.CubicCurve;
import cam72cam.immersiverailroading.util.MathUtil;
import cam72cam.immersiverailroading.util.SwitchUtil;
import cam72cam.immersiverailroading.util.VecUtil;
import cam72cam.mod.block.IRedstoneProvider;
import cam72cam.mod.entity.Player;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.util.Facing;
import com.goldenfield192.irpatches.IRPConfig;
import com.goldenfield192.irpatches.accessor.ITileRailBaseAccessor;
import com.goldenfield192.irpatches.gui.IRPGUIHelper;
import com.goldenfield192.irpatches.util.TrackRoll;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Mixin(TileRailBase.class)
public abstract class MixinTileRailBase extends BlockEntityTrackTickable
        implements IRedstoneProvider, ITileRailBaseAccessor {
    @Shadow(remap = false) private Augment augment;
    @Shadow(remap = false) private Collection<TileRail> tiles;
    @Shadow(remap = false) private int ticksExisted;
    @Unique
    private String IRPatch$filter;

    @Shadow(remap = false)
    public abstract <T extends EntityRollingStock> T getStockNearBy(Class<T> type);

    @Shadow(remap = false)
    public abstract TagCompound getReplaced();

    @Inject(method = "onClick", at = @At("HEAD"), remap = false, cancellable = true)
    public void inject(Player player, Player.Hand hand, Facing facing, Vec3d hit, CallbackInfoReturnable<Boolean> cir) {
        if (player.getHeldItem(Player.Hand.PRIMARY).isEmpty() && Augment.ACTUATOR.equals(this.augment)) {
            IRPGUIHelper.ACTUATOR.open(player, this.getPos());
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "save", at = @At("TAIL"), remap = false)
    public void save0(TagCompound nbt, CallbackInfo ci) {
        nbt.setString("irpfilter", IRPatch$filter);
    }

    @Inject(method = "load", at = @At("TAIL"), remap = false)
    public void load0(TagCompound nbt, CallbackInfo ci) {
        this.IRPatch$filter = nbt.getString("irpfilter");
    }

    @Inject(method = "update",
            at = @At(value = "INVOKE_ASSIGN", target = "Lcam72cam/mod/world/World;getRedstone(Lcam72cam/mod/math/Vec3i;)I", ordinal = 1),
            remap = false,
            cancellable = true)
    public void update0(CallbackInfo ci) {
        EntityRollingStock stock = getStockNearBy(EntityRollingStock.class);
        if (stock == null) {
            ci.cancel();
            return;
        }
        if(IRPatch$filter.isEmpty()){
            //Fallback for actual door actuator
            return;
        }
        String[] cg = IRPatch$filter.split(",");
        float value = (float) this.getWorld().getRedstone(this.getPos()) / 15.0F;
        for (String s : cg) {
            stock.setControlPosition(s, value);
        }
        ci.cancel();
    }

    @Inject(method = "update", at = @At(value = "HEAD"), remap = false)
    public void update1(CallbackInfo ci){
        TileRailBase self = (TileRailBase)(Object)this;
        
        if(IRPConfig.TrackExchangerChangeEntireTrack
                && !this.getWorld().isClient
                && ticksExisted % 5 == 0
                && self instanceof TileRail
                && self.getParentTile() != null){
            RailSettings settings = self.getParentTile().info.settings;
            if(!Objects.equals(settings.track, ((TileRail) self).info.settings.track) ||
               !Objects.equals(settings.railBed, ((TileRail) self).info.settings.railBed) ||
               !Objects.equals(settings.gauge, ((TileRail) self).info.settings.gauge)){
                ((TileRail) self).info = ((TileRail) self).info.withSettings(b -> {
                    b.track = settings.track;
                    b.railBed = settings.railBed;
                    b.gauge = settings.gauge;
                });
                ((TileRail) self).markAllDirty();
            }
        }
    }

    @Redirect(method = "onClick", at = @At(value = "INVOKE", target = "Lcam72cam/immersiverailroading/tile/TileRailBase;getParentTile()Lcam72cam/immersiverailroading/tile/TileRail;"), remap = false)
    public TileRail onClick0(TileRailBase instance){
        TileRail tileRail = instance.getParentTile();
        if(IRPConfig.TrackExchangerChangeEntireTrack && tileRail.getParentTile() != null){
            tileRail = tileRail.getParentTile();
        }
        return tileRail;
    }

    @Override
    public float getNextRoll(Vec3d currentPosition, Vec3d motion) {
        TileRailBase self = (TileRailBase) (BlockEntityTrackTickable) this;
        if (this.getReplaced() == null) {
            TileRail tile = self instanceof TileRail ?
                            (TileRail) self :
                            self.getParentTile();
            if (tile == null) {
                return 0;
            }

            SwitchState state = SwitchUtil.getSwitchState(tile, currentPosition);

            if (state == SwitchState.STRAIGHT) {
                tile = tile.getParentTile();
            }

            return TrackRoll.getRollMovementTrack(getWorld(), currentPosition, tile, motion);
        }
        if (this.tiles == null) {
            Map<Vec3i, TileRail> tileMap = new HashMap<>();
            for (TileRailBase current = self; current != null; current = current.getReplacedTile()) {
                TileRail tile = current instanceof TileRail ?
                                (TileRail) current :
                                current.getParentTile();
                TileRail parent = tile;
                while (parent != null && !parent.getPos().equals(parent.getParent())) {
                    // Move to root of switch (if applicable)
                    parent = parent.getParentTile();
                }
                if (tile != null && parent != null) {
                    tileMap.putIfAbsent(parent.getPos(), tile);
                }
            }
            tiles = tileMap.values();
        }


        Vec3d nextPos = currentPosition;
        Vec3d predictedPos = currentPosition.add(motion);
        boolean hasSwitchSet = false;
        float nextRoll = 0;

        for (TileRail tile : tiles) {
            SwitchState state = SwitchUtil.getSwitchState(tile, currentPosition);

            if (state == SwitchState.STRAIGHT) {
                tile = tile.getParentTile();
            }

            Vec3d potential = MovementTrack.nextPositionDirect(getWorld(), currentPosition, tile, motion);
            if (potential != null) {
                if (state == SwitchState.TURN) {
                    float other = VecUtil.toWrongYaw(potential.subtract(currentPosition));
                    float rotationYaw = VecUtil.toWrongYaw(motion);
                    double diff = MathUtil.trueModulus(other - rotationYaw, 360);
                    diff = Math.min(360 - diff, diff);
                    if (diff < 2.5) {
                        hasSwitchSet = true;
                        nextPos = potential;
                        nextRoll = TrackRoll.getRollMovementTrack(getWorld(), currentPosition, tile, motion);
                    }
                }
                if (currentPosition == nextPos || !hasSwitchSet && potential.distanceToSquared(
                        predictedPos) < nextPos.distanceToSquared(predictedPos)) {
                    nextPos = potential;
                    nextRoll = TrackRoll.getRollMovementTrack(getWorld(), currentPosition, tile, motion);
                }
            }
        }
        return nextRoll;
    }

    //false mean we need to invert roll
    public boolean getDirectionAlong(Vec3d currentPosition, Vec3d stockDirection) {
        TileRailBase self = (TileRailBase) (BlockEntityTrackTickable) this;
        TileRail rail = self instanceof TileRail ?
                        (TileRail) self :
                        self.getParentTile();
        BuilderBase builderBase = (rail.info.settings.type == TrackItems.SWITCH
                                   ?
                                   rail.info.withSettings(b -> b.type = TrackItems.STRAIGHT)
                                   :
                                   rail.info).getBuilder(getWorld(),
                                                         new Vec3i(rail.info.placementInfo.placementPosition).add(
                                                                 getPos()));
        if (!(builderBase instanceof BuilderCubicCurve)) {
            return true;
        }
        BuilderCubicCurve curve = (BuilderCubicCurve) builderBase;
        CubicCurve cubicCurve = curve.getCurve();
        Vec3d track = cubicCurve.p2.subtract(cubicCurve.p1).normalize();
        return track.x * stockDirection.x + track.y * stockDirection.y + track.z * stockDirection.z > 0;
    }

    @Override
    public String getCGFilter() {
        return IRPatch$filter;
    }

    @Override
    public void setCGFilter(String s) {
        IRPatch$filter = s;
        this.markDirty();
    }
}
