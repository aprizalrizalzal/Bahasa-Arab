package com.application.bahasa.arab.ui.main.chats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.bahasa.arab.R;
import com.application.bahasa.arab.data.chats.DataModelChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ListMessageAdapter extends RecyclerView.Adapter<ListMessageAdapter.ViewHolder> {

    private static final int MSG_LEFT=0;
    private static final int MSG_RIGHT=1;
    private Context context;
    private List<DataModelChat> modelChats;

    public ListMessageAdapter(Context context, List<DataModelChat> modelChats, String imageUrl) {
        this.context = context;
        this.modelChats = modelChats;
    }

    @NonNull
    @Override
    public ListMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.message_right,parent,false);
            return new ListMessageAdapter.ViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.message_left,parent,false);
            return new ListMessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataModelChat dataModelChat = modelChats.get(position);
        holder.bind(dataModelChat);
    }

    @Override
    public int getItemCount() {
        return modelChats.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvMessage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_message);
        }

        public void bind(DataModelChat dataModelChat) {
            tvMessage.setText(dataModelChat.getMessage());
        }
    }

    @Override
    public int getItemViewType(int position) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        if (modelChats.get(position).getSender().equals(user.getUid())){
            return MSG_RIGHT;
        }else {
            return MSG_LEFT;
        }
    }
}
