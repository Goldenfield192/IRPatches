package com.goldenfield192.irpatches.mixins.immersiverailroading.library;

import cam72cam.immersiverailroading.*;
import cam72cam.immersiverailroading.library.GuiTypes;
import cam72cam.mod.config.ConfigGui;
import cam72cam.mod.gui.GuiRegistry;
import cam72cam.mod.resource.Identifier;
import com.goldenfield192.irpatches.common.umc.IRPConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiTypes.class)
public class MixinGuiTypes {
    @Shadow(remap = false)
    @Final
    @Mutable
    public static GuiRegistry.GUI CONFIG;

    @Inject(method = "register", at = @At("HEAD"), remap = false)
    private static void init(CallbackInfo ci){
        CONFIG = GuiRegistry.register(new Identifier(ImmersiveRailroading.MODID, "config"), () -> new ConfigGui(Config.class, ConfigGraphics.class, ConfigSound.class, ConfigPermissions.class, IRPConfig.class));
    }
}
