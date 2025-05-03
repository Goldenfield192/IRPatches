package com.goldenfield192.irpatches.gui;

import cam72cam.immersiverailroading.tile.TileRailBase;
import cam72cam.mod.gui.GuiRegistry;
import cam72cam.mod.render.opengl.RenderContext;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.resource.Identifier;
import cam72cam.mod.util.With;
import com.goldenfield192.irpatches.IRPatches;
import com.goldenfield192.irpatches.document.ManualGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import util.Matrix4;

public class IRPGUIHelper {
    public static final GuiRegistry.GUI MANUAL =
            GuiRegistry.register(new Identifier(IRPatches.MODID, "MANUAL"), ManualGui::new);
    public static final GuiRegistry.BlockGUI ACTUATOR =
            GuiRegistry.registerBlock(TileRailBase.class, ActuatorGui::new);
    public static final GuiRegistry.GUI IRP_TRACK_BLUEPRINT =
            GuiRegistry.register(new Identifier(IRPatches.MODID, "track_blueprint"), IRPTrackGUI::new);

    public static int getTextWidth(String s) {
        return Minecraft.getMinecraft().fontRenderer.getStringWidth(s);
    }

    /**
     * Draw a left-aligned shadowed string
     */
    public static void drawString(String text, int x, int y, int color) {
        drawString(text, x, y, color, new Matrix4());
    }

    public static void drawString(String text, int x, int y, int color, Matrix4 matrix) {
        RenderState state = new RenderState().color(1, 1, 1, 1).alpha_test(true);
        state.model_view().multiply(matrix);
        try (With ctx = RenderContext.apply(state)) {
            GlStateManager.color(1, 1, 1, 0);
            Minecraft.getMinecraft().fontRenderer.drawString(text, x, y, color);
        }
    }

    public static void register() {
    }
}
