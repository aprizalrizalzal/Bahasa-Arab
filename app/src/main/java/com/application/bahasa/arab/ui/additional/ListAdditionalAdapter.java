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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.bahasa.arab.R;
import com.application.bahasa.arab.data.DataModelAdditional;
import com.application.bahasa.arab.ui.main.DetailActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListAdditionalAdapter extends RecyclerView.Adapter<ListAdditionalAdapter.ViewHolder> {
    private final ListAdditionalFragmentCallShare callShare;

    private ArrayList<DataModelAdditional> dataModelAdditionalArrayList = new ArrayList<>();

    ListAdditionalAdapter(ListAdditionalFragmentCallShare callShare) {
        this.callShare = callShare;
    }

    public void setDataModelAdditionalArrayList(List<DataModelAdditional> dataModelAdditionals) {
        if (dataModelAdditionalArrayList == null)return;
            dataModelAdditionalArrayList.clear();
            dataModelAdditionalArrayList.addAll(dataModelAdditionals);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageCoverAdditional;
        final TextView titleAdditional, runTimeAdditional;
        final ImageButton downloadAdditional, shareAdditional;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCoverAdditional = itemView.findViewById(R.id.image_coverAdditional);
            titleAdditional = itemView.findViewById(R.id.tv_item_titleAdditional);
            runTimeAdditional = itemView.findViewById(R.id.tv_item_pageAdditional);
            downloadAdditional = itemView.findViewById(R.id.image_downloadAdditional);
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

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_TITLE, dataModelAdditional.getAdditionalTitle());
                intent.putExtra(DetailActivity.EXTRA_LINK, dataModelAdditional.getAdditionalLink());
                intent.putExtra(DetailActivity.EXTRA_OVERVIEW, dataModelAdditional.getAdditionalOverview());
                itemView.getContext().startActivity(intent);
            });

            File file = new File(String.valueOf(itemView.getContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)),dataModelAdditional.getAdditionalTitle());
            if (file.exists()){
                downloadAdditional.setVisibility(View.INVISIBLE);
            }

            downloadAdditional.setOnClickListener(v -> {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(dataModelAdditional.getAdditionalLink()));
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalFilesDir(v.getContext(), Environment.DIRECTORY_DOCUMENTS,dataModelAdditional.getAdditionalTitle());

                DownloadManager downloadManager = (DownloadManager)v.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                request.allowScanningByMediaScanner();
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);

                if (haveNetwork()){
                    PermissionListener permissionListener = new PermissionListener() {
                        @Override
                        public void onPermissionGranted() {
                            downloadManager.enqueue(request);
                            downloadAdditional.setVisibility(View.INVISIBLE);
                            Toast.makeText(v.getContext(), "Download "+dataModelAdditional.getAdditionalTitle(), Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onPermissionDenied(List<String> deniedPermissions) {
                            Toast.makeText(v.getContext(), v.getContext().getString(R.string.denied_permission) +  deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                        }
                    };
                    TedPermission.with(v.getContext())
                            .setPermissionListener(permissionListener)
                            .setDeniedMessage(R.string.denied_message)
                            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .check();

                }else if (!haveNetwork()){
                    Toast.makeText(v.getContext(),R.string.not_have_network, Toast.LENGTH_SHORT).show();
                }
            });

            shareAdditional.setOnClickListener(v -> {
                callShare.onShareClick(dataModelAdditional);
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
