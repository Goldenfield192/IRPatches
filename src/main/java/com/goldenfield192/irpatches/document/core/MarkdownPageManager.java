package com.goldenfield192.irpatches.document.core;

import cam72cam.mod.resource.Identifier;
import com.goldenfield192.irpatches.document.ManualGui;
import com.goldenfield192.irpatches.document.manual.page.ItemComponentPageBuilder;
import com.goldenfield192.irpatches.document.manual.page.StockDescriptionPageBuilder;
import com.goldenfield192.irpatches.document.manual.page.TrackPageBuilder;

import java.util.HashMap;
import java.util.Map;

public class MarkdownPageManager {
    private static final Map<String, IPageBuilder> BUILDERS = new HashMap<>();
    private static final Map<String, Map<Identifier, MarkdownDocument>> CUSTOM_PAGES = new HashMap<>();

    static {
        registerPageBuilder("irstock", StockDescriptionPageBuilder.INSTANCE);
        registerPageBuilder("iritem", ItemComponentPageBuilder.INSTANCE);
        registerPageBuilder("irtrack", TrackPageBuilder.INSTANCE);
        registerPageBuilder("immersiverailroading", DefaultPageBuilder.INSTANCE);
    }

    public static void registerPageBuilder(String domain, IPageBuilder builder) {
        BUILDERS.put(domain, builder);
        CUSTOM_PAGES.put(domain, new HashMap<>());
    }

    /**
     * Try to get a cached page
     *
     * @param id The page's content location
     * @return The cached page or a new page if not present
     */
    public static synchronized MarkdownDocument getOrComputePageByID(Identifier id, int screenWidth) {
        MarkdownDocument document;
        if (BUILDERS.containsKey(id.getDomain())) {
            IPageBuilder builder = BUILDERS.get(id.getDomain());
            document = CUSTOM_PAGES.get(id.getDomain()).computeIfAbsent(id, identifier -> builder.build(id));
        } else {
            throw new IllegalArgumentException();
        }
        document.setPageWidth(screenWidth);
        return MarkdownLineBreaker.breakDocument(document, screenWidth);
    }

    /**
     * Try to get a cached page
     *
     * @param id The page's content location
     * @return The cached page or null if not present
     */
    public static synchronized MarkdownDocument getPageByID(Identifier id) {
        MarkdownDocument document;
        if (BUILDERS.containsKey(id.getDomain())) {
            document = CUSTOM_PAGES.get(id.getDomain()).get(id);
        } else {
            throw new IllegalArgumentException();
        }
        return document;
    }

    /**
     * API method for dynamic generated content
     *
     * @param id The cached page need to be cleared
     */
    public static synchronized void refreshByID(Identifier id) {
        if (BUILDERS.containsKey(id.getDomain())) {
            IPageBuilder builder = BUILDERS.get(id.getDomain());
            CUSTOM_PAGES.get(id.getDomain()).computeIfPresent(id, (ident, document) -> builder.build(ident));
            ManualGui.refresh();
        }
    }

    public static String getPageName(Identifier id) {
        if (BUILDERS.containsKey(id.getDomain())) {
            return BUILDERS.get(id.getDomain()).getPageTooltipName(id);
        }
        return "";
    }

    public static boolean validate(Identifier id) {
        return BUILDERS.containsKey(id.getDomain()) && BUILDERS.get(id.getDomain()).validatePath(id);
    }
}
