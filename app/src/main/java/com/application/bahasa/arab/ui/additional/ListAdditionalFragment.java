package com.application.bahasa.arab.ui.additional;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.bahasa.arab.R;
import com.application.bahasa.arab.data.home.DataModelAdditional;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;


public class ListAdditionalFragment extends Fragment implements ListAdditionalFragmentCallback {

    private ListAdditionalAdapter adapter = new ListAdditionalAdapter(this);
    public ListAdditionalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_additional, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv_video = view.findViewById(R.id.rv_additional);
        AdView adViewAdditional =view.findViewById(R.id.adViewAdditional);

        if (getActivity() !=null){
            AdRequest adRequest = new AdRequest.Builder().build();
            adViewAdditional.loadAd(adRequest);
            ListAdditionalViewModel viewModel = new ViewModelProvider(this,new ViewModelProvider.NewInstanceFactory()).get(ListAdditionalViewModel.class);
            List<DataModelAdditional> dataModelAdditionals = viewModel.dataModelAdditionalList();

            adapter.setDataModelAdditionalArrayList(dataModelAdditionals);

            rv_video.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_video.setHasFixedSize(true);
            rv_video.setAdapter(adapter);
        }
    }

    @Override
    public void onShareClick(DataModelAdditional dataModelAdditional) {
        if (getActivity() !=null){
            String mimeType = "text/plain";
            Toast.makeText(getActivity(), "Share " + dataModelAdditional.getAdditionalTitle(), Toast.LENGTH_SHORT).show();
            ShareCompat.IntentBuilder
                    .from(getActivity())
                    .setType(mimeType).setChooserTitle("Share")
                    .setText(getResources().getString(R.string.share_text, dataModelAdditional.getAdditionalTitle(), dataModelAdditional.getAdditionalLink()))
                    .startChooser();
        }
    }
}