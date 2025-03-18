package com.goldenfield192.irpatches.mixins.immersiverailroading.tile;

import cam72cam.immersiverailroading.entity.EntityRollingStock;
import cam72cam.immersiverailroading.library.Augment;
import cam72cam.immersiverailroading.thirdparty.trackapi.BlockEntityTrackTickable;
import cam72cam.immersiverailroading.tile.TileRailBase;
import cam72cam.mod.block.IRedstoneProvider;
import cam72cam.mod.entity.Player;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.util.Facing;
import com.goldenfield192.irpatches.accessor.ITileRailBaseAccessor;
import com.goldenfield192.irpatches.common.IRPGUIHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TileRailBase.class)
public abstract class MixinTileRailBase extends BlockEntityTrackTickable
                                        implements IRedstoneProvider, ITileRailBaseAccessor {
    @Shadow(remap = false) private Augment augment;

    @Shadow(remap = false) public abstract <T extends EntityRollingStock> T getStockNearBy(Class<T> type);

    @Shadow(remap = false) public abstract int getTicksExisted();

    @Unique
    private String IRPatch$filter;

    @Inject(method = "onClick", at = @At("HEAD"), remap = false, cancellable = true)
    public void inject(Player player, Player.Hand hand, Facing facing, Vec3d hit, CallbackInfoReturnable<Boolean> cir){
        if(player.getHeldItem(Player.Hand.PRIMARY).isEmpty() && this.augment.equals(Augment.ACTUATOR)){
            IRPGUIHelper.ACTUATOR.open(player, this.getPos());
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "save", at = @At("TAIL"), remap = false)
    public void save0(TagCompound nbt, CallbackInfo ci){
        nbt.setString("irpfilter", IRPatch$filter);
    }

    @Inject(method = "load", at = @At("TAIL"), remap = false)
    public void load0(TagCompound nbt, CallbackInfo ci){
        this.IRPatch$filter = nbt.getString("irpfilter");
    }

    @Inject(method = "update",
            at = @At(value = "INVOKE_ASSIGN", target = "Lcam72cam/mod/world/World;getRedstone(Lcam72cam/mod/math/Vec3i;)I", ordinal = 1),
            remap = false,
            cancellable = true)
    public void update0(CallbackInfo ci){
        EntityRollingStock stock = getStockNearBy(EntityRollingStock.class);
        if(stock == null){
            ci.cancel();
            return;
        }
        String[] cg = IRPatch$filter.split(",");
        float value = (float)this.getWorld().getRedstone(this.getPos()) / 15.0F;
        for(String s : cg) {
            stock.setControlPosition(s, value);
        }
        ci.cancel();
    }

    @Override
    public void IRPatch$setCGFilter(String s){
        IRPatch$filter = s;
        this.markDirty();
    }

    @Override
    public String IRPatch$getCGFilter() {
        return IRPatch$filter;
    }
}
