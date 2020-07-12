package com.application.bahasa.arab.ui.unit;

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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.bahasa.arab.R;
import com.application.bahasa.arab.data.home.ModelUnit;
import com.application.bahasa.arab.ui.main.DetailHomeActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListUnitAdapter extends RecyclerView.Adapter<ListUnitAdapter.ViewHolder> implements Filterable {

    private final ListUnitFragmentCallback callback;
    private ArrayList<ModelUnit> modelUnitArrayList = new ArrayList<>();
    private ArrayList<ModelUnit> getModelUnitArrayList = new ArrayList<>();


    ListUnitAdapter(ListUnitFragmentCallback callback) {
        this.callback = callback;
    }

    public void setModelUnitArrayList(List<ModelUnit> modelUnit) {
        if (modelUnitArrayList == null)return;
        modelUnitArrayList.clear();
        modelUnitArrayList.addAll(modelUnit);

        if (getModelUnitArrayList == null)return;
        getModelUnitArrayList.clear();
        getModelUnitArrayList.addAll(modelUnit);
    }

    @NonNull
    @Override
    public ListUnitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_unit,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelUnit modelUnit = modelUnitArrayList.get(position);
        holder.bind(modelUnit);
    }

    @Override
    public int getItemCount() {
        return modelUnitArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<ModelUnit> filterData = new ArrayList<>();
            if (charSequence.toString().isEmpty()){
                filterData.addAll(getModelUnitArrayList);
            }else {
                for (ModelUnit unit : getModelUnitArrayList){
                    if (unit.getUnitTitle().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filterData.add(unit);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filterData;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            modelUnitArrayList.clear();
            modelUnitArrayList.addAll((Collection<? extends ModelUnit>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageCoverUnit;
        final TextView titleUnit,pageUnit;
        final ImageButton downloadUnit, bookUnit,shareUnit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCoverUnit = itemView.findViewById(R.id.image_coverUnit);
            titleUnit = itemView.findViewById(R.id.tv_item_titleUnit);
            pageUnit = itemView.findViewById(R.id.tv_item_pageUnit);
            downloadUnit = itemView.findViewById(R.id.image_downloadUnit);
            bookUnit = itemView.findViewById(R.id.image_bookUnit);
            shareUnit = itemView.findViewById(R.id.image_shareUnit);

        }

        public void bind(ModelUnit modelUnit) {
            Glide.with(itemView.getContext())
                    .load(modelUnit.getUnitCover())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
                    .error(R.drawable.ic_error)
                    .into(imageCoverUnit);
            titleUnit.setText(modelUnit.getUnitTitle());
            pageUnit.setText(modelUnit.getUnitPage());

            File file = new File(itemView.getContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), modelUnit.getUnitTitle());
            if (!file.exists()){
                itemView.setOnClickListener(v -> Snackbar.make(v,v.getResources().getString( R.string.document_is_empty), Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setTextColor(v.getResources().getColor(R.color.browser_actions_text_color))
                        .setBackgroundTint(v.getResources().getColor(R.color.colorPrimary))
                        .show());
            }else {
                downloadUnit.setVisibility(View.INVISIBLE);
                bookUnit.setVisibility(View.VISIBLE);
                itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(itemView.getContext(), DetailHomeActivity.class);
                    intent.putExtra(DetailHomeActivity.EXTRA_TITLE, modelUnit.getUnitTitle());
                    intent.putExtra(DetailHomeActivity.EXTRA_LINK_MP3, modelUnit.getUnitLinkMp3());
                    itemView.getContext().startActivity(intent);
                });
            }

            downloadUnit.setOnClickListener(v -> {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(modelUnit.getUnitLink()));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalFilesDir(v.getContext(), Environment.DIRECTORY_DOCUMENTS, modelUnit.getUnitTitle());

                DownloadManager downloadManager = (DownloadManager)v.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);

                if (haveNetwork()){
                    PermissionListener permissionListener = new PermissionListener() {
                        @Override
                        public void onPermissionGranted() {
                            downloadManager.enqueue(request);
                            downloadUnit.setVisibility(View.INVISIBLE);
                            bookUnit.setVisibility(View.VISIBLE);
                            Snackbar.make(v,v.getResources().getString( R.string.download_document) +" "+ modelUnit.getUnitTitle(), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null)
                                    .setTextColor(v.getResources().getColor(R.color.browser_actions_text_color))
                                    .setBackgroundTint(v.getResources().getColor(R.color.colorPrimary))
                                    .show();
                            if (bookUnit.isShown()){
                                itemView.setOnClickListener(v -> {
                                    Intent intent = new Intent(itemView.getContext(), DetailHomeActivity.class);
                                    intent.putExtra(DetailHomeActivity.EXTRA_TITLE, modelUnit.getUnitTitle());
                                    intent.putExtra(DetailHomeActivity.EXTRA_LINK_MP3, modelUnit.getUnitLinkMp3());
                                    itemView.getContext().startActivity(intent);
                                });
                            }
                        }

                        @Override
                        public void onPermissionDenied(List<String> deniedPermissions) {
                            Snackbar.make(v, v.getResources().getString( R.string.deniedPermission) , Snackbar.LENGTH_LONG)
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
                    Snackbar.make(v, v.getResources().getString( R.string.not_have_network), Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .setTextColor(v.getResources().getColor(R.color.browser_actions_text_color))
                            .setBackgroundTint(v.getResources().getColor(R.color.colorPrimary))
                            .show();
                }
            });
            shareUnit.setOnClickListener(v -> {
                callback.onShareClick(modelUnit);
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
