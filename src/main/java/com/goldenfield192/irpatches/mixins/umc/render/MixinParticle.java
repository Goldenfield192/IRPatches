package com.goldenfield192.irpatches.mixins.umc.render;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.render.Particle;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import util.Matrix4;

@Mixin(Particle.class)
public class MixinParticle {
    //If camera and player are too far away there are some bugs...
    //So in that case we let particles face the camera
    @Inject(method = "lookAtPlayer", at = @At("HEAD"), remap = false, cancellable = true)
    public void lookAtPlayer0(Matrix4 mat, CallbackInfo ci) {
        if (Minecraft.getMinecraft().gameSettings.thirdPersonView != 0) {//Not in first-person view
            Vec3d vec3d = MinecraftClient.getPlayer().getLookVector();
            double x = vec3d.x;
            double y = vec3d.y;
            double z = vec3d.z;
            mat.rotate(Math.toRadians(180 - Math.toDegrees(MathHelper.atan2(-x, z))), 0, 1, 0);
            mat.rotate(Math.toRadians(180 - Math.toDegrees(MathHelper.atan2(Math.sqrt(z * z + x * x), y))) + 90, 1, 0,
                       0);
            ci.cancel();
        }
    }
}
