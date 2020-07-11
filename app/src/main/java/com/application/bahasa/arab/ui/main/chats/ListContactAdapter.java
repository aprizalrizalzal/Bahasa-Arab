package com.application.bahasa.arab.ui.main.chats;

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
import com.application.bahasa.arab.data.chats.DataModelProfileOrContact;
import com.application.bahasa.arab.ui.main.ChatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class ListContactAdapter extends RecyclerView.Adapter<ListContactAdapter.ViewHolder> {

    private Context context;
    private List<DataModelProfileOrContact> profileOrContactList;
    private boolean chats;

    public ListContactAdapter(Context context, List<DataModelProfileOrContact> profileOrContactList, boolean chats) {
        this.context = context;
        this.profileOrContactList = profileOrContactList;
        this.chats = chats;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_contact,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataModelProfileOrContact dataModelProfileOrContact = profileOrContactList.get(position);
        holder.bind(dataModelProfileOrContact,context,chats);

    }

    @Override
    public int getItemCount() {
        return profileOrContactList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView image_coverContact;
        final TextView tv_itemTitleContact, tv_itemPhoneNumber;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_coverContact = itemView.findViewById(R.id.image_coverContact);
            tv_itemTitleContact = itemView.findViewById(R.id.tv_item_titleContact);
            tv_itemPhoneNumber = itemView.findViewById(R.id.tv_item_phoneNumber);
        }

        public void bind(DataModelProfileOrContact dataModelProfileOrContact, Context context, boolean chats) {

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("userId",dataModelProfileOrContact.getId());
                itemView.getContext().startActivity(intent);
            });

            if (dataModelProfileOrContact.getProfilePictureInTheURL().equals("nothing")){
                Glide.with(context)
                        .load(R.drawable.ic_baseline_account_circle)
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
                        .error(R.drawable.ic_error)
                        .into(image_coverContact);
            }else {
                Glide.with(context)
                        .load(dataModelProfileOrContact.getProfilePictureInTheURL())
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
                        .error(R.drawable.ic_error)
                        .into(image_coverContact);
            }

            tv_itemTitleContact.setText(dataModelProfileOrContact.getStudentName());
            tv_itemPhoneNumber.setText(dataModelProfileOrContact.getPhoneNumber());
        }
    }
}
