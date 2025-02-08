package com.goldenfield192.irpatches.text;

import cam72cam.immersiverailroading.util.CAML;
import cam72cam.immersiverailroading.util.JSON;
import cam72cam.mod.resource.Identifier;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

@Deprecated
public class TranslationManager {
    public static HashMap<String, HashMap<String, String>> translationKeys = new HashMap<>();
    /*                    lang            key     value
     * formatting:
     * Label:
     * modelerName(exact "modelName" in json).stockName(exact "name" in json).label.labelWord(almost exact but replace "^" with " ")
     * Stock Name
     * modelerName(exact "modelName" in json).stockName(exact "name" in json)
     */
    public static void invoke() throws IOException {
        Identifier json = new Identifier("immersiverailroading", "lang/customtrans.json");
        json.getResourceStreamAll().stream()
                .map(stream -> {
                    try {
                        return JSON.parse(stream);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .forEach(block -> {
                    block.getBlockMap().forEach((s, dataBlock) -> {
                        HashMap<String, String> map = translationKeys.getOrDefault(s, new HashMap<>());
                        dataBlock.getValueMap().forEach((s1, value) -> map.put(s1, value.asString()));
                        translationKeys.put(s, map);
                    });
                });
        Identifier caml = new Identifier("immersiverailroading", "lang/customtrans.caml");
        caml.getResourceStreamAll().stream()
                .map(stream -> {
                    try {
                        return CAML.parse(stream);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .forEach(block -> {
                    block.getBlockMap().forEach((s, dataBlock) -> {
                        HashMap<String, String> map = translationKeys.getOrDefault(s, new HashMap<>());
                        dataBlock.getValueMap().forEach((s1, value) -> map.put(s1, value.asString()));
                        translationKeys.put(s, map);
                    });
                });

        System.out.println("TE");
        System.out.println(Arrays.toString(translationKeys.keySet().toArray()));
    }
}
