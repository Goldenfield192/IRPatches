package com.goldenfield192.irpatches.mixins.immersiverailroading.gui;

import cam72cam.immersiverailroading.gui.TrackGui;
import cam72cam.immersiverailroading.items.nbt.RailSettings;
import cam72cam.immersiverailroading.library.GuiText;
import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.helpers.GUIHelpers;
import cam72cam.mod.gui.screen.CheckBox;
import cam72cam.mod.gui.screen.IScreenBuilder;
import cam72cam.mod.gui.screen.Slider;
import com.goldenfield192.irpatches.accessor.IRailSettingsAccessor;
import com.goldenfield192.irpatches.accessor.IRailSettingsMutableAccessor;
import com.goldenfield192.irpatches.common.umc.IRPConfig;
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
    private Slider ctrl1RollSlider;
    @Unique
    private Slider ctrl2RollSlider;

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
        IRailSettingsAccessor accessor = (IRailSettingsMutableAccessor) settings;
        this.ctrl1RollSlider = new Slider(screen, -150 + (GUIHelpers.getScreenWidth() / 2), ytop, "", -14.2, 14.2,
                                          accessor.getFarEndTilt(), true) {
            @Override
            public void onSlider() {
                accessor.setFarEnd((float) this.getValue());
                ctrl1RollSlider.setText("Far end degrees: " + String.format("%.2f", accessor.getFarEndTilt()));
            }
        };
        ytop += height;
        this.ctrl2RollSlider = new Slider(screen, -150 + (GUIHelpers.getScreenWidth() / 2), ytop, "", -14.2, 14.2,
                                          accessor.getNearEndTilt(), true) {
            @Override
            public void onSlider() {
                accessor.setNearEnd((float) this.getValue());
                ctrl2RollSlider.setText("Near end degrees: " + String.format("%.2f", accessor.getNearEndTilt()));
            }
        };

        this.ctrl1RollSlider.onSlider();
        this.ctrl2RollSlider.onSlider();
    }

    @ModifyConstant(method = "lambda$init$0", constant = @Constant(intValue = 1000), remap = false)
    public int modConst(int constant) {
        return IRPConfig.MaxTrackLength;
    }
}
