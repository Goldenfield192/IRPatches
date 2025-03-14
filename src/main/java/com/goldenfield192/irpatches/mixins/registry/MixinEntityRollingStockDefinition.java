package com.goldenfield192.irpatches.mixins.registry;

import cam72cam.immersiverailroading.registry.EntityRollingStockDefinition;
import cam72cam.immersiverailroading.util.DataBlock;
import com.goldenfield192.irpatches.common.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Prevent widgets with TV modifier being rendered in inventory
 * @see com.goldenfield192.irpatches.mixins.model.part.MixinControl
 */
@Mixin(value = EntityRollingStockDefinition.class)
public class MixinEntityRollingStockDefinition {
    @Shadow(remap = false)
    @Final
    public List<String> itemGroups;

    @Shadow(remap = false)
    @Final
    public String defID;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    public <T> void mixin(Class<T> type, String defID, DataBlock data, CallbackInfo ci){
        List<String> list = itemGroups.stream().filter(x -> !x.contains("TV_default") && x.contains("_TV_")).collect(Collectors.toList());
        this.itemGroups.removeAll(list);
    }

    @Inject(method = "loadData",
            at = @At(value = "TAIL"),
            remap = false)
    public void mixinLoadData(DataBlock data, CallbackInfo ci){
        ExtraDefinition.loadExtraStockProperties(this.defID, data);
    }
}