package com.application.bahasa.arab.data.home;

public class ModelSemester {

    private String semesterTitle;
    private String semesterPage;
    private String semesterCover;
    private String semesterLink;
    private String semesterLinkMp3;

    public ModelSemester(String semesterTitle, String semesterPage, String semesterCover, String semesterLink, String semesterLinkMp3) {
        this.semesterTitle = semesterTitle;
        this.semesterPage = semesterPage;
        this.semesterCover = semesterCover;
        this.semesterLink = semesterLink;
        this.semesterLinkMp3 = semesterLinkMp3;
    }



    public String getSemesterTitle() {
        return semesterTitle;
    }

    public String getSemesterPage() {
        return semesterPage;
    }

    public String getSemesterCover() {
        return semesterCover;
    }

    public String getSemesterLink() {
        return semesterLink;
    }

    public String getSemesterLinkMp3() {
        return semesterLinkMp3;
    }

}
