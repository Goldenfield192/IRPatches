package com.goldenfield192.irpatches.util;

import cam72cam.immersiverailroading.model.TrackModel;
import cam72cam.immersiverailroading.registry.TrackDefinition;
import cam72cam.immersiverailroading.util.DataBlock;
import com.goldenfield192.irpatches.accessor.ITrackModelAccessor;
import org.apache.commons.lang3.tuple.Pair;
import trackapi.lib.Gauges;

import java.util.*;

public class ExtraTrackDefinition {
    private static final HashMap<String, ExtraTrackDefinition> extraDef = new HashMap<>();

    private List<ExtraTrackModel> model;

    public static void load(String defID, DataBlock dataBlock){
        double model_gauge_m = dataBlock.getValue("model_gauge_m").asDouble(Gauges.STANDARD);
        double spacing = dataBlock.getValue("model_spacing_m").asDouble( model_gauge_m / Gauges.STANDARD);

        DataBlock models = dataBlock.getBlock("models");
        Map<String, DataBlock> multiTracks = models.getBlockMap();
        if(multiTracks.isEmpty()){
            return;
        }
        ExtraTrackDefinition def = new ExtraTrackDefinition();
        def.model = new ArrayList<>();
        multiTracks.forEach((s, dataBlock1) -> {
            ExtraTrackModel model1 = new ExtraTrackModel(s);
            for (Map.Entry<String, DataBlock.Value> entry : dataBlock1.getValueMap().entrySet()) {
                try {
                    model1.addTrack(new TrackModel(s, entry.getValue().asIdentifier(), model_gauge_m, spacing),
                                    Integer.parseInt(entry.getKey()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            def.model.add(model1);
        });
        extraDef.put(defID, def);
    }

    public static ExtraTrackDefinition getExtraDef(TrackDefinition definition) {
        return getExtraDef(definition.trackID);
    }

    public static ExtraTrackDefinition getExtraDef(String defID) {
        return extraDef.get(defID);
    }

    public ExtraTrackModel getFirst(double gauge){
        for(ExtraTrackModel model1 : model){
            if(model1.surface.canRender(gauge)){
                return model1;
            }
        }
        return null;
    }

    public static class ExtraTrackModel{
        public TrackModel surface;

        private final String condition;
        private final List<Integer> numList;
        private final Map<Integer, TrackModel> refer;
        private Integer totalNumber = 0;

        public ExtraTrackModel(String con) {
            this.numList = new ArrayList<>();
            this.refer = new HashMap<>();
            this.condition = con;
        }

        public void addTrack(TrackModel model, int possibility){
            if(surface == null){
                surface = model;
                ((ITrackModelAccessor)surface).setExtra(this);
            }

            int thisNumber = refer.size();
            refer.put(thisNumber, model);

            int min = totalNumber;
            int max = totalNumber + possibility;
            totalNumber += possibility;
            for(int i = min; i < max; i++){
                numList.add(thisNumber);
            }
            System.out.println("numList" + Arrays.toString(numList.toArray()));
        }

        public Integer getTotalNumber() {
            return totalNumber;
        }

        public Pair<Integer, TrackModel> getModelForNumber(int num){
            return Pair.of(numList.get(num), refer.get(numList.get(num)));
        }

        public Map<Integer, TrackModel> getRefer() {
            return refer;
        }
    }
}
