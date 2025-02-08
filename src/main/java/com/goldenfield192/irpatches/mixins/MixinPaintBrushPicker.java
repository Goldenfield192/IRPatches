package com.goldenfield192.irpatches.mixins;

import cam72cam.immersiverailroading.ImmersiveRailroading;
import cam72cam.immersiverailroading.gui.PaintBrushPicker;
import cam72cam.mod.text.TextUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

//@Mixin(PaintBrushPicker.class)
//public class MixinPaintBrushPicker {
//    @ModifyArgs(method = "init", at = @At(value = "NEW", target = "(Lcam72cam/immersiverailroading/gui/PaintBrushPicker;Lcam72cam/mod/gui/screen/IScreenBuilder;IIILjava/lang/String;Ljava/util/Map;)Lcam72cam/immersiverailroading/gui/PaintBrushPicker$1;"))
//    public void mixinArgs(Args args){
//        Map<String, String> map = args.get(4);
//        for(Map.Entry<String, String> entry : map.entrySet()){
//        }
//        args.set(4, );
//    }
//}
