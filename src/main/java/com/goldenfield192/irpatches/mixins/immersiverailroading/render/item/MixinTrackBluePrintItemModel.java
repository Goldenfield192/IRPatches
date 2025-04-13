package com.goldenfield192.irpatches.mixins.immersiverailroading.render.item;

import cam72cam.immersiverailroading.render.item.TrackBlueprintItemModel;
import cam72cam.immersiverailroading.util.BlockUtil;
import cam72cam.mod.entity.Player;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.render.GlobalRender;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.world.World;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.block.LeavesBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

/**
 * Prevent leaves being replaced by track blueprint.
 *
 * @see com.goldenfield192.irpatches.mixins.immersiverailroading.items.MixinItemTrackBlueprint
 */
@Mixin(TrackBlueprintItemModel.class)
public class MixinTrackBluePrintItemModel {
    @Redirect(method = "renderMouseover",
            at = @At(value = "INVOKE", target = "Lcam72cam/immersiverailroading/util/BlockUtil;canBeReplaced(Lcam72cam/mod/world/World;Lcam72cam/mod/math/Vec3i;Z)Z", ordinal = 0),
            remap = false)
    private static boolean mixinBlockCheck(World world, Vec3i pos, boolean flag) {
        return BlockUtil.canBeReplaced(world, pos, flag) &&
                !(world.internal.getBlockState(pos.internal()).getBlock() instanceof LeavesBlock);
    }

    @ModifyArg(method = "renderMouseover", at = @At(value = "INVOKE", target = "Lcam72cam/immersiverailroading/render/rail/RailRender;render(Lcam72cam/immersiverailroading/util/RailInfo;Lcam72cam/mod/world/World;Lcam72cam/mod/math/Vec3i;ZLcam72cam/mod/render/opengl/RenderState;)V"), index = 4, remap = false)
    private static RenderState modArgs(RenderState state, @Share("state")LocalRef<RenderState> stateLocalRef){
        stateLocalRef.set(state);
        return state;
    }

    //TODO
    @Inject(method = "renderMouseover", at = @At("TAIL"), remap = false)
    private static void inject(Player player, ItemStack stack, Vec3i pos, Vec3d vec, RenderState state,
                               float partialTicks, CallbackInfo ci, @Local(ordinal = 2) Vec3d offset, @Share("state") LocalRef<RenderState> stateLocalRef){
        if(stateLocalRef.get() == null) return;
//        GlobalRender.drawText("Test", stateLocalRef.get(), new Vec3d(0,0,0), 0.2f, 0);
    }
}
