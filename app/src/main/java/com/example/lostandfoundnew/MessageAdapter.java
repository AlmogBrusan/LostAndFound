package com.example.lostandfoundnew;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {


    ArrayList<Chat> mChat;
    Context mContext;
    String imageUrl;
    FirebaseUser firebaseUser;
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;


    public MessageAdapter(ArrayList<Chat> mChat, Context mContext, String imageUrl) {
        this.mChat = mChat;
        this.mContext = mContext;
        this.imageUrl = imageUrl;
    }

    public interface RecyclerCallBack {
        void onItemClicked(String userId);
    }

    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        if (i == MSG_TYPE_LEFT) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_item_left, viewGroup, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_item_right, viewGroup, false);
            return new MessageAdapter.ViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(final MessageAdapter.ViewHolder viewHolder, final int i) {
        Chat chat = mChat.get(i);
        viewHolder.showMessage.setText(chat.getMessage());
        if (!imageUrl.equals("")) {
            Glide.with(mContext).load(imageUrl).thumbnail(0.4f).into(viewHolder.profileImage);
        }


        if (i == mChat.size() - 1) {
            if (chat.isIsseen()) {
                viewHolder.seenTV.setText(mContext.getString(R.string.seen ));
            } else {
                viewHolder.seenTV.setText(mContext.getString(R.string.deliverd));
            }
        } else {
            viewHolder.seenTV.setVisibility(View.GONE);
        }
        viewHolder.timeSentTV.setText(chat.getTimesent());


    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(firebaseUser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView coverImageView;
        ImageView profileImage;
        TextView showMessage, timeSentTV, seenTV, hobbyTV, dateTV;
        CardView parentLayout;
        Button chatBtn;

        public ViewHolder(final View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.chat_item_profile_image);
            showMessage = itemView.findViewById(R.id.chat_show_message);
            timeSentTV = itemView.findViewById(R.id.sent_at);
            seenTV = itemView.findViewById(R.id.seen_txt);

        }
    }

    public void setMessages(ArrayList<Chat> mChat) {
        this.mChat = mChat;
        notifyDataSetChanged();
    }
}
