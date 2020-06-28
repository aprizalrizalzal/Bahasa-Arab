package com.application.bahasa.arab.ui.unit;

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
import com.application.bahasa.arab.data.DataModelUnit;
import com.application.bahasa.arab.ui.main.DetailActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class ListUnitAdapter extends RecyclerView.Adapter<ListUnitAdapter.ViewHolder> {

    private final ListUnitFragmentCallback callback;
    private ArrayList<DataModelUnit> dataModelUnitArrayList = new ArrayList<>();

    ListUnitAdapter(ListUnitFragmentCallback callback) {
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
        final TextView titleUnit;
        final TextView pageUnit;
        final ImageButton shareUnit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCoverUnit = itemView.findViewById(R.id.image_coverUnit);
            titleUnit = itemView.findViewById(R.id.tv_item_titleUnit);
            pageUnit = itemView.findViewById(R.id.tv_item_pageUnit);
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
                intent.putExtra(DetailActivity.EXTRA_OVERVIEW,dataModelUnit.getUnitOverview());
                itemView.getContext().startActivity(intent);
            });

            shareUnit.setOnClickListener(v -> callback.onShareClick(dataModelUnit));
        }
    }
}
