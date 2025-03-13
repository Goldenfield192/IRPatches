package com.goldenfield192.irpatches.document;

import cam72cam.immersiverailroading.ImmersiveRailroading;
import cam72cam.mod.event.ClientEvents;
import cam72cam.mod.gui.helpers.GUIHelpers;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.resource.Identifier;
import com.goldenfield192.irpatches.common.IRPConfig;
import com.goldenfield192.irpatches.document.manual.ClippedRenderer;
import com.goldenfield192.irpatches.document.markdown.MarkdownDocument;
import com.goldenfield192.irpatches.document.markdown.MarkdownPageManager;
import com.goldenfield192.irpatches.document.markdown.element.MarkdownUrl;
import org.apache.commons.lang3.tuple.MutablePair;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Stack;

import static com.goldenfield192.irpatches.document.markdown.Colors.BUTTON_DISABLED_COLOR;

public class ManualGui implements IScreen {
    private static ManualGui currentOpeningManual;
    //                                        page     page's mainOffset
    private static final Stack<MutablePair<Identifier, Double>> historyPageStack = new Stack<>();
    private static final Stack<MutablePair<Identifier, Double>> futurePageStack = new Stack<>();
    private static Rectangle2D prevPageButton;
    private static Rectangle2D nextPageButton;

    private int width;
    private int height;
    private MarkdownDocument sidebar;
    private MarkdownDocument content;
    private Identifier lastPage;

    static {
        historyPageStack.push(MutablePair.of(new Identifier("immersiverailroading:wiki/en_us/home.md"), 0d));
    }

    //Will be called every time the screen scale changes
    //So there's no need to update line break manually
    @Override
    public void init(IScreenBuilder screen) {
        currentOpeningManual = this;
        prevPageButton = new Rectangle(60, 15, 20, 20);
        nextPageButton = new Rectangle(120, 15, 20, 20);
        sidebar = MarkdownPageManager.getOrComputePageByID(new Identifier(ImmersiveRailroading.MODID, "wiki/en_us/_sidebar.md"), 100);
        sidebar.setScrollRegion(new Rectangle(50, 35, 120, screen.getHeight() - 50));
        content = MarkdownPageManager.getOrComputePageByID(historyPageStack.peek().getLeft(), screen.getWidth() - 240);
        content.setScrollRegion(new Rectangle(180,15,width - 220,height - 30));
    }

    @Override
    public void onEnterKey(IScreenBuilder builder) {

    }

    @Override
    public void onClose() {
        currentOpeningManual = null;
    }

