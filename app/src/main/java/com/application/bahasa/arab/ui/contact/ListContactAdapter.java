package com.application.bahasa.arab.ui.contact;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.bahasa.arab.R;
import com.application.bahasa.arab.data.chats.ModelContactList;
import com.application.bahasa.arab.ui.main.MessageActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class ListContactAdapter extends RecyclerView.Adapter<ListContactAdapter.ViewHolder> {

    private Context contactContext;
    private List<ModelContactList> modelContactList;
    private boolean contactChat;

    public ListContactAdapter(Context contactContext, List<ModelContactList> modelContactList, boolean contactChat) {
        this.contactContext = contactContext;
        this.modelContactList = modelContactList;
        this.contactChat = contactChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contactContext).inflate(R.layout.items_contact,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelContactList modelContact = modelContactList.get(position);
        holder.bind(modelContact,contactContext,contactChat);

    }

    @Override
    public int getItemCount() {
        return modelContactList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imgCoverContact;
        final TextView tvTitleContact, tvPhoneNumber;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCoverContact = itemView.findViewById(R.id.imgCoverContact);
            tvTitleContact = itemView.findViewById(R.id.tvTitleContact);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
        }

        public void bind(ModelContactList modelContactList, Context contactContext, boolean contactChat) {

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(contactContext, MessageActivity.class);
                intent.putExtra("userId", modelContactList.getUserId());
                itemView.getContext().startActivity(intent);
            });

            if (modelContactList.getProfilePicture().equals("nothing")){
                Glide.with(contactContext)
                        .load(R.drawable.ic_baseline_account_circle)
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
                        .error(R.drawable.ic_error)
                        .into(imgCoverContact);
            }else {
                Glide.with(contactContext)
                        .load(modelContactList.getProfilePicture())
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
                        .error(R.drawable.ic_error)
                        .into(imgCoverContact);
            }

            tvTitleContact.setText(modelContactList.getUserName());
            tvPhoneNumber.setText(modelContactList.getPhoneNumber());
        }
    }
}
