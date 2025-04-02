package com.goldenfield192.irpatches.mixins.immersiverailroading.gui;

import cam72cam.immersiverailroading.gui.TrackGui;
import cam72cam.immersiverailroading.gui.components.ListSelector;
import cam72cam.immersiverailroading.items.nbt.RailSettings;
import cam72cam.immersiverailroading.library.GuiText;
import cam72cam.immersiverailroading.library.TrackItems;
import cam72cam.immersiverailroading.track.BuilderTurnTable;
import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.helpers.GUIHelpers;
import cam72cam.mod.gui.screen.*;
import com.goldenfield192.irpatches.accessor.IRailSettingsAccessor;
import com.goldenfield192.irpatches.accessor.IRailSettingsMutableAccessor;
import com.goldenfield192.irpatches.IRPConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

@Mixin(TrackGui.class)
public class MixinTrackGui {
    @Shadow(remap = false)
    private CheckBox isGradeCrossingCB;
    @Shadow(remap = false)
    private RailSettings.Mutable settings;
    @Shadow(remap = false)
    private ListSelector<TrackItems> typeSelector;
    @Shadow(remap = false)
    private Button typeButton;
    @Shadow(remap = false)
    private Slider degreesSlider;
    @Shadow(remap = false)
    private Slider curvositySlider;
    @Shadow(remap = false)
    private Button smoothingButton;
    @Shadow(remap = false)
    private Button directionButton;
    @Shadow(remap = false)
    private TextField lengthInput;
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

        this.typeSelector = new ListSelector<TrackItems>(screen, width, 100, height, settings.type,
                                                         Arrays.stream(TrackItems.values())
                                                               .filter(i -> i != TrackItems.CROSSING)
                                                               .collect(Collectors.toMap(TrackItems::toString, g -> g,
                                                                                         (u, v) -> u,
                                                                                         LinkedHashMap::new))
        ) {
            @Override
            public void onClick(TrackItems option) {
                settings.type = option;
                typeButton.setText(GuiText.SELECTOR_TYPE.toString(settings.type));
                degreesSlider.setVisible(settings.type.hasQuarters());
                curvositySlider.setVisible(settings.type.hasCurvosity());
                smoothingButton.setVisible(settings.type.hasSmoothing());
                directionButton.setVisible(settings.type.hasDirection());
                ctrl1RollSlider.setVisible(settings.type != TrackItems.TURNTABLE);
                ctrl2RollSlider.setVisible(settings.type != TrackItems.TURNTABLE);
                if (settings.type == TrackItems.TURNTABLE) {
                    lengthInput.setText("" + Math.min(Integer.parseInt(lengthInput.getText()),
                                                      BuilderTurnTable.maxLength(settings.gauge))); // revalidate
                }
            }
        };

        this.ctrl1RollSlider.setVisible(settings.type != TrackItems.TURNTABLE && settings.type != TrackItems.SWITCH);
        this.ctrl2RollSlider.setVisible(settings.type != TrackItems.TURNTABLE && settings.type != TrackItems.SWITCH);

        this.ctrl1RollSlider.onSlider();
        this.ctrl2RollSlider.onSlider();
    }

    @ModifyConstant(method = "lambda$init$0", constant = @Constant(intValue = 1000), remap = false)
    public int modConst(int constant) {
        return IRPConfig.MaxTrackLength;
    }
}
