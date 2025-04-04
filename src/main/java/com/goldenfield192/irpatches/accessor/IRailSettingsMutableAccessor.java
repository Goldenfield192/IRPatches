package com.goldenfield192.irpatches.accessor;

public interface IRailSettingsMutableAccessor extends IRailSettingsAccessor {
    //Ctrl2
    void setNearEnd(float degree);

    //Ctrl1
    void setFarEnd(float degree);

    float getNearEndTilt();

    float getFarEndTilt();
}
