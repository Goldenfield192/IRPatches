package com.goldenfield192.irpatches.mixins.minecraft;

import cam72cam.immersiverailroading.entity.EntityMoveableRollingStock;
import cam72cam.immersiverailroading.util.VecUtil;
import cam72cam.mod.entity.Player;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.world.World;
import com.goldenfield192.irpatches.accessor.IStockRollAccessor;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public class MixinRenderPlayer {
    @Inject(method = "applyRotations(Lnet/minecraft/client/entity/AbstractClientPlayer;FFF)V", at = @At("HEAD"), cancellable = true)
    public void inject(AbstractClientPlayer entityLiving, float ageInTicks, float rotationYaw, float partialTicks, CallbackInfo ci){
        if(World.get(entityLiving.world).getEntity(entityLiving).isPlayer()){
            Player umcPlayer = World.get(entityLiving.world).getEntity(entityLiving).asPlayer();
            if(umcPlayer.getRiding() instanceof EntityMoveableRollingStock){
                EntityMoveableRollingStock stock = (EntityMoveableRollingStock) umcPlayer.getRiding();
                float roll = ((IStockRollAccessor) stock).getAverageRollDegrees();
                float yaw = stock.getRotationYaw();
                float pitch = stock.getRotationPitch();

                GlStateManager.rotate(180 - stock.getRotationYaw(), 0, 1, 0);
                GlStateManager.rotate(roll, 0, 0, 1);
                GlStateManager.rotate(pitch, 1, 0, 0);
                GlStateManager.rotate(stock.getRotationYaw() - 180, 0, 1, 0);
                GlStateManager.rotate(180 - rotationYaw, 0, 1, 0);

                ci.cancel();
            }
        }
    }
}
