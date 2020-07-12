package com.application.bahasa.arab.ui.additional;

import androidx.lifecycle.ViewModel;

import com.application.bahasa.arab.data.home.ModelAdditional;
import com.application.bahasa.arab.data.home.ListDataAdditional;

import java.util.List;

public class ListAdditionalViewModel extends ViewModel {

    List<ModelAdditional> modelAdditionalList(){
        return ListDataAdditional.listModelAdditional();
    }
}
