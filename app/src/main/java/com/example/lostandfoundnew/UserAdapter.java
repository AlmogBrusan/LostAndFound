package com.example.lostandfoundnew;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {


    private ArrayList<UserProfileModel> mUserProfiles;
    private Context mContext;
    private boolean isChat;
    private String theLastMsg;
    private HashMap<String,Integer> unreadMsgMap;
    private String thetime;

    public interface RecyclerCallBack {
        void ChatFragmentOnItemClicked(String userId);
    }

    private UserAdapter.RecyclerCallBack recyclerCallBack;

    public UserAdapter(ArrayList<UserProfileModel> mUserProfiles, Context mContext, boolean isChat, HashMap<String,Integer> unreadMsgMap) {
        this.mUserProfiles = mUserProfiles;
        this.mContext = mContext;
        this.isChat = isChat;
        this.unreadMsgMap = unreadMsgMap;
        try {
            recyclerCallBack = (RecyclerCallBack) mContext;

        } catch (ClassCastException ex) {
            throw new ClassCastException(mContext.toString() + "must implement interface");
        }


    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, viewGroup, false);

        return new UserAdapter.ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull final UserAdapter.ViewHolder viewHolder, final int i) {

        final UserProfileModel userProfile = mUserProfiles.get(i);



        viewHolder.userName.setText(userProfile.getFirstName() +" " + userProfile.getLastName());
        if (!userProfile.getImage().equals("")) {
            Glide.with(mContext).load(userProfile.getImage()).thumbnail(0.2f).into(viewHolder.profileImage);
        }

        if (isChat){
            lastMsg(userProfile.getUserId(),viewHolder.lastMsg,viewHolder.timeOfLastMsg,viewHolder.numOfUnread,viewHolder.unreadHolder);
        }
        else {
            viewHolder.lastMsg.setVisibility(View.GONE);
        }

//        if (isChat) {
//            if (userProfile.getStatus().equals(mContext.getString(R.string.online))) {
//                viewHolder.imgOn.setVisibility(View.VISIBLE);
//                viewHolder.imgOff.setVisibility(View.GONE);
//            } else {
//                viewHolder.imgOn.setVisibility(View.GONE);
//                viewHolder.imgOff.setVisibility(View.VISIBLE);
//            }
//        } else {
//            viewHolder.imgOn.setVisibility(View.GONE);
//            viewHolder.imgOff.setVisibility(View.GONE);
//        }
        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerCallBack.ChatFragmentOnItemClicked(userProfile.getUserId());
            }
        });

    }


    @Override
    public int getItemCount() {
        return mUserProfiles.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImage;
        TextView userName;
        RelativeLayout unreadHolder;
        RelativeLayout relativeLayout;
        ImageView imgOn;
        ImageView imgOff;
        TextView lastMsg;
        TextView numOfUnread;
        TextView timeOfLastMsg;


        public ViewHolder(final View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profile_image);
            userName = itemView.findViewById(R.id.user_name);
            unreadHolder = itemView.findViewById(R.id.num_of_unread_holder);
            relativeLayout = itemView.findViewById(R.id.user_item_layout);
            imgOn = itemView.findViewById(R.id.img_online);
            imgOff = itemView.findViewById(R.id.img_offline);
            timeOfLastMsg = itemView.findViewById(R.id.last_msg_time);
            lastMsg = itemView.findViewById(R.id.last_msg);
            numOfUnread = itemView.findViewById(R.id.num_of_unread);

        }
    }

    public void setMessages(ArrayList<UserProfile> mChat) {
        this.mUserProfiles = mUserProfiles;
        notifyDataSetChanged();
    }

    private void lastMsg(final String userId, final TextView lastMsg , final TextView lastMsgTime , final TextView numOfUnread , final RelativeLayout holder) {
        theLastMsg = "";
        thetime = "";
        final int[] unread = {0};
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReciver().equals(firebaseUser.getUid())&& chat.getSender().equals(userId)){
                        if(!chat.isIsseen()){
                            unread[0]++;
                        }
                    }
                    if (chat.getReciver().equals(firebaseUser.getUid()) && chat.getSender().equals(userId) ||
                            chat.getReciver().equals(userId) && chat.getSender().equals(firebaseUser.getUid())) {
                        theLastMsg = chat.getMessage();
                        thetime= chat.getTimesent();
                    }
                }
                switch (theLastMsg) {
                    case "":
                        lastMsg.setText("");
                        break;
                    default:
                        lastMsg.setText(theLastMsg);
                        lastMsgTime.setText(thetime);
                }
                theLastMsg = "";
                if (unread[0]!=0){
                    holder.setVisibility(View.VISIBLE);
                    numOfUnread.setText(String.valueOf(unread[0]));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
