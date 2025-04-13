package com.goldenfield192.irpatches.gui;

import cam72cam.mod.resource.Identifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;

public class BufferedImageRenderer {
    public static Identifier getTextureIdentifier(BufferedImage image) {
        NativeImage image1 = new NativeImage(NativeImage.PixelFormat.RGB, image.getWidth(), image.getHeight(), true);
        for(int w = 0; w < image.getWidth(); w++){
            for (int h = 0; h < image.getHeight(); h++) {
                image1.setPixelRGBA(w, h, image.getRGB(w, h));
            }
        }

        ResourceLocation location =
                Minecraft.getInstance().getTextureManager()
                         .register("test", new DynamicTexture(image1));
        return new Identifier(location);
    }
}
