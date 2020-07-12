package com.application.bahasa.arab.ui.chats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.bahasa.arab.R;
import com.application.bahasa.arab.data.chats.ModelChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ListChatAdapter extends RecyclerView.Adapter<ListChatAdapter.ViewHolder> {

    private static final int MSG_LEFT=0;
    private static final int MSG_RIGHT=1;
    private Context chatContext;
    private List<ModelChat> modelChat;

    public ListChatAdapter(Context chatContext, List<ModelChat> modelChat) {
        this.chatContext = chatContext;
        this.modelChat = modelChat;
    }

    @NonNull
    @Override
    public ListChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_RIGHT){
            View view = LayoutInflater.from(chatContext).inflate(R.layout.message_right,parent,false);
            return new ListChatAdapter.ViewHolder(view);
        }else {
            View view = LayoutInflater.from(chatContext).inflate(R.layout.message_left,parent,false);
            return new ListChatAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelChat chat = modelChat.get(position);
        holder.bind(chat);
    }

    @Override
    public int getItemCount() {
        return modelChat.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvMessage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
        }

        public void bind(ModelChat modelChat) {
            tvMessage.setText(modelChat.getMessage());
        }
    }

    @Override
    public int getItemViewType(int position) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        if (modelChat.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_RIGHT;
        }else {
            return MSG_LEFT;
        }
    }
}
