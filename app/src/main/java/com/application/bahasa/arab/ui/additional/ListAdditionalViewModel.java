package com.application.bahasa.arab.ui.additional;

import androidx.lifecycle.ViewModel;

import com.application.bahasa.arab.data.home.DataModelAdditional;
import com.application.bahasa.arab.data.home.ListDataViewAdditional;

import java.util.List;

public class ListAdditionalViewModel extends ViewModel {

    List<DataModelAdditional> dataModelAdditionalList(){
        return ListDataViewAdditional.listDataModelAdditionals();
    }
}
