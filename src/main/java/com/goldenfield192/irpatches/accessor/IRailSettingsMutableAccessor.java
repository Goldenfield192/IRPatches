package com.goldenfield192.irpatches.accessor;

public interface IRailSettingsMutableAccessor extends IRailSettingsAccessor {
    //Ctrl2
    void setNearEnd(float degree);

    //Ctrl1
    void setFarEnd(float degree);

    //Bumpiness
    void setBumpiness(float factor);

    float getNearEndTilt();

    float getFarEndTilt();

    float getBumpiness();
}
