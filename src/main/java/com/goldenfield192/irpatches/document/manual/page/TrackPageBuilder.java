package com.goldenfield192.irpatches.document.manual.page;

import cam72cam.immersiverailroading.registry.DefinitionManager;
import cam72cam.immersiverailroading.registry.TrackDefinition;
import cam72cam.mod.resource.Identifier;
import com.goldenfield192.irpatches.document.manual.element.MDTrackRenderer;
import com.goldenfield192.irpatches.document.core.IPageBuilder;
import com.goldenfield192.irpatches.document.core.MarkdownDocument;
import com.goldenfield192.irpatches.document.core.element.MarkdownStyledText;

public class TrackPageBuilder implements IPageBuilder {
    public static final IPageBuilder INSTANCE = new TrackPageBuilder();

    @Override
    public MarkdownDocument build(Identifier id) {
        MarkdownDocument document = new MarkdownDocument(id);
        TrackDefinition def = DefinitionManager.getTrack("immersiverailroading:track/" + id.getPath());
        document.addLine(new MDTrackRenderer(def))
                .addLine(new MarkdownStyledText("Name: " + def.name))
                .addLine(new MarkdownStyledText("Modeler: " + def.modelerName))
                .addLine(new MarkdownStyledText("Pack: " + def.packName));
        return document;
    }

    @Override
    public boolean validatePath(Identifier id) {
        return id.getDomain().equals("irtrack");
    }

    @Override
    public String getPageTooltipName(Identifier id) {
        if (validatePath(id)) {
            return id.getPath();
        }
        return "";
    }
}
