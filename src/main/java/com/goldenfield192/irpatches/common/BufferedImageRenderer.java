package com.goldenfield192.irpatches.common;

import cam72cam.mod.gui.helpers.GUIHelpers;
import cam72cam.mod.resource.Identifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BufferedImageRenderer {
    static DynamicTexture dynamicTexture;
    public static void draw() throws IOException {
        if(dynamicTexture == null){
        }

        BufferedImage image = ImageIO.read(new Identifier("immersiverailroading:textures/light.png").getResourceStream());
        dynamicTexture = new DynamicTexture(image);
        ResourceLocation location =
                Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("test", dynamicTexture);
        GUIHelpers.texturedRect(new Identifier(location), 0, 0, 30, 30);
    }
}
