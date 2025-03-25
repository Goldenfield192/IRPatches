package com.goldenfield192.irpatches.document.markdown.element;

import cam72cam.mod.render.opengl.RenderState;

import java.util.HashMap;

/**
 * Abstract element class
 *
 * @see MarkdownTitle
 * @see MarkdownStyledText
 * @see MarkdownPicture
 * @see MarkdownSplitLine
 * @see MarkdownClickableElement
 */
public abstract class MarkdownElement {
    public String text;

    public HashMap<String, Object> extra = new HashMap<>();

    /**
     * Apply this element to Renderable string
     */
    public abstract String apply();

    /**
     * Split this element into two smaller ones
     */
    public abstract MarkdownElement[] split(int splitPos);

    /**
     * Render the element and return its height
     */
    public abstract int render(RenderState state, int pageWidth);
}
