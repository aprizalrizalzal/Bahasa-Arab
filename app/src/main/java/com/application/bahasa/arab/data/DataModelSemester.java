package com.application.bahasa.arab.data;

public class DataModelSemester {

    private String semesterId;
    private String semesterTitle;
    private String semesterPage;
    private String semesterCover;
    private String semesterOverview;

    public DataModelSemester(String semesterId, String semesterTitle, String semesterPage, String semesterCover, String semesterOverview) {
        this.semesterId = semesterId;
        this.semesterTitle = semesterTitle;
        this.semesterPage = semesterPage;
        this.semesterCover = semesterCover;
        this.semesterOverview = semesterOverview;
    }



    public String getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(String semesterId) {
        this.semesterId = semesterId;
    }

    public String getSemesterTitle() {
        return semesterTitle;
    }

    public void setSemesterTitle(String semesterTitle) {
        this.semesterTitle = semesterTitle;
    }

    public String getSemesterPage() {
        return semesterPage;
    }

    public void setSemesterPage(String semesterPage) {
        this.semesterPage = semesterPage;
    }

    public String getSemesterCover() {
        return semesterCover;
    }

    public void setSemesterCover(String semesterCover) {
        this.semesterCover = semesterCover;
    }

    public String getSemesterOverview() {
        return semesterOverview;
    }

    public void setSemesterOverview(String semesterOverview) {
        this.semesterOverview = semesterOverview;
    }
}
