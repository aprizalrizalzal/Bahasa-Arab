package com.application.bahasa.arab.data.home;

public class DataModelAdditional {

    private String additionalTitle;
    private String additionalPage;
    private String additionalCover;
    private String additionalLink;
    private String additionalLinkMp3;

    public DataModelAdditional(String additionalTitle, String additionalPage, String additionalCover, String additionalLink, String additionalLinkMp3) {
        this.additionalTitle = additionalTitle;
        this.additionalPage = additionalPage;
        this.additionalCover = additionalCover;
        this.additionalLink = additionalLink;
        this.additionalLinkMp3 = additionalLinkMp3;
    }

    public String getAdditionalPage() {
        return additionalPage;
    }

    public String getAdditionalTitle() {
        return additionalTitle;
    }

    public String getAdditionalCover() {
        return additionalCover;
    }

    public String getAdditionalLink() {
        return additionalLink;
    }

    public String getAdditionalLinkMp3() {
        return additionalLinkMp3;
    }

}
