package com.goldenfield192.irpatches.common;

import cam72cam.mod.config.ConfigFile;

@ConfigFile.Comment("Configuration File")
@ConfigFile.Name("general")
@ConfigFile.File("irp_general.cfg")
public class IRPConfig {
    @ConfigFile.Comment("Mouse Scroll Speed (negative values invert it)")
    @ConfigFile.Range(min = 1, max = 2)
    public static float ManualFontSize = 1;
}
