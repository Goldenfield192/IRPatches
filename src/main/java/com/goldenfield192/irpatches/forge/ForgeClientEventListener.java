package com.goldenfield192.irpatches.forge;

import com.goldenfield192.irpatches.IRPatches;
import com.goldenfield192.irpatches.IRPConfig;
import com.goldenfield192.irpatches.util.OnboardCamera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = IRPatches.MODID, value = Side.CLIENT)
public class ForgeClientEventListener {
    @SubscribeEvent
    public static void setOnboardFOV(EntityViewRenderEvent.FOVModifier event) {
        if (IRPConfig.EnableAdvancedCamera && OnboardCamera.enabled && Minecraft.getMinecraft().gameSettings.thirdPersonView != 0) {
            event.setFOV((float) OnboardCamera.fov);
        }
    }


    @SubscribeEvent
    public static void setOnboardCameraPosition(EntityViewRenderEvent.CameraSetup event) {
        if (!IRPConfig.EnableAdvancedCamera || IRPConfig.OnboardCameraCollideWithBlock) {
            return;
        }
        if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 1) {//back
            GlStateManager.translate(0, 0, -(OnboardCamera.distance - 4));
        } else if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 2) {//front
            GlStateManager.translate(0, 0, (OnboardCamera.distance - 4));
        }
    }
}
