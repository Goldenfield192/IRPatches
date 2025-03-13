package com.goldenfield192.irpatches.document.markdown.element;

import cam72cam.immersiverailroading.ConfigGraphics;
import cam72cam.immersiverailroading.IRItems;
import com.goldenfield192.irpatches.common.IRPConfig;
import com.goldenfield192.irpatches.document.markdown.MarkdownDocument;
import cam72cam.immersiverailroading.items.ItemRollingStock;
import cam72cam.immersiverailroading.items.ItemTabs;
import cam72cam.immersiverailroading.registry.EntityRollingStockDefinition;
import cam72cam.mod.gui.helpers.GUIHelpers;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.resource.Identifier;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MarkdownItemRenderer extends MarkdownClickableElement{
    public ItemStack stack;

    private static final List<ItemStack> ITEM_STOCKS;

    static {
        ITEM_STOCKS = new ArrayList<>();

        ITEM_STOCKS.addAll(IRItems.ITEM_ROLLING_STOCK.getItemVariants(ItemTabs.LOCOMOTIVE_TAB));
        ITEM_STOCKS.addAll(IRItems.ITEM_ROLLING_STOCK.getItemVariants(ItemTabs.PASSENGER_TAB));
        ITEM_STOCKS.addAll(IRItems.ITEM_ROLLING_STOCK.getItemVariants(ItemTabs.STOCK_TAB));
    }

    public MarkdownItemRenderer(EntityRollingStockDefinition def){
        Optional<ItemStack> item =
                ITEM_STOCKS.stream().filter(stack -> new ItemRollingStock.Data(stack).def.equals(def)).findFirst();
        if(item.isPresent()){
            this.stack = item.get();
        } else {
            throw new IllegalStateException("Can't find definition!");
        }
    }

    public MarkdownItemRenderer(ItemStack stack) {
        this.stack = stack;
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
        RenderState state1 = state.clone();
        state1.scale(5,5,5);
        GUIHelpers.drawItem(stack, 0, 0, state1.model_view());
        state.translate(0,80,0);
        return 80;
    }

    @Override
    public void click(MarkdownDocument context) {

    }

    @Override
    public void updateSection(Vec3d offset) {
        this.section = new Rectangle((int) offset.x, (int) offset.y,
                (int) (10 * IRPConfig.ManualFontSize),
                (int) (10 * IRPConfig.ManualFontSize));
    }

    @Override
    public void renderTooltip(Identifier id, int bottomBound) {

    }
}
