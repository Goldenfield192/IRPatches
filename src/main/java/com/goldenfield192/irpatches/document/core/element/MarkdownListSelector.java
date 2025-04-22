package com.goldenfield192.irpatches.document.core.element;

import cam72cam.mod.gui.helpers.GUIHelpers;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.resource.Identifier;
import com.goldenfield192.irpatches.gui.IRPGUIHelper;
import com.goldenfield192.irpatches.IRPConfig;
import com.goldenfield192.irpatches.document.core.MarkdownDocument;
import com.goldenfield192.irpatches.document.core.MarkdownPageManager;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.goldenfield192.irpatches.document.core.Colors.DEFAULT_TEXT_COLOR;

/**
 *
 */
public class MarkdownListSelector extends MarkdownClickableElement {
    public static final Pattern LIST_SELECTOR_PATTERN =
            Pattern.compile("\\[(?<name>[^\\[\\] ]+)]\\{(?!\\s*})(?<content>[^{}]+)}");// [name]{content,allow space}

    private final List<String> choices = new LinkedList<>();
    private final String name;
    private int maxLength = -1;
    private int currentState;

    public MarkdownListSelector(String input) {
        Matcher matcher = LIST_SELECTOR_PATTERN.matcher(input);
        if (matcher.find()) {
            this.name = matcher.group("name");
            String[] sel = matcher.group("content").split(",");
            Arrays.stream(sel).map(String::trim).forEach(str -> {
                choices.add(str);
                maxLength = Math.max(IRPGUIHelper.getTextWidth(str), maxLength);
            });
            currentState = 0;
        } else {
            throw new RuntimeException();
        }
    }

    public int getMaxLength() {
        return maxLength;
    }

    public String getName() {
        return name;
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

    @Override
    public String apply() {
        return "";
    }

    @Override
    public AbstractMarkdownElement[] split(int splitPos) {
        return new AbstractMarkdownElement[0];
    }

    @Override
    public int render(RenderState state, int pageWidth) {
        state.translate(0, 2, 0);
        Vec3d offset = state.model_view().apply(Vec3d.ZERO);
        GUIHelpers.drawRect((int) offset.x - 2, (int) offset.y - 1,
                            (int) ((maxLength + 2) * IRPConfig.ManualFontSize),
                            (int) (12 * IRPConfig.ManualFontSize), 0xFFDDDDFF);
        IRPGUIHelper.drawString(choices.get(currentState), 0, 0, DEFAULT_TEXT_COLOR, state.model_view());
        state.translate(0, 12, 0);
        return 16;
    }

    @Override
    public void click(MarkdownDocument context) {
        currentState = currentState == choices.size() - 1 ?
                       0 :
                       currentState + 1;
        context.changeProperty(name, currentState);
        MarkdownPageManager.refreshByID(context.page);
    }

    @Override
    public void updateSection(Vec3d offset) {
        this.section = new Rectangle((int) offset.x - 2, (int) offset.y - 1,
                                     (int) ((this.maxLength + 10) * IRPConfig.ManualFontSize),
                                     (int) (12 * IRPConfig.ManualFontSize));
    }

    @Override
    public void renderTooltip(Identifier id, int bottomBound) {

    }
}
