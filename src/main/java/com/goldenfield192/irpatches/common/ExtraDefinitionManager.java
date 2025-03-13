package com.goldenfield192.irpatches.common;

import cam72cam.immersiverailroading.Config;
import cam72cam.immersiverailroading.util.DataBlock;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExtraDefinitionManager {
    public static final HashMap<String, HashMap<String, Object>> stockDef = new HashMap<>();

    public static void loadExtraStockProperties(String defID, DataBlock data){
        DataBlock properties = data.getBlock("properties");
        HashMap<String, Object> extra = new HashMap<>();

        extra.put("leftFirstMultiplier", properties.getValue("left_first").asBoolean(true) ? 1 : -1);

        List<DataBlock.Value> list = properties.getValues("fuel");
        if(list != null){
            List<Pair<String, Integer>> burnable = new ArrayList<>();
            for(DataBlock.Value value : list) {
                String s = value.asString("");
                if(s.contains(":")){
                    String[] split = s.split(":");
                    burnable.add(Pair.of(split[0], Integer.valueOf(split[1])));
                } else if(Config.ConfigBalance.dieselFuels.containsKey(s)){
                    burnable.add(Pair.of(s, Config.ConfigBalance.dieselFuels.get(s)));
                }
            }
            extra.put("fuel", burnable);
        } else {
            extra.put("fuel", null);
        }

        extra.put("name", data.getValue("name").asString());
        extra.put("modeler", data.getValue("modeler").asString());
        extra.put("pack", data.getValue("pack").asString());
        if(data.getValue("description") != null){
            extra.put("description", data.getValue("description").asIdentifier());
        } else {
            extra.put("description", null);
        }
        stockDef.put(defID, extra);
    }

    public static HashMap<String, Object> getExtra(String defId){
        return stockDef.get(defId);
    }
}
