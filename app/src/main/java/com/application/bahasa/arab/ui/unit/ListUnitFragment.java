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
import com.application.bahasa.arab.data.DataModelUnit;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

public class ListUnitFragment extends Fragment implements ListUnitFragmentCallback {

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
        RecyclerView rv_unit = view.findViewById(R.id.rv_unit);
        AdView adViewUnit =view.findViewById(R.id.adViewUnit);

        if (getActivity() !=null){
            AdRequest adRequest = new AdRequest.Builder().build();
            adViewUnit.loadAd(adRequest);

            ListUnitViewModel viewModel = new ViewModelProvider(this,new ViewModelProvider.NewInstanceFactory()).get(ListUnitViewModel.class);
            List<DataModelUnit> dataModelTheories = viewModel.dataModelUnitList();

            ListUnitAdapter adapter = new ListUnitAdapter(this);
            adapter.setDataModelUnitArrayList(dataModelTheories);

            rv_unit.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_unit.setHasFixedSize(true);
            rv_unit.setAdapter(adapter);
        }
    }

    @Override
    public void onShareClick(DataModelUnit dataModelUnit) {
        if (getActivity() !=null){
            String mimeType = "text/plain";
            ShareCompat.IntentBuilder
                    .from(getActivity())
                    .setType(mimeType)
                    .setChooserTitle("Share now")
                    .setText(getResources().getString(R.string.share_text, dataModelUnit.getUnitTitle(), dataModelUnit.getUnitLink()))
                    .startChooser();
        }
    }
}