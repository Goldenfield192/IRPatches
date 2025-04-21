package com.goldenfield192.irpatches.document.markdown.element;

import cam72cam.mod.render.opengl.RenderState;

/**
 * Abstract element class
 *
 * @see MarkdownTitle
 * @see MarkdownStyledText
 * @see MarkdownPicture
 * @see MarkdownSplitLine
 * @see MarkdownClickableElement
 */
public abstract class AbstractMarkdownElement {
    public String text;

    /**
     * Apply this element to Renderable string
     */
    public abstract String apply();

    /**
     * Split this element into two smaller ones
     */
    public abstract AbstractMarkdownElement[] split(int splitPos);

    /**
     * Render the element and return its height
     */
    public abstract int render(RenderState state, int pageWidth);
}
