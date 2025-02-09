package com.goldenfield192.irpatches.mixins;

import cam72cam.immersiverailroading.library.ModelComponentType;
import cam72cam.immersiverailroading.model.StockModel;
import cam72cam.immersiverailroading.registry.EntityRollingStockDefinition;
import cam72cam.immersiverailroading.util.DataBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(value = EntityRollingStockDefinition.class)
public class MixinEntityRollingStockDefinition {

    @Shadow(remap = false)
    @Final
    public List<String> itemGroups;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    public void mixin(Class type, String defID, DataBlock data, CallbackInfo ci){
        List<String> list = itemGroups.stream().filter(x -> !x.contains("TV_default") && x.contains("_TV_")).collect(Collectors.toList());
        this.itemGroups.removeAll(list);
    }
}