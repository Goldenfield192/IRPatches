package com.goldenfield192.irpatches.document.manual;

import cam72cam.immersiverailroading.registry.DefinitionManager;
import cam72cam.mod.resource.Identifier;
import com.goldenfield192.irpatches.document.core.MarkdownDocument;
import com.goldenfield192.irpatches.document.core.element.MarkdownUrl;

import java.util.List;
import java.util.stream.Collectors;

public class TrackProvider {
    public static final String SYNTAX = "[track_provider]";

    public static List<MarkdownDocument.MarkdownLine> parse(String input, MarkdownDocument context) {
        return DefinitionManager.getTracks().stream()
                                .map(def -> MarkdownDocument.MarkdownLine.create(new MarkdownUrl(def.name,
                                                                                                 new Identifier(
                                                                                                         "irtrack",
                                                                                                         def.trackID.split(
                                                                                                                 "/")[1]))))
                                .collect(Collectors.toList());
    }
}
