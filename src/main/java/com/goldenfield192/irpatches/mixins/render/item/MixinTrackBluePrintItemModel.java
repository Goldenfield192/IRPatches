package com.goldenfield192.irpatches.mixins.render.item;

import cam72cam.immersiverailroading.render.item.TrackBlueprintItemModel;
import cam72cam.immersiverailroading.util.BlockUtil;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.world.World;
import net.minecraft.block.BlockLeaves;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Prevent leaves being replaced by track blueprint.
 * @see com.goldenfield192.irpatches.mixins.items.MixinItemTrackBlueprint
 */
@Mixin(TrackBlueprintItemModel.class)
public class MixinTrackBluePrintItemModel {
    @Redirect(method = "renderMouseover",
            at = @At(value = "INVOKE", target = "Lcam72cam/immersiverailroading/util/BlockUtil;canBeReplaced(Lcam72cam/mod/world/World;Lcam72cam/mod/math/Vec3i;Z)Z", ordinal = 0),
            remap = false)
    private static boolean mixinBlockCheck(World world, Vec3i pos, boolean flag){
        return BlockUtil.canBeReplaced(world, pos, flag) &&
                !(world.internal.getBlockState(pos.internal()).getBlock() instanceof BlockLeaves);
    }
}
