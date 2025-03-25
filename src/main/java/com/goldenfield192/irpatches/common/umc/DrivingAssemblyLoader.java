package com.goldenfield192.irpatches.common.umc;

import cam72cam.immersiverailroading.library.ModelComponentType;
import cam72cam.immersiverailroading.library.ValveGearConfig;
import cam72cam.immersiverailroading.model.ModelState;
import cam72cam.immersiverailroading.model.components.ComponentProvider;
import cam72cam.immersiverailroading.model.components.ModelComponent;
import cam72cam.immersiverailroading.model.part.DrivingAssembly;
import cam72cam.immersiverailroading.model.part.ValveGear;
import cam72cam.immersiverailroading.model.part.WheelSet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Hack to implement right-first valve gear
 */
public class DrivingAssemblyLoader {
    private static final Method valveGear$get;

    static {
        try {
            valveGear$get = ValveGear.class.getDeclaredMethod("get", WheelSet.class, ValveGearConfig.class,
                                                              ComponentProvider.class, ModelState.class,
                                                              ModelComponentType.ModelPosition.class, float.class);
            valveGear$get.setAccessible(true);
        } catch(NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    //Replace of DrivingAssembly.get
    public static DrivingAssembly get(ValveGearConfig type, ComponentProvider provider, ModelState state, ModelComponentType.ModelPosition pos, float angleOffset, int multiplier, WheelSet... backups) throws InvocationTargetException, IllegalAccessException {
        WheelSet wheels = WheelSet.get(provider, state, pos == null ?
                                                        ModelComponentType.WHEEL_DRIVER_X :
                                                        ModelComponentType.WHEEL_DRIVER_POS_X, pos, angleOffset);
        if(wheels == null) {
            for(WheelSet backup : backups) {
                if(backup != null) {
                    wheels = backup;
                    break;
                }
            }
        }
        if(wheels == null) {
            return null;
        }

        ValveGear left = (ValveGear) valveGear$get.invoke(null, wheels, type, provider, state,
                                                          ModelComponentType.ModelPosition.LEFT.and(pos), 0);
        ValveGear inner_left = (ValveGear) valveGear$get.invoke(null, wheels, type, provider, state,
                                                                ModelComponentType.ModelPosition.INNER_LEFT.and(pos),
                                                                180 * multiplier);
        ValveGear center = (ValveGear) valveGear$get.invoke(null, wheels, type, provider, state,
                                                            ModelComponentType.ModelPosition.CENTER.and(pos),
                                                            -120 * multiplier);
        ValveGear inner_right = (ValveGear) valveGear$get.invoke(null, wheels, type, provider, state,
                                                                 ModelComponentType.ModelPosition.INNER_RIGHT.and(pos),
                                                                 90 * multiplier);
        ValveGear right = (ValveGear) valveGear$get.invoke(null, wheels, type, provider, state,
                                                           ModelComponentType.ModelPosition.RIGHT.and(pos),
                                                           (center == null ?
                                                            -90 :
                                                            -240) * multiplier);

        ModelComponent steamChest = pos == null ?
                                    provider.parse(ModelComponentType.STEAM_CHEST) :
                                    provider.parse(ModelComponentType.STEAM_CHEST_POS, pos);

        state.include(steamChest);

        return new DrivingAssembly(wheels, right, inner_right, center, inner_left, left, steamChest);
    }
}
