package com.application.bahasa.arab.ui.additional;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.bahasa.arab.R;
import com.application.bahasa.arab.data.DataModelAdditional;
import com.application.bahasa.arab.ui.main.DetailActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class ListAdditionalAdapter extends RecyclerView.Adapter<ListAdditionalAdapter.ViewHolder> {
    private final ListAdditionalFragmentCallShare callShare;
    private final ListAdditionalFragmentCallDownload callDownload;

    private ArrayList<DataModelAdditional> dataModelAdditionalArrayList = new ArrayList<>();

    ListAdditionalAdapter(ListAdditionalFragmentCallShare callShare, ListAdditionalFragmentCallDownload callDownload) {
        this.callShare = callShare;
        this.callDownload = callDownload;
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
        final ImageButton downloadAdditional,shareAdditional;
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
                intent.putExtra(DetailActivity.EXTRA_OVERVIEW,dataModelAdditional.getAdditionalOverview());
                itemView.getContext().startActivity(intent);
            });

            downloadAdditional.setOnClickListener(v -> callDownload.onDownloadClick(dataModelAdditional));
            shareAdditional.setOnClickListener(v -> callShare.onShareClick(dataModelAdditional));
        }
    }
}
