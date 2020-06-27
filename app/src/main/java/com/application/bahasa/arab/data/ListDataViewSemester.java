package com.application.bahasa.arab.data;

import java.util.ArrayList;
import java.util.List;

public class ListDataViewSemester {

    public static List<DataModelSemester> listDataModelSemester(){
        ArrayList<DataModelSemester> dataModelSemesterArrayList = new ArrayList<>();
        dataModelSemesterArrayList.add(new DataModelSemester(
                "smt1",
                "Semester Satu",
                "Halaman 1/92",
                "https://firebasestorage.googleapis.com/v0/b/bahasa-6a723.appspot.com/o/bahasa-arab%2Fcover%2F41.jpg?alt=media&token=f6405644-43c3-4beb-806b-63dd8f0c3001",
                "https://firebasestorage.googleapis.com/v0/b/bahasa-6a723.appspot.com/o/bahasa-arab%2Fcontent%2Funit-pertama.pdf?alt=media&token=f1f98536-c57a-4e21-9d4b-3fc0975fcbed"));
        dataModelSemesterArrayList.add(new DataModelSemester(
                "smt2",
                "Semester Dua",
                "Halaman 93/188",
                "https://firebasestorage.googleapis.com/v0/b/bahasa-6a723.appspot.com/o/bahasa-arab%2Fcover%2F42.jpg?alt=media&token=84b9860c-fb2a-462e-9b07-5df78bf2e3da",
                "https://firebasestorage.googleapis.com/v0/b/bahasa-6a723.appspot.com/o/bahasa-arab%2Fcontent%2Funit-pertama.pdf?alt=media&token=f1f98536-c57a-4e21-9d4b-3fc0975fcbed"));
        dataModelSemesterArrayList.add(new DataModelSemester(
                "smt3",
                "Semester Tiga",
                "Halaman 189/280",
                "https://firebasestorage.googleapis.com/v0/b/bahasa-6a723.appspot.com/o/bahasa-arab%2Fcover%2F43.jpg?alt=media&token=98acca1a-1a1a-4442-974e-2803a66d8ad3",
                "https://firebasestorage.googleapis.com/v0/b/bahasa-6a723.appspot.com/o/bahasa-arab%2Fcontent%2Funit-pertama.pdf?alt=media&token=f1f98536-c57a-4e21-9d4b-3fc0975fcbed"));
        dataModelSemesterArrayList.add(new DataModelSemester(
                "smt4",
                "Semester Empat",
                "Halaman 281/378",
                "https://firebasestorage.googleapis.com/v0/b/bahasa-6a723.appspot.com/o/bahasa-arab%2Fcover%2F44.jpg?alt=media&token=20dd8eee-cefe-4caa-b07d-8d592e1ef9a6",
                "https://firebasestorage.googleapis.com/v0/b/bahasa-6a723.appspot.com/o/bahasa-arab%2Fcontent%2Funit-pertama.pdf?alt=media&token=f1f98536-c57a-4e21-9d4b-3fc0975fcbed"));

        return dataModelSemesterArrayList;
    }
}
