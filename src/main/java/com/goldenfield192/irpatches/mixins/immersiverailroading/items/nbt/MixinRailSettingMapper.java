package com.goldenfield192.irpatches.mixins.immersiverailroading.items.nbt;

import cam72cam.immersiverailroading.items.nbt.RailSettings;
import cam72cam.mod.serialization.TagCompound;
import com.goldenfield192.irpatches.accessor.IRailSettingsAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Mixin(RailSettings.Mapper.class)
public class MixinRailSettingMapper {
    @Inject(method = "lambda$apply$0", at = @At("RETURN"), remap = false)
    private static void lambda0(String fieldName, TagCompound d, RailSettings o, CallbackInfo ci) {
        TagCompound tag = new TagCompound();
        IRailSettingsAccessor accessor = (IRailSettingsAccessor) o;
        tag.setFloat("ctrl1", accessor.getFarEndTilt());
        tag.setFloat("ctrl2", accessor.getNearEndTilt());
        tag.setFloat("bumpiness", accessor.getBumpiness());
        tag.setInteger("transferNum", accessor.getTransferTableEntryNum());
        tag.setInteger("transferDist", accessor.getTransferTableEntryDistance());
        d.set("irp", tag);
    }

    @Inject(method = "lambda$apply$1", at = @At("HEAD"), remap = false, cancellable = true)
    private static void lambda1(String fieldName, TagCompound d, CallbackInfoReturnable<RailSettings> cir) {
        Constructor<RailSettings.Mutable> constructor;
        RailSettings m;
        try {
            constructor = RailSettings.Mutable.class.getDeclaredConstructor(TagCompound.class);
            constructor.setAccessible(true);
            m = constructor.newInstance(d.get(fieldName)).immutable();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        
        TagCompound irp = d.get("irp");
        IRailSettingsAccessor accessor = (IRailSettingsAccessor) m;
        if (irp != null) {
            Float ctrl1 = irp.getFloat("ctrl1");
            if (ctrl1 != null) {
                accessor.setFarEnd(ctrl1);
            } else {
                accessor.setFarEnd(0);
            }

            Float ctrl2 = irp.getFloat("ctrl2");
            if (ctrl2 != null) {
                accessor.setNearEnd(ctrl2);
            } else {
                accessor.setNearEnd(0);
            }

            Float bumpiness = irp.getFloat("bumpiness");
            if (bumpiness != null) {
                accessor.setBumpiness(bumpiness);
            } else {
                accessor.setBumpiness(0);
            }

            Integer transferNum = irp.getInteger("transferNum");
            if (transferNum != null) {
                accessor.setTransferTableEntryNum(transferNum);
            } else {
                accessor.setTransferTableEntryNum(1);
            }

            Integer transferDist = irp.getInteger("transferDist");
            if (transferDist != null) {
                accessor.setTransferTableEntryDistance(transferDist);
            } else {
                accessor.setTransferTableEntryDistance(1);
            }
        } else {
            accessor.setFarEnd(0);
            accessor.setNearEnd(0);
            accessor.setBumpiness(0);
            accessor.setTransferTableEntryNum(1);
            accessor.setTransferTableEntryDistance(1);
        }
        cir.setReturnValue(m);
    }
}