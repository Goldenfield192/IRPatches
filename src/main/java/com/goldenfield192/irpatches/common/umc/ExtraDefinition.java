package com.goldenfield192.irpatches.common.umc;

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
    public final HashMap<String, LightDefinition> extraLightDef = new HashMap<>();

    public static void loadExtraStockProperties(String defID, DataBlock data){
        ExtraDefinition def = new ExtraDefinition();
        DataBlock properties = data.getBlock("properties");
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

        DataBlock lights = data.getBlock("lights");
        if (lights != null) {
            lights.getBlockMap().forEach((key, block) -> {
                def.extraLightDef.put(key, new LightDefinition(block));
            });
        }

        extraDef.put(defID, def);
    }

    public static ExtraDefinition get(EntityRollingStockDefinition definition){
        return get(definition.defID);
    }

    public static ExtraDefinition get(String defID) {
        return extraDef.get(defID);
    }

    public static class LightDefinition {
        public final boolean enableTex;

        public LightDefinition(DataBlock block) {
            this.enableTex = block.getValue("enableTexture").asBoolean(true);
        }
    }
}
