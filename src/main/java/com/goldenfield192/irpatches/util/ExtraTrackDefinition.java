package com.goldenfield192.irpatches.util;

import cam72cam.immersiverailroading.model.TrackModel;
import cam72cam.immersiverailroading.registry.TrackDefinition;
import cam72cam.immersiverailroading.util.DataBlock;
import com.goldenfield192.irpatches.accessor.ITrackModelAccessor;
import org.apache.commons.lang3.tuple.Pair;
import trackapi.lib.Gauges;

import java.util.*;
import java.util.stream.Collectors;

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
            ExtraTrackModel model1 = new ExtraTrackModel();
//            if(dataBlock1.getValues("order") != null){
//                List<String> orderList = dataBlock1.getValues("order").stream()
//                                                   .map(DataBlock.Value::asString)
//                                                   .collect(Collectors.toList());
                //Parse order
//                Map<String, Integer> refers = new HashMap<>();
//                for (Map.Entry<String, DataBlock.Value> entry : dataBlock1.getValueMap().entrySet()) {
//                    try {
//                        model1.addTrack(new TrackModel(s, entry.getValue().asIdentifier(), model_gauge_m, spacing), 1);
//                        refers.put(entry.getKey(), model1.getRefer().size() - 1);
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//                int[] map = new int[orderList.size()];
//                for(int i = 0; i < orderList.size(); i++){
//                    String str = orderList.get(i);
//                    map[i] = refers.get(str);
//                }

//                model1.setOrder(map);
//            } else {
                //Otherwise it is random
                for (Map.Entry<String, DataBlock.Value> entry : dataBlock1.getValueMap().entrySet()) {
                    try {
                        model1.addTrack(new TrackModel(s, entry.getValue().asIdentifier(), model_gauge_m, spacing),
                                        Integer.parseInt(entry.getKey()));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
//            }
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

        private final List<Integer> numMappingList;
        private final List<TrackModel> refer;
        private Integer totalNumber = 0;

        private boolean isOrdered;
        private int[] order;

        public ExtraTrackModel() {
            this.numMappingList = new ArrayList<>();
            this.refer = new ArrayList<>();
        }

        public void addTrack(TrackModel model, int possibility){
            if(surface == null){
                surface = model;
                ((ITrackModelAccessor)surface).setExtra(this);
            }

            int selfIndex = refer.size();
            refer.add(model);

            int min = totalNumber;
            int max = totalNumber + possibility;
            totalNumber += possibility;
            for(int i = min; i < max; i++){
                numMappingList.add(selfIndex);
            }
        }

        public void setOrder(int[] order) {
            this.isOrdered = true;
            this.order = order;
        }

        public boolean isOrdered() {
            return isOrdered;
        }

        public int getOrderedIndex(int idx){
            return order[idx % order.length];
        }

        public Integer getSummedWeight() {
            return totalNumber;
        }

        public Pair<Integer, TrackModel> getModelForRandom(int idx){
            return Pair.of(numMappingList.get(idx), refer.get(numMappingList.get(idx)));
        }

        public List<TrackModel> getRefer() {
            return refer;
        }
    }
}