    @Override
    public void draw(IScreenBuilder builder, RenderState state) {
        IScreen.super.draw(builder, state);
        width = builder.getWidth();
        height = builder.getHeight();

        if(lastPage != historyPageStack.peek().getLeft()){
            //Meaning that we should refresh it
            content = MarkdownPageManager.getOrComputePageByID(historyPageStack.peek().getLeft(), width - 240);
            content.setScrollRegion(new Rectangle(170,20,width-220,height-30));
            content.setVerticalOffset(historyPageStack.peek().getValue());
            lastPage = historyPageStack.peek().getLeft();
        }

        //Background
        GUIHelpers.texturedRect(new Identifier("immersiverailroading:gui/wiki/left_top_corner.png"), 50, 10,10,10);
        GUIHelpers.texturedRect(new Identifier("immersiverailroading:gui/wiki/top_edge.png"), 60, 10,width - 120,10);
        GUIHelpers.texturedRect(new Identifier("immersiverailroading:gui/wiki/right_top_corner.png"), width - 60, 10,10,10);
        GUIHelpers.texturedRect(new Identifier("immersiverailroading:gui/wiki/left_edge.png"), 50, 20,10,height - 40);
        GUIHelpers.texturedRect(new Identifier("immersiverailroading:gui/wiki/center.png"), 60, 20,width - 120,height - 40);
        GUIHelpers.texturedRect(new Identifier("immersiverailroading:gui/wiki/right_edge.png"), width - 60, 20,10,height - 40);
        GUIHelpers.texturedRect(new Identifier("immersiverailroading:gui/wiki/left_bottom_corner.png"), 50, height - 20,10,10);
        GUIHelpers.texturedRect(new Identifier("immersiverailroading:gui/wiki/bottom_edge.png"), 60, height - 20,width - 120,10);
        GUIHelpers.texturedRect(new Identifier("immersiverailroading:gui/wiki/right_bottom_corner.png"), width - 60, height - 20,10,10);

        //Markdown
        ClippedRenderer.renderInRegion(54, 35, 120, height - 50, () -> {
            sidebar.render(state.clone().translate(57, 40, 0).scale(IRPConfig.ManualFontSize, IRPConfig.ManualFontSize, IRPConfig.ManualFontSize));
        });
        ClippedRenderer.renderInRegion(175, 15, GUIHelpers.getScreenWidth() - 220, height - 30, () -> {
            content.render(state.clone().translate(180, 20, 0).scale(IRPConfig.ManualFontSize, IRPConfig.ManualFontSize, IRPConfig.ManualFontSize));
        });

        //Middle split line
        GUIHelpers.drawRect(170,15,2,height - 30, BUTTON_DISABLED_COLOR);

        //Header
        if(historyPageStack.size() != 1){
            GUIHelpers.texturedRect(new Identifier("immersiverailroading:gui/wiki/left.png"),
                    60, 15, 20, 20);
        }
        if(!futurePageStack.isEmpty()){
            GUIHelpers.texturedRect(new Identifier("immersiverailroading:gui/wiki/right.png"),
                    120, 15, 20, 20);
        }

        //Tooltip
        //Currently only MarkdownUrl inherits MarkdownClickableElement, need change when more types are added
        for(MarkdownDocument screen : new MarkdownDocument[]{sidebar, content}){
            if(screen.getHoveredElement() != null && screen.getHoveredElement() instanceof MarkdownUrl){
                MarkdownUrl clickable = (MarkdownUrl) screen.getHoveredElement();
                clickable.renderTooltip(screen.page, (int) screen.getScrollRegion().getMaxY());
                break;
            }
        }
    }

    public static void pushContent(Identifier identifier){
        pushContent(identifier, 0d);
    }

    public static void pushContent(Identifier identifier, double offset){
        if(currentOpeningManual == null){
            return;
        }

        if(!futurePageStack.isEmpty() && futurePageStack.peek().getLeft().equals(identifier)){
            historyPageStack.push(MutablePair.of(identifier, offset));
            futurePageStack.pop();
        }

        if(!historyPageStack.peek().getLeft().equals(identifier)){
            historyPageStack.push(MutablePair.of(identifier, offset));
            if(!futurePageStack.isEmpty() && identifier != futurePageStack.peek().getLeft()){
                futurePageStack.clear();
            }
        }
    }

    //Return true if the event is handled
    public static boolean onClick(ClientEvents.MouseGuiEvent event){
        if(currentOpeningManual == null){
            return false;
        }

        if(event.scroll != 0) {
            currentOpeningManual.sidebar.onScroll(event);
            currentOpeningManual.content.onScroll(event);
        }

        if(event.action == ClientEvents.MouseAction.RELEASE){
            if(prevPageButton.contains(event.x, event.y)){
                if(historyPageStack.size() > 1){
                    futurePageStack.push(historyPageStack.pop());
                }
                //Terminates unnecessary call of sidebar&content's onMouseRelease as the mouse's pos cannot inside them
                return true;
            } else if(nextPageButton.contains(event.x, event.y)){
                if(!futurePageStack.isEmpty()){
                    pushContent(futurePageStack.peek().getLeft(), futurePageStack.peek().getRight());
                }
                return true;
            }
            currentOpeningManual.sidebar.onMouseRelease(event);
            currentOpeningManual.content.onMouseRelease(event);
        }
        return true;
    }

    //For scroll
    public static void onClientTick(){
        if(currentOpeningManual == null){
            return;
        }

        currentOpeningManual.sidebar.handleScrollOnTicks();
        currentOpeningManual.content.handleScrollOnTicks();
        historyPageStack.peek().setValue(currentOpeningManual.content.getVerticalOffset());
    }
}
