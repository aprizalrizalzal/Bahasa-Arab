package com.application.bahasa.arab.ui.additional;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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
import com.application.bahasa.arab.data.DataModelAdditional;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;


public class ListAdditionalFragment extends Fragment implements ListAdditionalFragmentCallShare, ListAdditionalFragmentCallDownload {

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

            ListAdditionalAdapter adapter = new ListAdditionalAdapter(this, this);
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
            ShareCompat.IntentBuilder
                    .from(getActivity())
                    .setType(mimeType).setChooserTitle("Share now")
                    .setText(getResources().getString(R.string.share_text, dataModelAdditional.getAdditionalTitle(), dataModelAdditional.getAdditionalLink()))
                    .startChooser();
        }
    }

    @Override
    public void onDownloadClick(DataModelAdditional dataModelAdditional) {
        if (getActivity() !=null){
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(dataModelAdditional.getAdditionalLink()));
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalFilesDir(getActivity(), Environment.DIRECTORY_DOCUMENTS,dataModelAdditional.getAdditionalTitle());
            DownloadManager downloadManager = (DownloadManager)getActivity().getBaseContext().getSystemService(Context.DOWNLOAD_SERVICE);
            request.setMimeType("application/pdf");
            request.allowScanningByMediaScanner();
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            if (writableExternalStorage() && checkPermission() && haveNetwork()){
                downloadManager.enqueue(request);
                Toast.makeText(getContext(), "Download "+dataModelAdditional.getAdditionalTitle(), Toast.LENGTH_SHORT).show();
            }else if (!haveNetwork()){
                Toast.makeText(getContext(),R.string.not_have_network, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean writableExternalStorage(){
        if (Environment.getExternalStorageState().equals(Environment.getExternalStorageState())) {
            Log.i("state", "yes, it is writable!");
            return true;
        }else {
            return false;
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