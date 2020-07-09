package com.application.bahasa.arab.ui.additional;

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
import com.application.bahasa.arab.data.home.DataModelAdditional;
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

public class ListAdditionalAdapter extends RecyclerView.Adapter<ListAdditionalAdapter.ViewHolder> implements Filterable {

    private final ListAdditionalFragmentCallback callShare;
    private ArrayList<DataModelAdditional> dataModelAdditionalArrayList = new ArrayList<>();
    private ArrayList<DataModelAdditional> getDataModelAdditionalArrayList = new ArrayList<>();

    ListAdditionalAdapter(ListAdditionalFragmentCallback callShare) {
        this.callShare = callShare;
    }

    public void setDataModelAdditionalArrayList(List<DataModelAdditional> dataModelAdditionals) {
        if (dataModelAdditionalArrayList == null)return;
        dataModelAdditionalArrayList.clear();
        dataModelAdditionalArrayList.addAll(dataModelAdditionals);

        if (getDataModelAdditionalArrayList == null)return;
        getDataModelAdditionalArrayList.clear();
        getDataModelAdditionalArrayList.addAll(dataModelAdditionals);
    }

    @NonNull
    @Override
    public ListAdditionalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_additional,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataModelAdditional dataModelAdditional = dataModelAdditionalArrayList.get(position);
        holder.bind(dataModelAdditional);
    }

    @Override
    public int getItemCount() {
        return dataModelAdditionalArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
        }

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                ArrayList<DataModelAdditional> filterData = new ArrayList<>();
                if (charSequence.toString().isEmpty()){
                    filterData.addAll(getDataModelAdditionalArrayList);
                }else {
                    for (DataModelAdditional additional : getDataModelAdditionalArrayList){
                        if (additional.getAdditionalTitle().contains(charSequence.toString())){
                            filterData.add(additional);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filterData;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            dataModelAdditionalArrayList.clear();
            dataModelAdditionalArrayList.addAll((Collection<? extends DataModelAdditional>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageCoverAdditional;
        final TextView titleAdditional, runTimeAdditional;
        final ImageButton downloadAdditional, bookAdditional, shareAdditional;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCoverAdditional = itemView.findViewById(R.id.image_coverAdditional);
            titleAdditional = itemView.findViewById(R.id.tv_item_titleAdditional);
            runTimeAdditional = itemView.findViewById(R.id.tv_item_pageAdditional);
            downloadAdditional = itemView.findViewById(R.id.image_downloadAdditional);
            bookAdditional = itemView.findViewById(R.id.image_bookAdditional);
            shareAdditional = itemView.findViewById(R.id.image_shareAdditional);
        }

        public void bind(DataModelAdditional dataModelAdditional) {

            Glide.with(itemView.getContext())
                    .load(dataModelAdditional.getAdditionalCover())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
                    .error(R.drawable.ic_error)
                    .into(imageCoverAdditional);
            titleAdditional.setText(dataModelAdditional.getAdditionalTitle());
            runTimeAdditional.setText(dataModelAdditional.getAdditionalPage());

            File file = new File(itemView.getContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),dataModelAdditional.getAdditionalTitle());
            if (!file.exists()){
                itemView.setOnClickListener(v -> Snackbar.make(v,v.getResources().getString( R.string.document_is_empty), Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setTextColor(v.getResources().getColor(R.color.browser_actions_text_color))
                        .setBackgroundTint(v.getResources().getColor(R.color.colorPrimary))
                        .show());
            }else {
                downloadAdditional.setVisibility(View.INVISIBLE);
                bookAdditional.setVisibility(View.VISIBLE);
                itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(itemView.getContext(), DetailHomeActivity.class);
                    intent.putExtra(DetailHomeActivity.EXTRA_TITLE, dataModelAdditional.getAdditionalTitle());
                    intent.putExtra(DetailHomeActivity.EXTRA_LINK_MP3, dataModelAdditional.getAdditionalLinkMp3());
                    itemView.getContext().startActivity(intent);
                });
            }

            downloadAdditional.setOnClickListener(v -> {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(dataModelAdditional.getAdditionalLink()));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalFilesDir(v.getContext(),Environment.DIRECTORY_DOCUMENTS,dataModelAdditional.getAdditionalTitle());
                DownloadManager downloadManager = (DownloadManager)v.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);

                if (haveNetwork()){
                    PermissionListener permissionListener = new PermissionListener() {
                        @Override
                        public void onPermissionGranted() {
                            downloadManager.enqueue(request);
                            downloadAdditional.setVisibility(View.INVISIBLE);
                            bookAdditional.setVisibility(View.VISIBLE);
                            Snackbar.make(v,v.getResources().getString( R.string.download_document) +" "+ dataModelAdditional.getAdditionalTitle(), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null)
                                    .setTextColor(v.getResources().getColor(R.color.browser_actions_text_color))
                                    .setBackgroundTint(v.getResources().getColor(R.color.colorPrimary))
                                    .show();
                            if (bookAdditional.isShown()){
                                itemView.setOnClickListener(v -> {
                                    Intent intent = new Intent(itemView.getContext(), DetailHomeActivity.class);
                                    intent.putExtra(DetailHomeActivity.EXTRA_TITLE, dataModelAdditional.getAdditionalTitle());
                                    intent.putExtra(DetailHomeActivity.EXTRA_LINK_MP3, dataModelAdditional.getAdditionalLinkMp3());
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

            shareAdditional.setOnClickListener(v -> callShare.onShareClick(dataModelAdditional));
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
