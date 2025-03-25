package com.goldenfield192.irpatches.mixins.immersiverailroading.items.nbt;

import cam72cam.immersiverailroading.items.nbt.RailSettings;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.serialization.TagCompound;
import com.goldenfield192.irpatches.accessor.IRailSettingsAccessor;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Mixin(RailSettings.class)
public class MixinRailSettings implements IRailSettingsAccessor {
    @Unique
    public float IRPatch$ctrl1Roll;
    @Unique
    public float IRPatch$ctrl2Roll;

    @Inject(method = "from", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private static void from(ItemStack stack, CallbackInfoReturnable<RailSettings> cir) {
        Constructor<RailSettings.Mutable> constructor;
        RailSettings m;
        TagCompound tag = stack.getTagCompound();
        try {
            constructor = RailSettings.Mutable.class.getDeclaredConstructor(TagCompound.class);
            constructor.setAccessible(true);
            m = constructor.newInstance(tag).immutable();
        } catch(NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        if(tag != null) {
            if(tag.get("irp") != null && tag.get("irp").getFloat("ctrl1") != null) {
                ((IRailSettingsAccessor) m).setFarEnd(tag.get("irp").getFloat("ctrl1"));
            } else {
                ((IRailSettingsAccessor) m).setFarEnd(0);
            }
            if(tag.get("irp") != null && tag.get("irp").getFloat("ctrl2") != null) {
                ((IRailSettingsAccessor) m).setNearEnd(tag.get("irp").getFloat("ctrl2"));
            } else {
                ((IRailSettingsAccessor) m).setNearEnd(0);
            }
        }
        cir.setReturnValue(m);
    }

    @Override
    public void setNearEnd(float degree) {
        this.IRPatch$ctrl2Roll = degree;
    }

    @Override
    public void setFarEnd(float degree) {
        this.IRPatch$ctrl1Roll = degree;
    }

    @Override
    public float getNearEndTilt() {
        return IRPatch$ctrl2Roll;
    }

    @Override
    public float getFarEndTilt() {
        return IRPatch$ctrl1Roll;
    }

    @Inject(method = "write", at = @At(value = "INVOKE", target = "Lcam72cam/mod/item/ItemStack;setTagCompound(Lcam72cam/mod/serialization/TagCompound;)V"), remap = false)
    public void write(ItemStack stack, CallbackInfo ci, @Local TagCompound data) {
        TagCompound tag = new TagCompound();
        tag.setFloat("ctrl1", ((IRailSettingsAccessor) this).getFarEndTilt());
        tag.setFloat("ctrl2", ((IRailSettingsAccessor) this).getNearEndTilt());
        data.set("irp", tag);
    }

}
