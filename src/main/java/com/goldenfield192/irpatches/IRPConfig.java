package com.goldenfield192.irpatches;

import cam72cam.mod.config.ConfigFile;

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

    @ConfigFile.Comment("Does onboard camera collide with blocks?")
    public static boolean OnboardCameraCollideWithBlock = true;
}
