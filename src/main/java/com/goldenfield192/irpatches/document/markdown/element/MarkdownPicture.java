package com.goldenfield192.irpatches.document.markdown.element;

import cam72cam.mod.gui.helpers.GUIHelpers;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.resource.Identifier;
import com.goldenfield192.irpatches.IRPConfig;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Element class representing a picture
 *
 * @see MarkdownElement
 */
public class MarkdownPicture extends MarkdownElement {
    public final Identifier picture;

    public final double ratio;
    public final BufferedImage image;

    public MarkdownPicture(Identifier picture) {
        this.picture = picture;
        try {
            this.image = ImageIO.read(picture.getResourceStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.ratio = (double) this.image.getHeight() / this.image.getWidth();
    }

    @Override
    public String apply() {
        return "";
    }

    @Override
    public MarkdownElement[] split(int splitPos) {
        return new MarkdownElement[0];
    }

    @Override
    public int render(RenderState state, int pageWidth) {
        Vec3d offset = state.model_view().apply(Vec3d.ZERO);
        int picHeight = (int) (pageWidth * this.ratio);
        GUIHelpers.texturedRect(this.picture, (int) offset.x, (int) offset.y, pageWidth, picHeight);
        state.translate(0, picHeight / IRPConfig.ManualFontSize, 0);
        return (int) (picHeight / IRPConfig.ManualFontSize);
    }
}
