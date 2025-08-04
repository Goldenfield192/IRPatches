package com.goldenfield192.irpatches.mixins.features.custom_breaking;

import cam72cam.immersiverailroading.entity.EntityMoveableRollingStock;
import cam72cam.immersiverailroading.entity.EntityRidableRollingStock;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.world.BlockInfo;
import cam72cam.mod.world.World;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.lang.reflect.Field;

@Mixin(EntityMoveableRollingStock.class)
public class MixinEntityMoveableRollingStock extends EntityRidableRollingStock {
    @Redirect(method = "onTick", at = @At(value = "INVOKE", target = "Lcam72cam/mod/world/World;breakBlock(Lcam72cam/mod/math/Vec3i;Z)V"), remap = false)
    public void redirect(World instance, Vec3i pos, boolean drop){

        BlockInfo info = instance.getBlock(pos);
        try {
            Field f = BlockInfo.class.getDeclaredField("internal");
            f.setAccessible(true);
            System.out.println(((IBlockState)f.get(info)).getBlock().getRegistryName());
        } catch (NoSuchFieldException | IllegalAccessException ignore) {}
    }
}
