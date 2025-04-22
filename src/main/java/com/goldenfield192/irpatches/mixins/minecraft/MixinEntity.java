package com.goldenfield192.irpatches.mixins.minecraft;

import cam72cam.mod.entity.boundingbox.DefaultBoundingBox;
import cam72cam.mod.entity.boundingbox.IBoundingBox;
import cam72cam.mod.entity.custom.ICollision;
import cam72cam.mod.world.World;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(Entity.class)
public class MixinEntity {
    @ModifyVariable(method = "move", at = @At("HEAD"), remap = false)
    public Vector3d inject(Vector3d movement){
        Entity self = (Entity)(Object)this;

        if(self instanceof PlayerEntity && self.isSpectator()){
            return movement;
        }

        List<cam72cam.mod.entity.Entity> entities = World.get(self.level).getEntities(entity -> entity instanceof ICollision, cam72cam.mod.entity.Entity.class);
        IBoundingBox current = new DefaultBoundingBox(self.getBoundingBox());
        IBoundingBox next = new DefaultBoundingBox(self.getBoundingBox().move(movement));
        boolean currCollision = entities.stream().anyMatch(entity -> ((ICollision)entity).getCollision().intersects(current));
        boolean nextCollision = entities.stream().anyMatch(entity -> ((ICollision)entity).getCollision().intersects(next));
        if(currCollision && nextCollision){ //Entity is stuck
            self.setDeltaMovement(new Vector3d(0,0.05,0));
            return new Vector3d(0,0.05,0);
        } else if(nextCollision){ //It is trying to move in
            self.setDeltaMovement(new Vector3d(0,0,0));
            return new Vector3d(0,0,0);
        }
        //Let it out
        return movement;
    }
}
