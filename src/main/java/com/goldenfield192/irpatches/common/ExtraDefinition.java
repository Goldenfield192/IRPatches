package com.goldenfield192.irpatches.common;

import cam72cam.immersiverailroading.Config;
import cam72cam.immersiverailroading.registry.EntityRollingStockDefinition;
import cam72cam.immersiverailroading.util.DataBlock;
import cam72cam.mod.resource.Identifier;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExtraDefinition {
    private static final HashMap<String, ExtraDefinition> extraDef = new HashMap<>();

    public String name;
    public String modelerName;
    public String packName;
    public int leftFirstMultiplier;
    public List<Pair<String, Integer>> burnables;
    public Identifier description;

    public static void loadExtraStockProperties(String defID, DataBlock data){
        DataBlock properties = data.getBlock("properties");
        ExtraDefinition def = new ExtraDefinition();
        def.leftFirstMultiplier = properties.getValue("left_first").asBoolean(true) ? 1 : -1;

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
            def.burnables = burnable;
        } else {
            def.burnables = null;
        }

        def.name = data.getValue("name").asString();
        def.modelerName = data.getValue("modeler").asString();
        def.packName = data.getValue("pack").asString();
        if(data.getValue("description") != null){
            def.description = data.getValue("description").asIdentifier();
        } else {
            def.description = null;
        }
        extraDef.put(defID, def);
    }

    public static ExtraDefinition getExtra(EntityRollingStockDefinition definition){
        return getExtra(definition.defID);
    }

    public static ExtraDefinition getExtra(String str) {
        return extraDef.get(str);
    }
}
