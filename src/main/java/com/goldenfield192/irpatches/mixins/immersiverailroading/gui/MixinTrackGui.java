package com.goldenfield192.irpatches.mixins.immersiverailroading.gui;

import cam72cam.immersiverailroading.gui.TrackGui;
import cam72cam.immersiverailroading.gui.components.ListSelector;
import cam72cam.immersiverailroading.items.nbt.RailSettings;
import cam72cam.immersiverailroading.library.Gauge;
import cam72cam.immersiverailroading.library.GuiText;
import cam72cam.immersiverailroading.library.TrackItems;
import cam72cam.immersiverailroading.track.BuilderTurnTable;
import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.helpers.GUIHelpers;
import cam72cam.mod.gui.screen.*;
import com.goldenfield192.irpatches.accessor.IRailSettingsAccessor;
import com.goldenfield192.irpatches.accessor.IRailSettingsMutableAccessor;
import com.goldenfield192.irpatches.IRPConfig;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.lang.reflect.Field;
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
    @Unique
    private Slider bumpinessSlider;

    //As they only exist in transfer table they can overlap with others
    @Unique
    private Slider transferTableEntryNum;
    @Unique
    private Slider transferTableEntrySpacing;

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
        this.ctrl1RollSlider = new Slider(screen, -150 - xtop, ytop, "", -14.2, 14.2,
                                          accessor.getFarEndTilt(), true) {
            @Override
            public void onSlider() {
                accessor.setFarEnd((float) this.getValue());
                ctrl1RollSlider.setText("Far end rolling: " + String.format("%.2f", accessor.getFarEndTilt()) + "°");
            }
        };
        ytop += height;
        this.ctrl2RollSlider = new Slider(screen, -150 - xtop, ytop, "", -14.2, 14.2,
                                          accessor.getNearEndTilt(), true) {
            @Override
            public void onSlider() {
                accessor.setNearEnd((float) this.getValue());
                ctrl2RollSlider.setText("Near end rolling: " + String.format("%.2f", accessor.getNearEndTilt()) + "°");
            }
        };
        ytop += height;
        this.bumpinessSlider = new Slider(screen, -150 - xtop, ytop, "", 0, 7.1,
                                          accessor.getBumpiness(), true) {
            @Override
            public void onSlider() {
                accessor.setBumpiness((float) this.getValue());
                bumpinessSlider.setText("Bump amplitude: " + String.format("%.2f", accessor.getBumpiness()) + "°");
            }
        };
        ytop += 3 * height;

        this.transferTableEntryNum = new Slider(screen, 25+xtop, ytop, "", 1, 15, accessor.getTransferTableEntryNum(), false) {
            @Override
            public void onSlider() {
                accessor.setTransferTableEntryNum((int) this.getValue());
                transferTableEntryNum.setText("Transfer table entry: " + (int) transferTableEntryNum.getValue());
            }
        };
        transferTableEntryNum.onSlider();
        ytop += height;

        this.transferTableEntrySpacing = new Slider(screen, 25+xtop, ytop, "", 1, 7, accessor.getTransferTableEntryDistance(), false) {
            @Override
            public void onSlider() {
                accessor.setTransferTableEntryDistance((int) this.getValue());
                transferTableEntrySpacing.setText("Distance between 2 entry: " + (int) transferTableEntrySpacing.getValue());
            }
        };
        transferTableEntrySpacing.onSlider();

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

                transferTableEntryNum.setVisible(settings.type == TrackItems.valueOf("TRANSFER_TABLE"));
                transferTableEntrySpacing.setVisible(settings.type == TrackItems.valueOf("TRANSFER_TABLE"));

                ctrl1RollSlider.setEnabled(true);
                ctrl2RollSlider.setEnabled(true);
                bumpinessSlider.setEnabled(true);
                if(settings.type == TrackItems.TURNTABLE
                        || settings.type == TrackItems.SWITCH
                        || settings.type == TrackItems.valueOf("TRANSFER_TABLE")){
                    ctrl1RollSlider.setEnabled(false);
                    ctrl2RollSlider.setEnabled(false);
                    bumpinessSlider.setEnabled(false);
                }
                if (settings.type == TrackItems.TURNTABLE || settings.type == TrackItems.valueOf("TRANSFER_TABLE")) {
                    lengthInput.setText("" + Math.min(Integer.parseInt(lengthInput.getText()),
                                                      BuilderTurnTable.maxLength(settings.gauge))); // revalidate
                }
            }
        };

        this.ctrl1RollSlider.onSlider();
        this.ctrl2RollSlider.onSlider();
        this.bumpinessSlider.onSlider();

        if(settings.type == TrackItems.TURNTABLE || settings.type == TrackItems.SWITCH){
            ctrl1RollSlider.setEnabled(false);
            ctrl2RollSlider.setEnabled(false);
            bumpinessSlider.setEnabled(false);
        }
    }

    @ModifyConstant(method = "lambda$init$0", constant = @Constant(intValue = 1000), remap = false)
    public int modConst(int constant) {
        if(this.settings.type == TrackItems.valueOf("TRANSFER_TABLE")){
            return BuilderTurnTable.maxLength(this.settings.gauge);
        }
        return IRPConfig.MaxTrackLength;
    }

    @Mixin(targets = "cam72cam.immersiverailroading.gui.TrackGui$1")
    private static class GaugeSelector{
        @Dynamic
        @Final
        @Shadow(remap = false)
        private TrackGui this$0 ;

        @Inject(method = "onClick(Lcam72cam/immersiverailroading/library/Gauge;)V", at = @At("RETURN"), remap = false)
        public void inject(Gauge gauge, CallbackInfo ci){
            try {
                Field settingF = TrackGui.class.getDeclaredField("settings");
                settingF.setAccessible(true);
                RailSettings.Mutable settings = (RailSettings.Mutable) settingF.get(this$0);

                Field lengthInputF = TrackGui.class.getDeclaredField("lengthInput");
                lengthInputF.setAccessible(true);
                TextField lengthInput = (TextField) lengthInputF.get(this$0);

                if (settings.type == TrackItems.valueOf("TRANSFER_TABLE")) {
                    lengthInput.setText("" + Math.min(Integer.parseInt(lengthInput.getText()), BuilderTurnTable.maxLength(settings.gauge)));
                }

                lengthInputF.set(this$0, lengthInput);
            }catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
