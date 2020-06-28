package com.application.bahasa.arab.data;

import java.util.ArrayList;
import java.util.List;

public class ListDataViewSemester {

    public static List<DataModelSemester> listDataModelSemester(){
        ArrayList<DataModelSemester> dataModelSemesterArrayList = new ArrayList<>();
        dataModelSemesterArrayList.add(new DataModelSemester(
                "smt1",
                "Semester Satu",
                "Halaman 1/114",
                "https://firebasestorage.googleapis.com/v0/b/bahasa-6a723.appspot.com/o/bahasa-arab%2Fcover%2Fs1.png?alt=media&token=1f471e15-1a36-4294-8762-1b431ff730ec",
                "https://firebasestorage.googleapis.com/v0/b/bahasa-6a723.appspot.com/o/bahasa-arab%2Fcontent%2Funit-pertama.pdf?alt=media&token=f1f98536-c57a-4e21-9d4b-3fc0975fcbed"));
        dataModelSemesterArrayList.add(new DataModelSemester(
                "smt2",
                "Semester Dua",
                "Halaman 115/256",
                "https://firebasestorage.googleapis.com/v0/b/bahasa-6a723.appspot.com/o/bahasa-arab%2Fcover%2Fs2.png?alt=media&token=1fdcf21f-b0b3-47b7-ae06-1f43aeeba4ad",
                "https://firebasestorage.googleapis.com/v0/b/bahasa-6a723.appspot.com/o/bahasa-arab%2Fcontent%2Funit-pertama.pdf?alt=media&token=f1f98536-c57a-4e21-9d4b-3fc0975fcbed"));
        dataModelSemesterArrayList.add(new DataModelSemester(
                "smt3",
                "Semester Tiga",
                "Halaman 257/348",
                "https://firebasestorage.googleapis.com/v0/b/bahasa-6a723.appspot.com/o/bahasa-arab%2Fcover%2Fs3.png?alt=media&token=d83005b4-f112-4b84-a23c-22c370d4e1f5",
                "https://firebasestorage.googleapis.com/v0/b/bahasa-6a723.appspot.com/o/bahasa-arab%2Fcontent%2Funit-pertama.pdf?alt=media&token=f1f98536-c57a-4e21-9d4b-3fc0975fcbed"));
        dataModelSemesterArrayList.add(new DataModelSemester(
                "smt4",
                "Semester Empat",
                "Halaman ?/?",
                "https://firebasestorage.googleapis.com/v0/b/bahasa-6a723.appspot.com/o/bahasa-arab%2Fcover%2Fs4.png?alt=media&token=de03c414-8172-4b62-8370-312e6f00a392",
                "https://firebasestorage.googleapis.com/v0/b/bahasa-6a723.appspot.com/o/bahasa-arab%2Fcontent%2Funit-pertama.pdf?alt=media&token=f1f98536-c57a-4e21-9d4b-3fc0975fcbed"));

        return dataModelSemesterArrayList;
    }
}
