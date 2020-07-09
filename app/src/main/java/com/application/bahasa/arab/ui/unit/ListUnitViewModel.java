package com.application.bahasa.arab.ui.unit;

import androidx.lifecycle.ViewModel;

import com.application.bahasa.arab.data.home.DataModelUnit;
import com.application.bahasa.arab.data.home.ListDataViewUnit;

import java.util.List;

public class ListUnitViewModel extends ViewModel {

    List<DataModelUnit> dataModelUnitList(){
        return ListDataViewUnit.listDataModelUnit();
    }
}
