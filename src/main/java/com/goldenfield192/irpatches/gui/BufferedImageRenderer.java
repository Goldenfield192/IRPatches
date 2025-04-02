package com.goldenfield192.irpatches.gui;

import cam72cam.mod.resource.Identifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;

public class BufferedImageRenderer {
    public static Identifier getTextureIdentifier(BufferedImage image) {
        ResourceLocation location =
                Minecraft.getMinecraft().getTextureManager()
                         .getDynamicTextureLocation("test", new DynamicTexture(image));
        return new Identifier(location);
    }
}
