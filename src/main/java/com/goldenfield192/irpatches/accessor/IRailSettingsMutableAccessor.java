package com.goldenfield192.irpatches.accessor;

public interface IRailSettingsMutableAccessor extends IRailSettingsAccessor{
    //Ctrl2
    void IRPatch$setNearEnd(float degree);
    //Ctrl1
    void IRPatch$setFarEnd(float degree);
    float IRPatch$getNearEndTilt();
    float IRPatch$getFarEndTilt();
}
