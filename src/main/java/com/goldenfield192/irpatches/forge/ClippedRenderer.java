package com.goldenfield192.irpatches.forge;

import cam72cam.mod.gui.helpers.GUIHelpers;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class ClippedRenderer {
    public static int getScaleFactor() {
        return (int) Minecraft.getInstance().getWindow().getGuiScale();
    }

    public static void renderInRegion(int x, int y, int width, int height, Runnable function) {
        int x1 = x * getScaleFactor();
        int y1 = y * getScaleFactor();
        int x2 = width * getScaleFactor();
        int y2 = height * getScaleFactor();
        int screenHeight = GUIHelpers.getScreenHeight() * getScaleFactor();

        RenderSystem.enableScissor(x1, screenHeight - y1 - y2, x2, y2);

        function.run();

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
}
