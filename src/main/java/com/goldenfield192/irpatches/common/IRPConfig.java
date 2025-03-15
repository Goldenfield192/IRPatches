package com.goldenfield192.irpatches.common;

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
}
