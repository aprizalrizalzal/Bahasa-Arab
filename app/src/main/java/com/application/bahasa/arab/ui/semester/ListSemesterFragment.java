package com.application.bahasa.arab.ui.semester;

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
import com.application.bahasa.arab.data.DataModelSemester;

import java.util.List;

public class ListSemesterFragment extends Fragment implements ListSemesterFragmentCallback {

    public ListSemesterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_semester, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv_voice = view.findViewById(R.id.rv_voice);

        if (getActivity() !=null){
            ListSemesterViewModel viewModel = new ViewModelProvider(this,new ViewModelProvider.NewInstanceFactory()).get(ListSemesterViewModel.class);
            List<DataModelSemester> dataModelSemesters = viewModel.dataModelSemesters();

            ListSemesterAdapter adapter = new ListSemesterAdapter(this);
            adapter.setDataModelSemesterArrayList(dataModelSemesters);

            rv_voice.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_voice.setHasFixedSize(true);
            rv_voice.setAdapter(adapter);
        }
    }

    @Override
    public void onShareClick(DataModelSemester dataModelSemester) {
        if (getActivity() !=null){
            String mimeType = "text/plain";
            ShareCompat.IntentBuilder.from(getActivity()).setType(mimeType).setChooserTitle("Share this theory now").setText(getResources().getString(R.string.share_text, dataModelSemester.getSemesterTitle(), dataModelSemester.getSemesterOverview())).startChooser();
        }
    }
}