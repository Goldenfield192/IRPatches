package com.goldenfield192.irpatches.forge;

import com.goldenfield192.irpatches.IRPatches;
import com.goldenfield192.irpatches.IRPConfig;
import com.goldenfield192.irpatches.util.OnboardCamera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.settings.PointOfView;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
        Method move;
        try {
            move = ActiveRenderInfo.class.getDeclaredMethod("move", double.class, double.class, double.class);
            move.setAccessible(true);
            if (Minecraft.getInstance().getEntityRenderDispatcher().options.getCameraType() != PointOfView.FIRST_PERSON) {
                move.invoke(Minecraft.getInstance().gameRenderer.getMainCamera(), -(OnboardCamera.zoom - 4), 0, 0);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
