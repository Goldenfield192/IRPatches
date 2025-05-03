package com.goldenfield192.irpatches.mixins.immersiverailroading.gui;

import cam72cam.immersiverailroading.gui.TrackGui;
import cam72cam.immersiverailroading.items.nbt.RailSettings;
import cam72cam.immersiverailroading.library.GuiText;
import cam72cam.mod.MinecraftClient;
import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.helpers.GUIHelpers;
import cam72cam.mod.gui.screen.*;
import com.goldenfield192.irpatches.IRPConfig;
import com.goldenfield192.irpatches.gui.IRPGUIHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(TrackGui.class)
public class MixinTrackGui {
    @Shadow(remap = false)
    private CheckBox isGradeCrossingCB;
    @Shadow(remap = false)
    private RailSettings.Mutable settings;
    @Unique
    private Button jumpToIRPbtn;

    @ModifyConstant(method = "init", constant = @Constant(intValue = 6), remap = false)
    private int inject1(int constant) {
        return constant - 1;
    }

    @Inject(method = "init", at = @At("TAIL"), remap = false, locals = LocalCapture.CAPTURE_FAILSOFT)
    public void inject2(IScreenBuilder screen, CallbackInfo ci, int width, int height, int xtop, int ytop, Slider zoom_slider) {
        isGradeCrossingCB = new CheckBox(screen, xtop + 102, ytop - 38, GuiText.SELECTOR_GRADE_CROSSING.toString(),
                                         settings.isGradeCrossing) {
            public void onClick(Player.Hand hand) {
                settings.isGradeCrossing = isGradeCrossingCB.isChecked();
            }
        };

        ytop = -GUIHelpers.getScreenHeight() / 4;
        jumpToIRPbtn = new Button(screen, -150 + (GUIHelpers.getScreenWidth() / 2), ytop, 150, 20, "test") {
            @Override
            public void onClick(Player.Hand hand) {
                screen.close();
                IRPGUIHelper.IRP_TRACK_BLUEPRINT.open(MinecraftClient.getPlayer());
            }
        };
    }

    @ModifyConstant(method = "lambda$init$0", constant = @Constant(intValue = 1000), remap = false)
    public int modConst(int constant) {
        return IRPConfig.MaxTrackLength;
    }
}
