package com.goldenfield192.irpatches.common;

import cam72cam.immersiverailroading.util.DataBlock;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ExtraDefinitionManager {
    public static final HashMap<String, HashMap<String, Object>> stockDef = new HashMap<>();

    public static void loadExtraStockProperties(String defID, DataBlock data){
        DataBlock properties = data.getBlock("properties");
        stockDef.put(defID, new HashMap<>());
        stockDef.get(defID).put("leftFirstMultiplier", properties.getValue("left_first").asBoolean(true) ? 1 : -1);
        List<DataBlock.Value> list = properties.getValues("fuel");
        if(list != null){
            stockDef.get(defID).put("fuel", list.stream()
                                                .map(DataBlock.Value::asString)
                                                .collect(Collectors.toList()));
        } else {
            stockDef.get(defID).put("fuel", null);
        }
    }
}
