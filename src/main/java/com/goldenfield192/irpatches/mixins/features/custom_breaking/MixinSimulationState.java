package com.goldenfield192.irpatches.mixins.features.custom_breaking;

import cam72cam.immersiverailroading.entity.physics.SimulationState;
import cam72cam.immersiverailroading.util.BlockUtil;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.world.BlockInfo;
import cam72cam.mod.world.World;
import com.goldenfield192.irpatches.IRPConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.List;

@Mixin(SimulationState.class)
public class MixinSimulationState {
    @Shadow(remap = false) public List<Vec3i> interferingBlocks;

    //TODO I don't know how to change if condition...
    @Redirect(method = "calculateBlockCollisions", at = @At(value = "INVOKE", target = "Lcam72cam/immersiverailroading/util/BlockUtil;isIRRail(Lcam72cam/mod/world/World;Lcam72cam/mod/math/Vec3i;)Z", ordinal = 0), remap = false)
    public boolean redirect(World world, Vec3i pos, @Share("bool") LocalBooleanRef ref){
        boolean isWhiteList;
        BlockInfo info = world.getBlock(pos);
        try {
            Field f = BlockInfo.class.getDeclaredField("internal");
            f.setAccessible(true);
            String registryKey = ((IBlockState)f.get(info)).getBlock().getRegistryName().toString();
            isWhiteList = IRPConfig.whiteList.containsKey(registryKey) && IRPConfig.whiteList.get(registryKey);
        } catch (NoSuchFieldException | IllegalAccessException ignore) {
            throw new RuntimeException();
        }
        ref.set(isWhiteList);
        return BlockUtil.isIRRail(world, pos);
    }

    @Inject(method = "calculateBlockCollisions", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1), remap = false)
    private void condition(List<Vec3i> blocksAlreadyBroken, CallbackInfo ci){
        interferingBlocks.remove(interferingBlocks.size() - 1);
    }

    @WrapOperation(method = "calculateBlockCollisions", at = @At(value = "INVOKE", target = "Lcam72cam/mod/world/World;getBlockHardness(Lcam72cam/mod/math/Vec3i;)F"), remap = false)
    private float wrap(World instance, Vec3i pos, Operation<Float> original, @Share("bool") LocalBooleanRef ref){
        if(ref.get()){
            return 0;
        } else {
            return original.call(instance, pos);
        }
    }
}
