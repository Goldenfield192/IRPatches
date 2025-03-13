package com.goldenfield192.irpatches.document.markdown.element;

import cam72cam.mod.math.Vec3d;
import cam72cam.mod.render.opengl.RenderState;
import com.goldenfield192.irpatches.common.ManualGUIHelper;

import static com.goldenfield192.irpatches.document.markdown.Colors.DEFAULT_TEXT_COLOR;

/**
 * Element class representing a title
 * <p>
 * Text will be scale by level:
 * <p>
 * 1 -> scale to 1.8x
 * <p>
 * 2 -> scale to 1.5x
 * <p>
 * 3 -> scale to 1.2x
 * <p>
 * 4+ -> normal text
 * <p>
 * CANNOT CONTAIN URL
 * @see MarkdownElement
 */
public class MarkdownTitle extends MarkdownElement {
    //Starting from 1
    public final int level;

    //Store reciprocals to avoid division
    public static final double LEVEL1 = 1/1.8;
    public static final double LEVEL2 = 1/1.5;
    public static final double LEVEL3 = 1/1.2;

    public MarkdownTitle(String text) {
        label: {
            for (int i = 0; i < text.length(); i++) {
                if (text.charAt(i) != '#') {
                    this.level = i;
                    this.text = text.substring(i).trim();
                    break label;
                }
            }
            //All the chars are '#'
            this.text = "";
            this.level = -1;
        }
    }

    public MarkdownTitle(String text, int level) {
        this.text = text;
        this.level = level;
    }

    @Override
    public String apply() {
        if(level == -1){//Invalid
            return "";
        } else {
            return text;
        }
    }

    @Override
    public MarkdownElement[] split(int splitPos) {
        int i = splitPos;
        while (this.text.charAt(i) == ' '){
            i++;
            if(i == this.text.length()){//Reaching end, which means chars after splitPos are all spaces
                return new MarkdownElement[]{
                        new MarkdownTitle(this.text.substring(0, splitPos), this.level),
                        //Just return empty string
                        new MarkdownTitle("", this.level)};
            }
        }
        return new MarkdownElement[]{
                new MarkdownTitle(this.text.substring(0, splitPos), this.level),
                new MarkdownTitle(this.text.substring(i), this.level)};
    }

    @Override
    public int render(RenderState state, int pageWidth){
        Vec3d offset = state.model_view().apply(Vec3d.ZERO);
        String str = this.apply();
        if(this.level == 1){
            //Scale matrix
            state.scale(1.8, 1.8, 1.8);
            ManualGUIHelper.drawString(str, 0, 0, DEFAULT_TEXT_COLOR, state.model_view());

            //Revert matrix
            state.scale(LEVEL1, LEVEL1, LEVEL1);

            //Move down
            state.translate(0, 18, 0);
            return 18;
        } else if(this.level == 2){
            //Scale matrix
            state.scale(1.5, 1.5, 1.5);
            ManualGUIHelper.drawString(str, 0, 0, DEFAULT_TEXT_COLOR, state.model_view());

            //Revert matrix
            state.scale(LEVEL2, LEVEL2, LEVEL2);
            //Move down
            state.translate(0, 15, 0);
            return 15;
        } else if (this.level == 3) {
            //Scale matrix
            state.scale(1.2, 1.2, 1.2);
            ManualGUIHelper.drawString(str, 0, 0, DEFAULT_TEXT_COLOR, state.model_view());

            //Revert matrix
            state.scale(LEVEL3, LEVEL3, LEVEL3);
            //Move down
            state.translate(0, 12, 0);
            return 12;
        } else {
            ManualGUIHelper.drawString(str, 0, 0, DEFAULT_TEXT_COLOR, state.model_view());
            state.translate(0, 10, 0);
            return 10;
        }
    }
}
