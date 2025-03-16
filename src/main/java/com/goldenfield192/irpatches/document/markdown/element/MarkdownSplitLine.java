package com.goldenfield192.irpatches.document.markdown.element;

import cam72cam.mod.gui.helpers.GUIHelpers;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.render.opengl.RenderState;
import com.goldenfield192.irpatches.common.umc.IRPConfig;

import static com.goldenfield192.irpatches.document.markdown.Colors.SPLIT_LINE_COLOR;

/**
 * Element class representing a split line
 * @see MarkdownElement
 */
public class MarkdownSplitLine extends MarkdownElement {
    private static final char[] ALLOWED_CHARS_FOR_SPLIT = new char[]{'*', '-', '_'};

    public static boolean validate(String str){
        if(str.length() < 3){
            return false;
        }
        for(char c : ALLOWED_CHARS_FOR_SPLIT){
            label: {
                for (int i = 0; i < str.length(); i++) {
                    if (str.charAt(i) != c) {
                        break label;
                    }
                }
                return true;
            }
        }
        return false;
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
        state.translate(0, 10,0);
        Vec3d offset = state.model_view().apply(Vec3d.ZERO);
        GUIHelpers.drawRect((int) offset.x, (int) offset.y, pageWidth, (int) (2 * IRPConfig.ManualFontSize),  SPLIT_LINE_COLOR);
        state.translate(0, (int) (2 * IRPConfig.ManualFontSize),0);
        state.translate(0, 10,0);
        return 20 + (int) (2 * IRPConfig.ManualFontSize);
    }
}
