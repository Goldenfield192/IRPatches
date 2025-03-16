package com.goldenfield192.irpatches.common;

import cam72cam.mod.gui.helpers.GUIHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

//Version Specific!!!
public class ClippedRenderer {
    public static int getScaleFactor(){
        return new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
    }

    public static void renderInRegion(int x, int y, int width, int height, Runnable function){
        int x1 = x * getScaleFactor();
        int y1 = y * getScaleFactor();
        int x2 = width * getScaleFactor();
        int y2 = height * getScaleFactor();
        int screenHeight = GUIHelpers.getScreenHeight() * getScaleFactor();

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(x1, screenHeight - y1 - y2, x2, y2);

        function.run();

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
}
