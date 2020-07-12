package com.application.bahasa.arab.ui.unit;

import androidx.lifecycle.ViewModel;

import com.application.bahasa.arab.data.home.ModelUnit;
import com.application.bahasa.arab.data.home.ListDataUnit;

import java.util.List;

public class ListUnitViewModel extends ViewModel {

    List<ModelUnit> modelUnitList(){
        return ListDataUnit.listDataUnit();
    }
}
