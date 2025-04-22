package com.goldenfield192.irpatches.mixins.minecraft;

import cam72cam.mod.entity.Entity;
import cam72cam.mod.entity.boundingbox.DefaultBoundingBox;
import cam72cam.mod.entity.boundingbox.IBoundingBox;
import cam72cam.mod.entity.custom.ICollision;
import cam72cam.mod.world.World;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
    @Unique
    private static final AxisAlignedBB IRPatch$RANGE =
            new AxisAlignedBB(-10, -10, -10, 10, 10, 10);

//    @ModifyVariable(method = "travel", at = @At("HEAD"), remap = false)
//    public Vector3d inject(Vector3d movement){
//    }
}
