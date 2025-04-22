package com.goldenfield192.irpatches.document.manual.element;

import cam72cam.immersiverailroading.model.TrackModel;
import cam72cam.immersiverailroading.registry.DefinitionManager;
import cam72cam.immersiverailroading.registry.TrackDefinition;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.render.opengl.RenderState;
import com.goldenfield192.irpatches.IRPConfig;
import com.goldenfield192.irpatches.document.core.element.AbstractMarkdownElement;

import static cam72cam.immersiverailroading.library.Gauge.STANDARD;

public class MDTrackRenderer extends AbstractMarkdownElement {
    private final StandardModel model;

    public MDTrackRenderer(String trackID) {
        this(DefinitionManager.getTrack(trackID));
    }

    public MDTrackRenderer(TrackDefinition def) {
        model = new StandardModel().addCustom((state, partialTicks) -> {
            TrackModel trackModel = def.getTrackForGauge(STANDARD);
            RenderState state1 = state.clone();
            try (OBJRender.Binding binding = trackModel.binder().bind(state1)) {
                binding.draw();
            }
            try (OBJRender.Binding binding = trackModel.binder().bind(state1.translate(1, 0, 0))) {
                binding.draw();
            }
            try (OBJRender.Binding binding = trackModel.binder().bind(state1.translate(-2, 0, 0))) {
                binding.draw();
            }
        });
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
        RenderState state1 = state.clone();
        double scale = pageWidth / 4.0 / IRPConfig.ManualFontSize;
        state1.translate(scale * 2, scale, 100);
        state1.rotate(180, 0, 0, 1);
        state1.rotate(30, 1, 0, 0);
        state1.rotate(30, 0, 1, 0);
        state1.scale(scale, scale, scale);
        state1.lighting(true);
        state1.lightmap(1, 1);
        model.render(state1);
        state.translate(0, scale * 2, 0);
        return (int) (scale * 2 / IRPConfig.ManualFontSize);
    }
}
