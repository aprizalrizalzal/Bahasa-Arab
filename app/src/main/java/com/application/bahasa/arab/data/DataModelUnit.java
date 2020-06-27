package com.application.bahasa.arab.data;

public class DataModelUnit {

    private String unitId;
    private String unitTitle;
    private String unitPage;
    private String unitCover;
    private String unitLink;
    private String unitOverview;

    public DataModelUnit(String unitId, String unitTitle, String unitPage, String unitCover, String unitLink, String unitOverview) {
        this.unitId = unitId;
        this.unitTitle = unitTitle;
        this.unitPage = unitPage;
        this.unitCover = unitCover;
        this.unitLink = unitLink;
        this.unitOverview = unitOverview;
    }

    public String getUnitPage() {
        return unitPage;
    }

    public void setUnitPage(String unitPage) {
        this.unitPage = unitPage;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitTitle() {
        return unitTitle;
    }

    public void setUnitTitle(String unitTitle) {
        this.unitTitle = unitTitle;
    }

    public String getUnitCover() {
        return unitCover;
    }

    public void setUnitCover(String unitCover) {
        this.unitCover = unitCover;
    }

    public String getUnitLink() {
        return unitLink;
    }

    public void setUnitLink(String unitLink) {
        this.unitLink = unitLink;
    }

    public String getUnitOverview() {
        return unitOverview;
    }

    public void setUnitOverview(String unitOverview) {
        this.unitOverview = unitOverview;
    }
}
