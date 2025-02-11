package com.goldenfield192.irpatches.utils;

import cam72cam.immersiverailroading.util.DataBlock;

import java.util.HashMap;

public class ExtraDefinitionManager {
    public static final HashMap<String, HashMap<String, Object>> stockDef = new HashMap<>();

    public static void loadExtraStockProperties(String defID, DataBlock data){
        DataBlock properties = data.getBlock("properties");
        stockDef.put(defID, new HashMap<>());
        stockDef.get(defID).put("leftFirstMultiplier", properties.getValue("left_first").asBoolean(true) ? 1 : -1);
    }
}
