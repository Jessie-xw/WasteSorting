package com.example.wastesorting.mineListItem;

public class MineItem {

    private String name;

    private int frontImageId;

    private int backImageId;

    public MineItem(String name, int frontImageId, int backImageId) {
        this.name = name;
        this.frontImageId = frontImageId;
        this.backImageId = backImageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFrontImageId() {
        return frontImageId;
    }

    public void setFrontImageId(int frontImageId) {
        this.frontImageId = frontImageId;
    }

    public int getBackImageId() {
        return backImageId;
    }

    public void setBackImageId(int backImageId) {
        this.backImageId = backImageId;
    }
}
