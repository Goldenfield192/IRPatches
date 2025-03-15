package com.goldenfield192.irpatches.common;

import com.goldenfield192.irpatches.IRPatches;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = IRPatches.MODID)
public class EventListener {
    @SubscribeEvent
    public static void onFOV(EntityViewRenderEvent.FOVModifier event) {
         if(OnboardCamera.enabled && Minecraft.getMinecraft().gameSettings.thirdPersonView == 1){
             event.setFOV((float) OnboardCamera.fov);
         }
    }
}
