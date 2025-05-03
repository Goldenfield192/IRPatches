package com.goldenfield192.irpatches.gui;

import cam72cam.immersiverailroading.IRItems;
import cam72cam.immersiverailroading.items.nbt.RailSettings;
import cam72cam.immersiverailroading.library.GuiTypes;
import cam72cam.immersiverailroading.library.TrackItems;
import cam72cam.immersiverailroading.net.ItemRailUpdatePacket;
import cam72cam.mod.MinecraftClient;
import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.helpers.GUIHelpers;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;
import cam72cam.mod.gui.screen.Slider;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.render.opengl.RenderState;
import com.goldenfield192.irpatches.accessor.IRailSettingsAccessor;
import com.goldenfield192.irpatches.accessor.IRailSettingsMutableAccessor;

public class IRPTrackGUI implements IScreen {
    private Slider ctrl1RollSlider;
    private Slider ctrl2RollSlider;
    private Slider bumpinessSlider;

    private RailSettings.Mutable settings;

    @Override
    public void init(IScreenBuilder screen) {
        ItemStack stack = MinecraftClient.getPlayer().getHeldItem(Player.Hand.PRIMARY);
        if(stack == null || !stack.is(IRItems.ITEM_TRACK_BLUEPRINT)){
            screen.close();
        }
        this.settings = RailSettings.from(MinecraftClient.getPlayer().getHeldItem(Player.Hand.PRIMARY)).mutable();

        int ytop = -GUIHelpers.getScreenHeight() / 4;
        int height = 20;

        IRailSettingsAccessor accessor = (IRailSettingsMutableAccessor) settings;
        this.ctrl1RollSlider = new Slider(screen, -150 + (GUIHelpers.getScreenWidth() / 2), ytop, "", -14.2, 14.2,
                                          accessor.getFarEndTilt(), true) {
            @Override
            public void onSlider() {
                accessor.setFarEnd((float) this.getValue());
                ctrl1RollSlider.setText("Far end rolling: " + String.format("%.2f", accessor.getFarEndTilt()) + "°");
            }
        };
        ytop += height;
        this.ctrl2RollSlider = new Slider(screen, -150 + (GUIHelpers.getScreenWidth() / 2), ytop, "", -14.2, 14.2,
                                          accessor.getNearEndTilt(), true) {
            @Override
            public void onSlider() {
                accessor.setNearEnd((float) this.getValue());
                ctrl2RollSlider.setText("Near end rolling: " + String.format("%.2f", accessor.getNearEndTilt()) + "°");
            }
        };
        ytop += height;
        this.bumpinessSlider = new Slider(screen, -150 + (GUIHelpers.getScreenWidth() / 2), ytop, "", 0, 7.1,
                                          accessor.getBumpiness(), true) {
            @Override
            public void onSlider() {
                accessor.setBumpiness((float) this.getValue());
                bumpinessSlider.setText("Bump amplitude: " + String.format("%.2f", accessor.getBumpiness()) + "°");
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

    @Override
    public void onEnterKey(IScreenBuilder builder) {

    }

    @Override
    public void onClose() {
        (new ItemRailUpdatePacket(this.settings.immutable())).sendToServer();
//        GuiTypes.RAIL.open(MinecraftClient.getPlayer());
    }

    @Override
    public void draw(IScreenBuilder builder, RenderState state) {
        IScreen.super.draw(builder, state);
    }
}
