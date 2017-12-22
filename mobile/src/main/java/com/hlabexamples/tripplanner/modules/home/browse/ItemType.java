package com.hlabexamples.tripplanner.modules.home.browse;

public enum ItemType {
    HOME(0), FAV(1);

    int type;

    ItemType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
