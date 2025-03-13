package com.goldenfield192.irpatches.document.markdown.element;

import cam72cam.mod.gui.helpers.GUIHelpers;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.render.opengl.RenderState;
import com.goldenfield192.irpatches.common.ManualGUIHelper;
import com.goldenfield192.irpatches.common.IRPConfig;
import com.goldenfield192.irpatches.document.markdown.BufferReaderAdapter;
import com.goldenfield192.irpatches.document.markdown.DefaultPageBuilder;
import com.goldenfield192.irpatches.document.markdown.MarkdownDocument;

import java.util.Iterator;

import static com.goldenfield192.irpatches.document.markdown.Colors.CODE_BACKGROUND_COLOR;
import static com.goldenfield192.irpatches.document.markdown.Colors.DEFAULT_TEXT_COLOR;

/**
 * Proxy class to simplify DefaultPageBuilder's build logic and MarkdownDocument's render logic
 * @see DefaultPageBuilder
 * @see MarkdownDocument
 */
public class MarkdownCodeBlock {
    /**
     * Parse code block into given MarkdownDocument
     * @param reader Markdown file's reader
     * @param document Given document
     * @param firstLine The line contains beginning syntax
     */
    public static void parse(BufferReaderAdapter reader, MarkdownDocument document, String firstLine){
        if(firstLine.length() > 3){
            //Meaning it has language mark
            document.addLine(MarkdownDocument.MarkdownLine.create(new MarkdownStyledText(firstLine.substring(3)))
                    .isCodeBlockStart(true));
        } else {
            //Language is empty
            document.addLine(MarkdownDocument.MarkdownLine.create(new MarkdownStyledText(""))
                    .isCodeBlockStart(true));
        }
        String str;
        while ((str = reader.readLine()) != null){
            if(str.startsWith("```")){
                document.addLine(MarkdownDocument.MarkdownLine.create(new MarkdownStyledText("")).isCodeBlockEnd(true));
                return;
            }
            document.addLine(DefaultPageBuilder.parse(str));
        }
        document.addLine(MarkdownDocument.MarkdownLine.create(new MarkdownStyledText("")).isCodeBlockEnd(true));
    }

    /**
     * Render given document's code block element
     * @param state Gui RenderState
     * @param iterator Lines iterator
     * @param document Source document
     * @param currentLine Current iterated line which can't be gotten by iterator.next()
     * @return The code block's height for further use
     */
    public static int render(RenderState state, Iterator<MarkdownDocument.MarkdownLine> iterator,
                             MarkdownDocument document, MarkdownDocument.MarkdownLine currentLine){
        Vec3d offset = state.model_view().apply(Vec3d.ZERO);
        int height = 0;
        //Draw header line
        //Code blocks have a gray background and start with a language specification mark
        GUIHelpers.drawRect((int) offset.x, (int) offset.y,
                document.getPageWidth(), (int) (10 * IRPConfig.ManualFontSize), CODE_BACKGROUND_COLOR);
        int delta = (int) ((document.getPageWidth() - ManualGUIHelper.getTextWidth(currentLine.getElements().get(0).apply())* IRPConfig.ManualFontSize)
                / IRPConfig.ManualFontSize);
        state.translate(delta, 0, 0);
        ManualGUIHelper.drawString(currentLine.getElements().get(0).apply(), 0, 0, DEFAULT_TEXT_COLOR, state.model_view());
        state.translate(-delta, 10, 0);
        height += 10;

        while (iterator.hasNext()){
            MarkdownDocument.MarkdownLine line = iterator.next();
            offset = state.model_view().apply(Vec3d.ZERO);
            if(line.codeBlockEnd){
                //Draw footer line
                GUIHelpers.drawRect((int) offset.x, (int) offset.y,
                        document.getPageWidth(), (int) (5 * IRPConfig.ManualFontSize), CODE_BACKGROUND_COLOR);
                state.translate(0, 5, 0);
                height += 5;
                return height;
            }
            //Otherwise draw content
            GUIHelpers.drawRect((int) offset.x , (int) offset.y ,
                    document.getPageWidth(), (int) (10 * IRPConfig.ManualFontSize), CODE_BACKGROUND_COLOR);
            ManualGUIHelper.drawString(line.getElements().get(0).apply(), 0, 0, DEFAULT_TEXT_COLOR, state.model_view());
            state.translate(0, 10, 0);
            height += 10;
        }

        //Why does a file ends and leaves a code block open? This should never happen!
        throw new IllegalStateException();
    }
}
