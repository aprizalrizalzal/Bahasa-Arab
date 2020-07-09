package com.application.bahasa.arab.data.home;

public class DataModelUnit {

    private String unitTitle;
    private String unitPage;
    private String unitCover;
    private String unitLink;
    private String unitLinkMp3;

    public DataModelUnit(String unitTitle, String unitPage, String unitCover, String unitLink, String unitLinkMp3) {
        this.unitTitle = unitTitle;
        this.unitPage = unitPage;
        this.unitCover = unitCover;
        this.unitLink = unitLink;
        this.unitLinkMp3 = unitLinkMp3;
    }

    public String getUnitPage() {
        return unitPage;
    }

    public String getUnitTitle() {
        return unitTitle;
    }

    public String getUnitCover() {
        return unitCover;
    }

    public String getUnitLink() {
        return unitLink;
    }

    public String getUnitLinkMp3() {
        return unitLinkMp3;
    }

}
