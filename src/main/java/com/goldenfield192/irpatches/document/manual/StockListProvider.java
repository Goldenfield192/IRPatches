package com.goldenfield192.irpatches.document.manual;

import com.goldenfield192.irpatches.common.ExtraDefinitionManager;
import com.goldenfield192.irpatches.document.markdown.MarkdownDocument;
import com.goldenfield192.irpatches.document.markdown.element.MarkdownTitle;
import com.goldenfield192.irpatches.document.markdown.element.MarkdownUrl;
import cam72cam.immersiverailroading.registry.DefinitionManager;
import cam72cam.immersiverailroading.registry.EntityRollingStockDefinition;
import cam72cam.mod.ModCore;
import cam72cam.mod.resource.Identifier;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class StockListProvider {
    public static final String SYNTAX = "[list_stock_page]";

    public static List<MarkdownDocument.MarkdownLine> parse(String input, MarkdownDocument context){
        List<MutablePair<String, EntityRollingStockDefinition>> definitions =
                DefinitionManager.getDefinitions().stream()
                .map(def -> {
                    String name = (String) ExtraDefinitionManager.getExtra(def.defID).get("name");
                    String modelerName = (String) ExtraDefinitionManager.getExtra(def.defID).get("modeler");
                    String packName = (String) ExtraDefinitionManager.getExtra(def.defID).get("pack");
                    switch (context.getProperty("stock")){
                        case 0:
                            return MutablePair.of("N/A".equals(name) ? "Unknown" : name, def);
                        case 1:
                            return MutablePair.of("N/A".equals(modelerName) ? "Unknown" : modelerName, def);
                        case 2:
                        default:
                            return MutablePair.of("N/A".equals(packName) ? "Unknown" : packName, def);
                    }
                })
                .sorted(Comparator.comparing(MutablePair::getLeft))
                .collect(Collectors.toList());
        List<MarkdownDocument.MarkdownLine> lines = new LinkedList<>();
        Character lastStartingChar = null;
        String lastFullName = null;
        for (MutablePair<String, EntityRollingStockDefinition> definition : definitions) {
            if(lastStartingChar == null || lastStartingChar != definition.getLeft().charAt(0)){
                lastStartingChar = definition.getLeft().charAt(0);
                ModCore.info(definition.getLeft());
                lines.add(MarkdownDocument.MarkdownLine.create(new MarkdownTitle(lastStartingChar.toString(), 1)));
            }
            if(context.getProperty("stock") != 0 && (lastFullName == null || !lastFullName.equals(definition.getLeft()))){
                lastFullName = definition.getLeft();
                lines.add(MarkdownDocument.MarkdownLine.create(new MarkdownTitle(lastFullName, 2)));
            }
            lines.add(MarkdownDocument.MarkdownLine.create(new MarkdownUrl((String) ExtraDefinitionManager.getExtra(definition.getRight().defID).get("name"), new Identifier("irstock", definition.getRight().defID))));
        }
        return lines;
    }
}
