package com.goldenfield192.irpatches.common;

import com.goldenfield192.irpatches.IRPatches;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = IRPatches.MODID)
public class ForgeEventListener {
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void setOnboardFOV(EntityViewRenderEvent.FOVModifier event) {
         if(OnboardCamera.enabled && Minecraft.getMinecraft().gameSettings.thirdPersonView != 0){
             event.setFOV((float) OnboardCamera.fov);
         }
    }
}
