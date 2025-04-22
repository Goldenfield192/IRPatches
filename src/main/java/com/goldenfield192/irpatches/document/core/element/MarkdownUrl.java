package com.goldenfield192.irpatches.document.core.element;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.resource.Identifier;
import cam72cam.mod.text.PlayerMessage;
import cam72cam.mod.text.TextColor;
import com.goldenfield192.irpatches.gui.IRPGUIHelper;
import com.goldenfield192.irpatches.IRPConfig;
import com.goldenfield192.irpatches.document.ManualGui;
import com.goldenfield192.irpatches.document.core.MarkdownDocument;
import com.goldenfield192.irpatches.document.core.MarkdownPageManager;
import net.minecraft.util.ResourceLocationException;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.goldenfield192.irpatches.document.core.Colors.DEFAULT_TEXT_COLOR;

/**
 * Element class representing a url, which is clickable
 * <p>
 * Also parses Markdown format url
 *
 * @see MarkdownClickableElement
 * @see AbstractMarkdownElement
 */
public class MarkdownUrl extends MarkdownClickableElement {
    //text may be empty, while url mustn't be empty
    public static final Pattern MARKDOWN_URL_PATTERN = Pattern.compile("\\[(?<text>.*?)]\\((?<url>\\S+?)\\)");

    public Identifier destination;

    //1.16 compat
    //TODO Need research
    private boolean isLink = false;

    public MarkdownUrl(String text, String destination) {
        this.text = text;
        try {
            this.destination = new Identifier(destination);
        } catch (ResourceLocationException e){
            this.isLink = true;
            this.text = destination;
        }
    }

    public MarkdownUrl(String text, Identifier destination) {
        this.text = text;
        this.destination = destination;
    }

    /**
     * Helper method to parse a String into MarkdownUrl element
     *
     * @param input Raw String needed to be parsed
     * @return The parsed element, or null if it can't be parsed
     */
    public static MarkdownUrl compileSingle(String input) {
        Matcher matcher = MARKDOWN_URL_PATTERN.matcher(input);
        if (matcher.find()) {
            return new MarkdownUrl(matcher.group("text"), matcher.group("url"));
        } else {
            return null;
        }
    }

    /**
     * Helper method to split a text element into a list by urls
     *
     * @param input Raw String needed to be parsed
     * @return The parsed element, or null if it can't be parsed
     */
    public static List<AbstractMarkdownElement> splitLineByUrl(MarkdownStyledText input) {
        List<AbstractMarkdownElement> urls = new ArrayList<>();

        Matcher matcher = MARKDOWN_URL_PATTERN.matcher(input.text);
        int prev = 0;
        while (matcher.find()) {
            urls.add(new MarkdownStyledText(input.text.substring(prev, matcher.start("text") - 1), input.styles));
            urls.add(new MarkdownUrl(matcher.group("text"),
                                     matcher.group("url")));
            prev = matcher.end("url") + 1;
        }
        //Last element is not a url, finalize as ordinary text
        if (prev != input.text.length() - 1) {
            urls.add(new MarkdownStyledText(input.text.substring(prev), input.styles));
        }
        return urls;
    }

    @Override
    public String apply() {
        return TextColor.BLUE.wrap(TextColor.UNDERLINE.wrap(text));
    }

    @Override
    public AbstractMarkdownElement[] split(int splitPos) {
        int i = splitPos;
        while (this.text.charAt(i) == ' ') {
            i++;
            if (i == this.text.length()) {//rest are all space
                return new AbstractMarkdownElement[]{
                        new MarkdownUrl(this.text.substring(0, splitPos), this.destination),
                        new MarkdownUrl("", this.destination)};
            }
        }
        return new AbstractMarkdownElement[]{
                new MarkdownUrl(this.text.substring(0, splitPos), this.destination),
                new MarkdownUrl(this.text.substring(i), this.destination)};
    }

    @Override
    public int render(RenderState state, int pageWidth) {
        String str = this.apply();
        IRPGUIHelper.drawString(str, 0, 0, DEFAULT_TEXT_COLOR, state.model_view());
        state.translate(IRPGUIHelper.getTextWidth(str), 0, 0);
        return 0;
    }

    @Override
    public void click(MarkdownDocument document) {
        if (MarkdownPageManager.validate(this.destination)) {
            ManualGui.pushContent(this.destination);
        } else if (isLink || this.destination.getDomain().equals("https")) {
            MinecraftClient.getPlayer().sendMessage(PlayerMessage.url(this.destination.toString()));
        } else {
            //What should we do?
        }
    }

    @Override
    public void updateSection(Vec3d offset) {
        this.section = new Rectangle((int) offset.x, (int) offset.y,
                                     (int) (IRPGUIHelper.getTextWidth(this.apply()) * IRPConfig.ManualFontSize),
                                     (int) (10 * IRPConfig.ManualFontSize));
    }

    //TODO Translation file
    @Override
    public void renderTooltip(Identifier id, int bottomBound) {
         if (isLink || this.destination.getDomain().equals("https")) {
            renderTooltip("Click to send this website to your dialog!", bottomBound);
        } else if (MarkdownPageManager.getPageName(id) != null) {
            renderTooltip("Open page: " + MarkdownPageManager.getPageName(destination), bottomBound);
        } else {
            //What should we do?
        }
    }
}
