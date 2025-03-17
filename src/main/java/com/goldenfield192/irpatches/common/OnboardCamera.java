package com.goldenfield192.irpatches.common;

import cam72cam.immersiverailroading.entity.EntityRollingStock;
import cam72cam.mod.MinecraftClient;
import cam72cam.mod.entity.Entity;
import com.goldenfield192.irpatches.common.umc.IRPConfig;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class OnboardCamera {
    public static boolean enabled = false;
    public static double distance;
    public static double fov;

    private static double targetDistance;
    private static double targetFov;

    private static double prevDistance = 0;
//    private static double prevFov = 0;

    public static void onClientTick(){
        if(!MinecraftClient.isReady()){
            return;
        }

        Entity riding = MinecraftClient.getPlayer().getRiding();
        if (riding instanceof EntityRollingStock) {
            if(!enabled){
                enabled = true;
                targetDistance = prevDistance <= 20 ? 20 : prevDistance;
                if(Minecraft.getMinecraft().gameSettings.thirdPersonView != 1){
                    distance = prevDistance <= 20 ? 20 : prevDistance;
                }
            }
        }
        if(!(riding instanceof EntityRollingStock)){
            prevDistance = distance;
            targetDistance = 4;
            targetFov = Minecraft.getMinecraft().gameSettings.fovSetting;
            enabled = false;
        }

        if(Math.abs(targetDistance - distance) <= 0.001){
            distance = targetDistance;
        } else {
            distance += Math.min((targetDistance - distance) * 0.1, 0.75);
        }
        fov = targetFov;
    }

    public static boolean handleScroll(double d){
        if(!MinecraftClient.isReady()){
            return true;
        }

        if(MinecraftClient.getPlayer().getRiding() instanceof EntityRollingStock
                && enabled
                && Minecraft.getMinecraft().gameSettings.thirdPersonView != 0){
            if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
                targetDistance = Math.max(10, Math.min(IRPConfig.ThirdPersonMaxDistance, targetDistance - 1.5 * d));
                return false;
            } else if(Keyboard.isKeyDown(Keyboard.KEY_LMENU)){
                targetFov = Math.max(20, Math.min(80, targetFov - d));
                return false;
            }
        }
        return true;
    }
}
