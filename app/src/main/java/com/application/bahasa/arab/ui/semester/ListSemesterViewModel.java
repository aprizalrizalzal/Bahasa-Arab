package com.application.bahasa.arab.ui.semester;

import androidx.lifecycle.ViewModel;

import com.application.bahasa.arab.data.home.DataModelSemester;
import com.application.bahasa.arab.data.home.ListDataViewSemester;

import java.util.List;

public class ListSemesterViewModel extends ViewModel {

    List<DataModelSemester> dataModelSemesters(){
        return ListDataViewSemester.listDataModelSemester();
    }
}
