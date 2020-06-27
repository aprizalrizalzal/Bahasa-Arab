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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

public class ListAdditionalAdapter extends RecyclerView.Adapter<ListAdditionalAdapter.ViewHolder> {
    private final ListAdditionalFragmentCallback callback;

    private ArrayList<DataModelAdditional> dataModelAdditionalArrayList = new ArrayList<>();

    ListAdditionalAdapter(ListAdditionalFragmentCallback callback) {
        this.callback = callback;
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
        final TextView titleAdditional;
        final TextView runTimeAdditional;
        final ImageButton shareAdditional;
        final AdView adViewAdditional;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCoverAdditional = itemView.findViewById(R.id.image_coverAdditional);
            titleAdditional = itemView.findViewById(R.id.tv_item_titleAdditional);
            runTimeAdditional = itemView.findViewById(R.id.tv_item_pageAdditional);
            shareAdditional = itemView.findViewById(R.id.image_shareAdditional);
            adViewAdditional = itemView.findViewById(R.id.adViewAdditional);
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
                intent.putExtra(DetailActivity.EXTRA_OVERVIEW,dataModelAdditional.getAdditionalOverview());
                itemView.getContext().startActivity(intent);
            });

            shareAdditional.setOnClickListener(v -> callback.onShareClick(dataModelAdditional));
            AdRequest adRequest = new AdRequest.Builder().build();
            adViewAdditional.loadAd(adRequest);
        }
    }
}
