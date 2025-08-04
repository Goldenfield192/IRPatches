package com.goldenfield192.irpatches;

import cam72cam.mod.config.ConfigFile;

import java.util.HashMap;
import java.util.Map;

@ConfigFile.Comment("Configuration File")
@ConfigFile.Name("general")
@ConfigFile.File("irp_general.cfg")
public class IRPConfig {
    @ConfigFile.Comment("Font size of manual")
    @ConfigFile.Range(min = 1, max = 2)
    public static float ManualFontSize = 1;

    @ConfigFile.Comment("Max distance of on-stock third person view")
    @ConfigFile.Range(min = 10, max = 500)
    public static float ThirdPersonMaxDistance = 20;

    @ConfigFile.Comment("Max track length")
    @ConfigFile.Range(min = 1000, max = 10000)
    public static int MaxTrackLength = 1000;

    @ConfigFile.Comment("Track render distance")
    @ConfigFile.Range(min = 256, max = 8192)
    public static int TrackRenderDistance = 1000;

//    @ConfigFile.Comment("Does a track exchanger replace a single segment of a segmented track or the entire track?")
//    public static boolean TrackExchangerChangeEntireTrack = true;

    @ConfigFile.Comment("Enable IRP's onboard camera")
    public static boolean EnableAdvancedCamera = true;

    @ConfigFile.Comment("Does onboard camera collide with blocks?")
    public static boolean OnboardCameraCollideWithBlock = true;

    @ConfigFile.Comment("Enable IRP's redstone turntable")
    public static boolean IRPTurnTable = true;

    @ConfigFile.Comment("Blocks not to break")
    public static Map<String, Boolean> whiteList;

    public static void init() {
        if(whiteList == null || whiteList.isEmpty()){
            whiteList = new HashMap<>();
            whiteList.put("littletiles:blocklittletiles", true);
        }
    }
}
