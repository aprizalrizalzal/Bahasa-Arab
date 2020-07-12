package com.application.bahasa.arab.ui.semester;

import androidx.lifecycle.ViewModel;

import com.application.bahasa.arab.data.home.ModelSemester;
import com.application.bahasa.arab.data.home.ListDataSemester;

import java.util.List;

public class ListSemesterViewModel extends ViewModel {

    List<ModelSemester> modelSemestersList(){
        return ListDataSemester.listDataSemester();
    }
}
