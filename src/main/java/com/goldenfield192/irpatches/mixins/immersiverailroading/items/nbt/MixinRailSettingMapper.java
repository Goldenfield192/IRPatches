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
        tag.setFloat("ctrl1", ((IRailSettingsAccessor) o).getFarEndTilt());
        tag.setFloat("ctrl2", ((IRailSettingsAccessor) o).getNearEndTilt());
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

        if (d.get("irp") != null && d.get("irp").getFloat("ctrl1") != null) {
            ((IRailSettingsAccessor) m).setFarEnd(d.get("irp").getFloat("ctrl1"));
        } else {
            ((IRailSettingsAccessor) m).setFarEnd(0);
        }
        if (d.get("irp") != null && d.get("irp").getFloat("ctrl2") != null) {
            ((IRailSettingsAccessor) m).setNearEnd(d.get("irp").getFloat("ctrl2"));
        } else {
            ((IRailSettingsAccessor) m).setNearEnd(0);
        }
        cir.setReturnValue(m);
    }
}