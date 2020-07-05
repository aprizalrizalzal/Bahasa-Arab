package com.application.bahasa.arab.ui.semester;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.bahasa.arab.R;
import com.application.bahasa.arab.data.DataModelSemester;
import com.application.bahasa.arab.ui.main.DetailActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListSemesterAdapter extends RecyclerView.Adapter<ListSemesterAdapter.ViewHolder> implements Filterable {

    private final ListSemesterFragmentCallback callback;
    private ArrayList<DataModelSemester> dataModelSemesterArrayList = new ArrayList<>();
    private ArrayList<DataModelSemester> getDataModelSemesterArrayList = new ArrayList<>();

    public ListSemesterAdapter(ListSemesterFragmentCallback callback) {
        this.callback = callback;
    }

    public void setDataModelSemesterArrayList(List<DataModelSemester> dataModelSemesters) {

        if (dataModelSemesterArrayList == null)return;
        dataModelSemesterArrayList.clear();
        dataModelSemesterArrayList.addAll(dataModelSemesters);

        if (getDataModelSemesterArrayList == null)return;
        getDataModelSemesterArrayList.clear();
        getDataModelSemesterArrayList.addAll(dataModelSemesters);
    }

    @NonNull
    @Override
    public ListSemesterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_semester,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataModelSemester dataModelSemester = dataModelSemesterArrayList.get(position);
        holder.bind(dataModelSemester);
    }

    @Override
    public int getItemCount() {
        return dataModelSemesterArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            ArrayList<DataModelSemester> filterData = new ArrayList<>();
            if (charSequence.toString().isEmpty()){
                filterData.addAll(getDataModelSemesterArrayList);
            }else {
                for (DataModelSemester semester : getDataModelSemesterArrayList){
                    if (semester.getSemesterTitle().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filterData.add(semester);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filterData;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            dataModelSemesterArrayList.clear();
            dataModelSemesterArrayList.addAll((Collection<? extends DataModelSemester>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageCoverSemester;
        final TextView titleSemester,pageSemester;
        final ImageButton downloadSemester,bookSemester,shareSemester;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCoverSemester = itemView.findViewById(R.id.image_coverSemester);
            titleSemester = itemView.findViewById(R.id.tv_item_titleSemester);
            pageSemester = itemView.findViewById(R.id.tv_item_pageSemester);
            downloadSemester = itemView.findViewById(R.id.image_downloadSemester);
            bookSemester = itemView.findViewById(R.id.image_bookSemester);
            shareSemester = itemView.findViewById(R.id.image_shareSemester);
        }

        public void bind(DataModelSemester dataModelSemester) {

            Glide.with(itemView.getContext())
                    .load(dataModelSemester.getSemesterCover())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
                    .error(R.drawable.ic_error)
                    .into(imageCoverSemester);
            titleSemester.setText(dataModelSemester.getSemesterTitle());
            pageSemester.setText(dataModelSemester.getSemesterPage());

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_TITLE, dataModelSemester.getSemesterTitle());
                intent.putExtra(DetailActivity.EXTRA_LINK_MP3, dataModelSemester.getSemesterTitle());
                itemView.getContext().startActivity(intent);
            });

            downloadSemester.setOnClickListener(v -> {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(dataModelSemester.getSemesterLink()));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalFilesDir(v.getContext(), Environment.DIRECTORY_DOCUMENTS,dataModelSemester.getSemesterTitle());

                DownloadManager downloadManager = (DownloadManager) v.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);

                if (haveNetwork()){
                    PermissionListener permissionListener = new PermissionListener() {
                        @Override
                        public void onPermissionGranted() {
                            downloadManager.enqueue(request);
                            Snackbar.make(v,v.getResources().getString( R.string.download_document) +" "+ dataModelSemester.getSemesterTitle(), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null)
                                    .setTextColor(v.getResources().getColor(R.color.browser_actions_text_color))
                                    .setBackgroundTint(v.getResources().getColor(R.color.colorPrimary))
                                    .show();
                        }

                        @Override
                        public void onPermissionDenied(List<String> deniedPermissions) {
                            Snackbar.make(v, v.getResources().getString( R.string.denied_permission), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null)
                                    .setTextColor(v.getResources().getColor(R.color.browser_actions_text_color))
                                    .setBackgroundTint(v.getResources().getColor(R.color.colorPrimary))
                                    .show();
                        }
                    };

                    TedPermission.with(v.getContext())
                            .setPermissionListener(permissionListener)
                            .setDeniedMessage(R.string.denied_message)
                            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .check();
                }else if (!haveNetwork()){
                    Snackbar.make(v, v.getResources().getString( R.string.not_have_network) , Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .setTextColor(v.getResources().getColor(R.color.browser_actions_text_color))
                            .setBackgroundTint(v.getResources().getColor(R.color.colorPrimary))
                            .show();
                }
            });

            shareSemester.setOnClickListener(v -> {
                callback.onShareClick(dataModelSemester);
                Toast.makeText(itemView.getContext(), "Share "+dataModelSemester.getSemesterTitle(), Toast.LENGTH_SHORT).show();
            });
        }

        private boolean haveNetwork() {
            boolean haveConnection =false;
            ConnectivityManager connectivityManager = (ConnectivityManager) itemView.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo !=null && activeNetworkInfo.isConnected()){
                haveConnection=true;
            }
            return haveConnection;
        }
    }
}