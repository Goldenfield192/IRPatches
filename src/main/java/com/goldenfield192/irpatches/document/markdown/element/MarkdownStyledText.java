package com.goldenfield192.irpatches.document.markdown.element;

import cam72cam.mod.gui.helpers.GUIHelpers;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.text.TextColor;
import com.goldenfield192.irpatches.common.IRPGUIHelper;
import com.goldenfield192.irpatches.common.umc.IRPConfig;

import java.util.*;
import java.util.stream.Collectors;

import static com.goldenfield192.irpatches.document.markdown.Colors.CODE_BACKGROUND_COLOR;
import static com.goldenfield192.irpatches.document.markdown.Colors.DEFAULT_TEXT_COLOR;

/**
 * Element class representing a String with styles
 */
public class MarkdownStyledText extends MarkdownElement {
    public static final Map<String, Set<MarkdownTextStyle>> MARKER_STYLES;
    public static final List<String> MARKER_PARSE_PRIORITY = Arrays.asList("***", "++", "**", "~~", "*", "`");

    static {
        MARKER_STYLES = new HashMap<>();
        MARKER_STYLES.put("***", EnumSet.of(MarkdownTextStyle.BOLD, MarkdownTextStyle.ITALIC));
        MARKER_STYLES.put("++", EnumSet.of(MarkdownTextStyle.UNDERLINE));
        MARKER_STYLES.put("**", EnumSet.of(MarkdownTextStyle.BOLD));
        MARKER_STYLES.put("~~", EnumSet.of(MarkdownTextStyle.STRIKETHROUGH));
        MARKER_STYLES.put("*", EnumSet.of(MarkdownTextStyle.ITALIC));
        MARKER_STYLES.put("`", EnumSet.of(MarkdownTextStyle.CODE));
    }

    public final Set<MarkdownTextStyle> styles;

    public MarkdownStyledText(String text) {
        this(text, Collections.emptySet());
    }

    public MarkdownStyledText(String text, MarkdownTextStyle... styles) {
        this.text = text;
        this.styles = Arrays.stream(styles).collect(Collectors.toSet());
    }

    public MarkdownStyledText(String text, Set<MarkdownTextStyle> styles) {
        this.text = text;
        this.styles = styles;
    }

    public boolean hasBold() {
        return this.styles.contains(MarkdownTextStyle.BOLD);
    }

    public boolean hasCode() {
        return this.styles.contains(MarkdownTextStyle.CODE);
    }

    @Override
    public String apply() {
        String str = text;
        for (MarkdownTextStyle style : styles) {
            str = style.wrapper.wrap(str);
        }
        return str;
    }

    @Override
    public MarkdownElement[] split(int splitPos) {
        int i = splitPos;
        while (this.text.charAt(i) == ' ') {
            i++;
            if (i == this.text.length()) {//Reaching end, which means chars after splitPos are all spaces
                return new MarkdownElement[]{
                        new MarkdownStyledText(this.text.substring(0, splitPos), this.styles),
                        //Just return empty String
                        new MarkdownStyledText("", this.styles)};
            }
        }
        return new MarkdownElement[]{
                new MarkdownStyledText(this.text.substring(0, splitPos), this.styles),
                new MarkdownStyledText(this.text.substring(i), this.styles)};
    }

    @Override
    public int render(RenderState state, int pageWidth) {
        String str = this.apply();
        if (this.hasCode()) {
            Vec3d offset = state.model_view().apply(Vec3d.ZERO);
            GUIHelpers.drawRect((int) offset.x - 2, (int) offset.y - 1,
                                (int) (IRPGUIHelper.getTextWidth(str) * IRPConfig.ManualFontSize + 4),
                                (int) (12 * IRPConfig.ManualFontSize), CODE_BACKGROUND_COLOR);
            IRPGUIHelper.drawString(str, 0, 0, DEFAULT_TEXT_COLOR, state.model_view());
            state.translate(IRPGUIHelper.getTextWidth(str) + 2, 0, 0);
        } else {
            IRPGUIHelper.drawString(str, 0, 0, DEFAULT_TEXT_COLOR, state.model_view());
            state.translate(IRPGUIHelper.getTextWidth(str), 0, 0);
        }
        return 0;
    }

    /**
     * All basic text styles
     */
    public enum MarkdownTextStyle {
        BOLD(TextColor.BOLD),
        ITALIC(TextColor.ITALIC),
        STRIKETHROUGH(TextColor.STRIKETHROUGH),
        UNDERLINE(TextColor.UNDERLINE),
        CODE(TextColor.BLACK);

        public final TextColor wrapper;

        MarkdownTextStyle(TextColor wrapper) {
            this.wrapper = wrapper;
        }
    }
}
