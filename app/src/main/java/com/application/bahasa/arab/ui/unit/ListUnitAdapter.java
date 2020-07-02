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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.bahasa.arab.R;
import com.application.bahasa.arab.data.DataModelUnit;
import com.application.bahasa.arab.ui.main.DetailActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListUnitAdapter extends RecyclerView.Adapter<ListUnitAdapter.ViewHolder> {

    private final ListUnitFragmentCallShare callback;

    private ArrayList<DataModelUnit> dataModelUnitArrayList = new ArrayList<>();

    ListUnitAdapter(ListUnitFragmentCallShare callback) {
        this.callback = callback;
    }

    public void setDataModelUnitArrayList(List<DataModelUnit> dataModelUnit) {
        if (dataModelUnitArrayList == null)return;
            dataModelUnitArrayList.clear();
            dataModelUnitArrayList.addAll(dataModelUnit);
    }

    @NonNull
    @Override
    public ListUnitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_unit,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataModelUnit dataModelUnit = dataModelUnitArrayList.get(position);
        holder.bind(dataModelUnit);
    }

    @Override
    public int getItemCount() {
        return dataModelUnitArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageCoverUnit;
        final TextView titleUnit,pageUnit;
        final ImageButton downloadUnit,shareUnit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCoverUnit = itemView.findViewById(R.id.image_coverUnit);
            titleUnit = itemView.findViewById(R.id.tv_item_titleUnit);
            pageUnit = itemView.findViewById(R.id.tv_item_pageUnit);
            downloadUnit = itemView.findViewById(R.id.image_downloadUnit);
            shareUnit = itemView.findViewById(R.id.image_shareUnit);

        }

        public void bind(DataModelUnit dataModelUnit) {

            Glide.with(itemView.getContext())
                    .load(dataModelUnit.getUnitCover())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
                    .error(R.drawable.ic_error)
                    .into(imageCoverUnit);
            titleUnit.setText(dataModelUnit.getUnitTitle());
            pageUnit.setText(dataModelUnit.getUnitPage());

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_TITLE, dataModelUnit.getUnitTitle());
                itemView.getContext().startActivity(intent);
            });

            File file = new File(String.valueOf(itemView.getContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)),dataModelUnit.getUnitTitle());
            if (file.exists()){
                downloadUnit.setVisibility(View.INVISIBLE);
            }

            downloadUnit.setOnClickListener(v -> {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(dataModelUnit.getUnitLink()));
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalFilesDir(v.getContext(), Environment.DIRECTORY_DOCUMENTS,dataModelUnit.getUnitTitle());
                DownloadManager downloadManager = (DownloadManager)v.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                request.allowScanningByMediaScanner();
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
                if (haveNetwork()){
                    PermissionListener permissionListener = new PermissionListener() {
                        @Override
                        public void onPermissionGranted() {
                            downloadManager.enqueue(request);
                            downloadUnit.setVisibility(View.INVISIBLE);
                            Toast.makeText(v.getContext(),v.getContext().getString(R.string.download_document)+dataModelUnit.getUnitTitle(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onPermissionDenied(List<String> deniedPermissions) {
                            Toast.makeText(v.getContext(),v.getContext().getString(R.string.denied_permission), Toast.LENGTH_SHORT).show();
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
            shareUnit.setOnClickListener(v -> {
                callback.onShareClick(dataModelUnit);
                Toast.makeText(itemView.getContext(), "Share "+dataModelUnit.getUnitTitle(), Toast.LENGTH_SHORT).show();
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
