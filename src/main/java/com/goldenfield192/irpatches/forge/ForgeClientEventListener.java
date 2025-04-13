package com.goldenfield192.irpatches.forge;

import com.goldenfield192.irpatches.IRPatches;
import com.goldenfield192.irpatches.IRPConfig;
import com.goldenfield192.irpatches.util.OnboardCamera;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.PointOfView;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = IRPatches.MODID, value = Dist.CLIENT)
public class ForgeClientEventListener {
    @SubscribeEvent
    public static void setOnboardFOV(EntityViewRenderEvent.FOVModifier event) {
        if (IRPConfig.EnableAdvancedCamera && Minecraft.getInstance().getEntityRenderDispatcher().options.getCameraType() != PointOfView.FIRST_PERSON) {
            event.setFOV((float) OnboardCamera.fov);
        }
    }


    @SubscribeEvent
    public static void setOnboardCameraPosition(EntityViewRenderEvent.CameraSetup event) {
        if (!IRPConfig.EnableAdvancedCamera || IRPConfig.OnboardCameraCollideWithBlock) {
            return;
        }
        if (Minecraft.getInstance().getEntityRenderDispatcher().options.getCameraType() == PointOfView.THIRD_PERSON_BACK) {//back
            RenderSystem.translated(0, 0, -(OnboardCamera.zoom - 4));
        } else if (Minecraft.getInstance().getEntityRenderDispatcher().options.getCameraType() == PointOfView.THIRD_PERSON_FRONT) {//front
            RenderSystem.translated(0, 0, (OnboardCamera.zoom - 4));
        }
    }
}
