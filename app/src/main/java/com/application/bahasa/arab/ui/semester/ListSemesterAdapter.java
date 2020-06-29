package com.application.bahasa.arab.ui.semester;

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
import com.application.bahasa.arab.data.DataModelSemester;
import com.application.bahasa.arab.ui.main.DetailActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class ListSemesterAdapter extends RecyclerView.Adapter<ListSemesterAdapter.ViewHolder>{

    private final ListSemesterFragmentCallback callback;
    private ArrayList<DataModelSemester> dataModelSemesterArrayList = new ArrayList<>();

    ListSemesterAdapter(ListSemesterFragmentCallback callback) {
        this.callback = callback;
    }

    public void setDataModelSemesterArrayList(List<DataModelSemester> dataModelSemesters) {
        if (dataModelSemesterArrayList == null)return;
            dataModelSemesterArrayList.clear();
            dataModelSemesterArrayList.addAll(dataModelSemesters);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageCoverSemester;
        final TextView titleSemester;
        final TextView pageSemester;
        final ImageButton shareSemester;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCoverSemester = itemView.findViewById(R.id.image_coverSemester);
            titleSemester = itemView.findViewById(R.id.tv_item_titleSemester);
            pageSemester = itemView.findViewById(R.id.tv_item_pageSemester);
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
                intent.putExtra(DetailActivity.EXTRA_OVERVIEW, dataModelSemester.getSemesterOverview());
                itemView.getContext().startActivity(intent);
            });

            shareSemester.setOnClickListener(v -> callback.onShareClick(dataModelSemester));
        }
    }
}
