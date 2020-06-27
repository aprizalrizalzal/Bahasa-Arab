package com.application.bahasa.arab.data;

public class DataModelAdditional {

    private String additionalId;
    private String additionalTitle;
    private String additionalPage;
    private String additionalCover;
    private String additionalLink;
    private String additionalOverview;

    public DataModelAdditional(String additionalId, String additionalTitle, String additionalPage, String additionalCover, String additionalLink, String additionalOverview) {
        this.additionalId = additionalId;
        this.additionalTitle = additionalTitle;
        this.additionalPage = additionalPage;
        this.additionalCover = additionalCover;
        this.additionalLink = additionalLink;
        this.additionalOverview = additionalOverview;
    }

    public String getAdditionalPage() {
        return additionalPage;
    }

    public void setAdditionalPage(String additionalPage) {
        this.additionalPage = additionalPage;
    }

    public String getAdditionalId() {
        return additionalId;
    }

    public void setAdditionalId(String additionalId) {
        this.additionalId = additionalId;
    }

    public String getAdditionalTitle() {
        return additionalTitle;
    }

    public void setAdditionalTitle(String additionalTitle) {
        this.additionalTitle = additionalTitle;
    }

    public String getAdditionalCover() {
        return additionalCover;
    }

    public void setAdditionalCover(String additionalCover) {
        this.additionalCover = additionalCover;
    }
    public String getAdditionalLink() {
        return additionalLink;
    }

    public void setAdditionalLink(String additionalLink) {
        this.additionalLink = additionalLink;
    }

    public String getAdditionalOverview() {
        return additionalOverview;
    }

    public void setAdditionalOverview(String additionalOverview) {
        this.additionalOverview = additionalOverview;
    }
}
