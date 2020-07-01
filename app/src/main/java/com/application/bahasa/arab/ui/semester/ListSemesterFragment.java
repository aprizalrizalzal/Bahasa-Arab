package com.application.bahasa.arab.ui.semester;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.bahasa.arab.R;
import com.application.bahasa.arab.data.DataModelSemester;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

public class ListSemesterFragment extends Fragment implements ListSemesterFragmentCallback, ListSemesterFragmentCallDownload {

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
        RecyclerView rv_voice = view.findViewById(R.id.rv_semester);
        AdView adViewUnit =view.findViewById(R.id.adViewSemester);

        if (getActivity() !=null){
            AdRequest adRequest = new AdRequest.Builder().build();
            adViewUnit.loadAd(adRequest);

            ListSemesterViewModel viewModel = new ViewModelProvider(this,new ViewModelProvider.NewInstanceFactory()).get(ListSemesterViewModel.class);
            List<DataModelSemester> dataModelSemesters = viewModel.dataModelSemesters();

            ListSemesterAdapter adapter = new ListSemesterAdapter(this, this);
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
            ShareCompat.IntentBuilder
                    .from(getActivity())
                    .setType(mimeType)
                    .setChooserTitle("Share now")
                    .setText(getResources().getString(R.string.share_text, dataModelSemester.getSemesterTitle(), dataModelSemester.getSemesterLink()))
                    .startChooser();
        }
    }

    @Override
    public void onDownloadClick(DataModelSemester dataModelSemester) {
        if (getActivity() != null) {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(dataModelSemester.getSemesterLink()));
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalFilesDir(getActivity(), Environment.DIRECTORY_DOCUMENTS,dataModelSemester.getSemesterTitle());
            DownloadManager downloadManager = (DownloadManager) getActivity().getBaseContext().getSystemService(Context.DOWNLOAD_SERVICE);
            request.setMimeType("application/pdf");
            request.allowScanningByMediaScanner();
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            if (haveNetwork()){
                PermissionListener permissionListener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        downloadManager.enqueue(request);
                        Toast.makeText(getContext(), "Download "+dataModelSemester.getSemesterTitle(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(getContext(), deniedPermissions.toString() +getString(R.string.denied_permission), Toast.LENGTH_SHORT).show();
                    }
                };
                TedPermission.with(getActivity())
                        .setPermissionListener(permissionListener)
                        .setDeniedMessage(R.string.denied_message)
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .check();
            }else if (!haveNetwork()){
                Toast.makeText(getContext(),R.string.not_have_network, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkPermission() {
        int check = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return( check == PackageManager.PERMISSION_GRANTED);
    }

    private boolean haveNetwork() {
        boolean haveConnection =false;

        if (getActivity() != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

            if (activeNetworkInfo !=null && activeNetworkInfo.isConnected()){
                haveConnection=true;
            }
        }
        return haveConnection;
    }
}