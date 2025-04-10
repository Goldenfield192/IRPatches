package com.goldenfield192.irpatches.util;

import cam72cam.immersiverailroading.entity.EntityRollingStock;
import cam72cam.mod.MinecraftClient;
import cam72cam.mod.entity.Entity;
import com.goldenfield192.irpatches.IRPConfig;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class OnboardCamera {
    public static boolean enabled = false;
    public static double zoom;
    public static double fov;

    public static int zoomDown = 0;
    public static int fovDown = 0;

    private static double targetZoom = 4;
    private static double targetFov = 70;

    private static double prevZoom = -1;
    private static double prevFov = -1;


    public static void onClientTick() {
        if (!MinecraftClient.isReady()) {
            return;
        }

        zoomDown = Math.max(0, zoomDown - 1);
        fovDown = Math.max(0, fovDown - 1);

        Entity riding = MinecraftClient.getPlayer().getRiding();
        if (riding instanceof EntityRollingStock) {
            if (!enabled) {
                enabled = true;
                targetZoom = prevZoom == -1 ? 20 : prevZoom;
                targetFov = prevFov == -1 ? Minecraft.getMinecraft().gameSettings.fovSetting : prevFov;
            }
        }
        if (!(riding instanceof EntityRollingStock)) {
            if(enabled){
                prevZoom = targetZoom;
                prevFov = targetFov;
            }

            targetZoom = 4;
            targetFov = Minecraft.getMinecraft().gameSettings.fovSetting;
            enabled = false;
        }

        if (Math.abs(targetZoom - zoom) <= 0.001) {
            zoom = targetZoom;
        } else {
            zoom += Math.min((targetZoom - zoom) * 0.1, 2);
        }

        if (Math.abs(targetFov - fov) <= 0.001) {
            fov = targetFov;
        } else {
            fov += Math.min((targetFov - fov) * 0.1, 0.75);
        }
    }

    public static boolean handleScroll(double d) {
        if (!MinecraftClient.isReady() || !IRPConfig.EnableAdvancedCamera) {
            return true;
        }

        if (MinecraftClient.getPlayer().getRiding() instanceof EntityRollingStock
                && enabled
                && Minecraft.getMinecraft().gameSettings.thirdPersonView != 0) {
            if (zoomDown > 0) {
                targetZoom = Math.max(10, Math.min(IRPConfig.ThirdPersonMaxDistance, targetZoom - 1.5 * d));
                return false;
            }
            if (fovDown > 0) {
                targetFov = Math.max(20, Math.min(90, targetFov - d));
                return false;
            }
        }
        return true;
    }
}
