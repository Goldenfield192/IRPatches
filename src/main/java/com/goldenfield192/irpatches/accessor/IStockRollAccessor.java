package com.goldenfield192.irpatches.accessor;

public interface IStockRollAccessor {
    float getFrontRollDegrees();

    void setFrontRollDegrees(float val);

    float getRearRollDegrees();

    void setRearRollDegrees(float val);

    default float getAverageRollDegrees(){
        return (getFrontRollDegrees() + getRearRollDegrees()) / 2f;
    }

    default float getAverageRollRadians(){
        return (float) Math.toRadians(getAverageRollDegrees());
    }
}
