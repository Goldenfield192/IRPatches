package com.goldenfield192.irpatches;

import cam72cam.mod.text.TextUtil;

public enum IRPText {
    FAR_END_ROLL("selector.far_roll"),
    NEAR_END_ROLL("selector.near_roll"),
    BUMPINESS("selector.bumpiness"),
    TRANSFER_TABLE_ENTRY_NUM("selector.transfer_table_entry_num"),
    TRANSFER_TABLE_ENTRY_DIST("selector.transfer_table_entry_dist");


    String value;

    IRPText(String s){
        this.value = s;
    }

    public String getRaw() {
        return "gui.immersiverailroading:" + value;
    }

    public String toString(Object...objects) {
        return TextUtil.translate(getRaw(), objects);
    }
}
