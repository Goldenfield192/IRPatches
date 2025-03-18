package com.goldenfield192.irpatches.mixins.immersiverailroading.items.nbt;

import cam72cam.immersiverailroading.items.nbt.RailSettings;
import cam72cam.mod.serialization.*;
import com.goldenfield192.irpatches.accessor.IRailSettingsAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Mixin(RailSettings.Mapper.class)
public class MixinRailSettingMapper {
    @Inject(method = "apply",
            at = @At(value = "HEAD"),
            remap = false, cancellable = true)
    //We want to save some extra data......However I found it errors when injecting into lambda
    public void overwriteSave(Class<RailSettings> type, String fieldName, TagField tag, CallbackInfoReturnable<TagMapper.TagAccessor<RailSettings>> cir){
        cir.setReturnValue(new TagMapper.TagAccessor<>(
                (d, o) -> {
                    TagCompound target = new TagCompound();
                    try {
                        TagSerializer.serialize(target, o.mutable());
                    } catch(SerializationException e) {
                        // This is messy
                        throw new RuntimeException(e);
                    }
                    d.set(fieldName, target);
                },
                d -> {
                    try {
                        Constructor<RailSettings.Mutable> constructor = RailSettings.Mutable.class.getDeclaredConstructor(TagCompound.class);
                        constructor.setAccessible(true);
                        RailSettings m = constructor.newInstance(d.get(fieldName)).immutable();
                        if(d.get(fieldName).getFloat("ctrl1") != null){
                            ((IRailSettingsAccessor) m).IRPatch$setFarEnd(d.get(fieldName).getFloat("ctrl1"));
                        } else {
                            ((IRailSettingsAccessor) m).IRPatch$setFarEnd(0);
                        }
                        if(d.get(fieldName).getFloat("ctrl2") != null){
                            ((IRailSettingsAccessor) m).IRPatch$setNearEnd(d.get(fieldName).getFloat("ctrl2"));
                        } else {
                            ((IRailSettingsAccessor) m).IRPatch$setNearEnd(0);
                        }
                        return m;
                    } catch(NoSuchMethodException | InvocationTargetException |
                            InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }));
    }
}