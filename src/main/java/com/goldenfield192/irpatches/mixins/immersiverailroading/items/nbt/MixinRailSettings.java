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
    @Unique
    public float IRPatch$bumpiness;

    @Unique
    public int IRPatch$transferTableEntryNum;
    @Unique
    public int IRPatch$transferTableEntryDist;

    @Inject(method = "from", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private static void from(ItemStack stack, CallbackInfoReturnable<RailSettings> cir) {
        Constructor<RailSettings.Mutable> constructor;
        RailSettings m;
        TagCompound tag = stack.getTagCompound();
        try {
            constructor = RailSettings.Mutable.class.getDeclaredConstructor(TagCompound.class);
            constructor.setAccessible(true);
            m = constructor.newInstance(tag).immutable();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        if (tag != null) {
            TagCompound irp = tag.get("irp");
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
                    accessor.setTransferTableEntryDistance(0);
                }
            } else {
                accessor.setFarEnd(0);
                accessor.setNearEnd(0);
                accessor.setBumpiness(0);
                accessor.setTransferTableEntryNum(1);
                accessor.setTransferTableEntryDistance(0);
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
    public void setBumpiness(float factor) {
        this.IRPatch$bumpiness = factor;
    }

    @Override
    public void setTransferTableEntryNum(int num) {
        this.IRPatch$transferTableEntryNum = num;
    }

    @Override
    public void setTransferTableEntryDistance(int distance) {
        this.IRPatch$transferTableEntryDist = distance;
    }

    @Override
    public float getNearEndTilt() {
        return IRPatch$ctrl2Roll;
    }

    @Override
    public float getFarEndTilt() {
        return IRPatch$ctrl1Roll;
    }

    @Override
    public float getBumpiness() {
        return IRPatch$bumpiness;
    }

    @Override
    public int getTransferTableEntryNum() {
        return IRPatch$transferTableEntryNum;
    }

    @Override
    public int getTransferTableEntryDistance() {
        return IRPatch$transferTableEntryDist;
    }

    @Inject(method = "write", at = @At(value = "INVOKE", target = "Lcam72cam/mod/item/ItemStack;setTagCompound(Lcam72cam/mod/serialization/TagCompound;)V"), remap = false)
    public void write(ItemStack stack, CallbackInfo ci, @Local TagCompound data) {
        TagCompound tag = new TagCompound();
        tag.setFloat("ctrl1", ((IRailSettingsAccessor) this).getFarEndTilt());
        tag.setFloat("ctrl2", ((IRailSettingsAccessor) this).getNearEndTilt());
        tag.setFloat("bumpiness", ((IRailSettingsAccessor) this).getBumpiness());
        tag.setInteger("transferNum", ((IRailSettingsAccessor) this).getTransferTableEntryNum());
        tag.setInteger("transferDist", ((IRailSettingsAccessor) this).getTransferTableEntryDistance());
        data.set("irp", tag);
    }
}
