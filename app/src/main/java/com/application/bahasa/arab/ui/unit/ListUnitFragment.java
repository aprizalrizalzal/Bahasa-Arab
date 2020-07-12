package com.application.bahasa.arab.ui.unit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.bahasa.arab.R;
import com.application.bahasa.arab.data.home.ModelUnit;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

public class ListUnitFragment extends Fragment implements ListUnitFragmentCallback {

    private ListUnitAdapter adapter = new ListUnitAdapter(this);

    public ListUnitFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_unit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv_unit = view.findViewById(R.id.rvUnit);
        AdView adViewUnit =view.findViewById(R.id.adViewUnit);
        AdRequest adRequest = new AdRequest.Builder().build();
        adViewUnit.loadAd(adRequest);

        if (getActivity() !=null){

            ListUnitViewModel viewModel = new ViewModelProvider(this,new ViewModelProvider.NewInstanceFactory()).get(ListUnitViewModel.class);
            List<ModelUnit> modelUnit = viewModel.modelUnitList();

            adapter.setModelUnitArrayList(modelUnit);

            rv_unit.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_unit.setHasFixedSize(true);
            rv_unit.setAdapter(adapter);
        }
    }

    @Override
    public void onShareClick(ModelUnit modelUnit) {
        if (getActivity() !=null){
            String mimeType = "text/plain";
            ShareCompat.IntentBuilder
                    .from(getActivity())
                    .setType(mimeType)
                    .setChooserTitle("Share")
                    .setText(getResources().getString(R.string.share_text, modelUnit.getUnitTitle(), modelUnit.getUnitLink()))
                    .startChooser();
        }
    }
}