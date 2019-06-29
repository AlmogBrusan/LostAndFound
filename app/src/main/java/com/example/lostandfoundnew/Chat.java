package com.example.lostandfoundnew;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import de.hdodenhof.circleimageview.CircleImageView;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.ocpsoft.prettytime.PrettyTime;

public class Chat extends AppCompatActivity {
    private static final int TOTAL_ITEM_TO_LOAD = 10;

    TextView OnlineStatus;
    String chatflag = "";
    ChildEventListener childEventListener;
    String cruntuser;
    DatabaseReference databaseReference;
    String image;
    String imageprofile;
    EditText input;
    private int itempos = 0;
    private String lastkeyonetime = "";
    String lastonline;
    DatabaseReference mdatabaseref_notification;
//    private MessageListAdapter messageAdapter;
//    List<ChatMessage> messageList = new ArrayList();
    private String mlastkey = "";
    private LinearLayoutManager mlinearlayout;
//    Model_User_profile model_user_profile = new Model_User_profile();
    SwipeRefreshLayout mrefreashlayout;
    int notificationflag = 0;
    String online = "3";
    String post_id;
    private String prevkey = "";
    private String prevkeyonetime = "";
    Query query;
    Query query_readstarus;
    String readStatus = "false";
    String reciver_id;
    RecyclerView recyclerView;
//    Sender_Model sender_model;
//    Sender_Model sender_model1;
    int status;
    private int swipecount = 0;
    String timestamp;
    String title;
    DatabaseReference useronline;
    DatabaseReference userprofileref;
    DatabaseReference userref;

//    public static String ConvertTimeStampintoAgo(Long paramLong) {
//        try {
//            Calendar calendar = Calendar.getInstance(Locale.getDefault());
//            calendar.setTimeInMillis(paramLong.longValue());
//            String str = DateFormat.format("yyyy-MM-dd HH:mm:ss", calendar).toString();
//            Date date = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())).parse(str);
//            return (new PrettyTime()).format(date);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "";
//        }
//    }
//
//    private void checkstatus() { FirebaseDatabase.getInstance().getReference("onlineuser").child(this.reciver_id).child("conection").addValueEventListener(new ValueEventListener() {
//        public void onCancelled(DatabaseError param1DatabaseError) {}
//
//        public void onDataChange(DataSnapshot param1DataSnapshot) {
//            lastonline = param1DataSnapshot.getValue().toString();
//            if (lastonline.equals("true")) {
//                OnlineStatus.setText("Online");
//                return;
//            }
//            OnlineStatus.setText("last seen " + Chat.ConvertTimeStampintoAgo(Long.valueOf(Long.parseLong(lastonline))));
//        }
//    }); }
//
//    public static Bitmap decodeBase64(String paramString) {
//        byte[] arrayOfByte = Base64.decode(paramString, 0);
//        return BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length);
//    }
//
//    public static String encodeTobase64(Bitmap paramBitmap) {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        paramBitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
//        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
//    }
//
//    private void loaddata() {
//        this.query = FirebaseDatabase.getInstance().getReference("chat").child(this.cruntuser).child(this.post_id).child(this.reciver_id).limitToLast(13);
//        this.childEventListener = new ChildEventListener() {
//            public void onCancelled(DatabaseError param1DatabaseError) {}
//
//            public void onChildAdded(DataSnapshot param1DataSnapshot, String param1String) {
//                ChatMessage chatMessage = (ChatMessage)param1DataSnapshot.getValue(ChatMessage.class);
//                Chat.access$508(Chat.this);
//                if (itempos == 1) {
//                    String str = param1DataSnapshot.getKey();
//                    Chat.access$202(Chat.this, str);
//                    Chat.access$402(Chat.this, str);
//                }
//                messageList.add(chatMessage);
//                messageAdapter.notifyDataSetChanged();
//                recyclerView.smoothScrollToPosition(messageList.size() - 1);
//                recyclerView.scrollToPosition(messageList.size() - 1);
//                mrefreashlayout.setRefreshing(false);
//            }
//
//            public void onChildChanged(DataSnapshot param1DataSnapshot, String param1String) {}
//
//            public void onChildMoved(DataSnapshot param1DataSnapshot, String param1String) {}
//
//            public void onChildRemoved(DataSnapshot param1DataSnapshot) {}
//        };
//        this.query.addChildEventListener(this.childEventListener);
//    }
//
//    private void loadmoredata() { FirebaseDatabase.getInstance().getReference("chat").child(this.cruntuser).child(this.post_id).child(this.reciver_id).orderByKey().endAt(this.lastkeyonetime).limitToLast(13).addChildEventListener(new ChildEventListener() {
//        public void onCancelled(DatabaseError param1DatabaseError) {}
//
//        public void onChildAdded(DataSnapshot param1DataSnapshot, String param1String) {
//            ChatMessage chatMessage = (ChatMessage)param1DataSnapshot.getValue(ChatMessage.class);
//            String str = param1DataSnapshot.getKey();
//            if (!prevkeyonetime.equals(str))
//                messageList.add(Chat.access$508(Chat.this), chatMessage);
//            if (itempos == 1)
//                Chat.access$102(Chat.this, str);
//            if (prevkeyonetime.equals(str))
//                Chat.access$302(Chat.this, lastkeyonetime);
//            messageAdapter.notifyDataSetChanged();
//            mrefreashlayout.setRefreshing(false);
//            mlinearlayout.scrollToPositionWithOffset(10, 0);
//        }
//
//        public void onChildChanged(DataSnapshot param1DataSnapshot, String param1String) {}
//
//        public void onChildMoved(DataSnapshot param1DataSnapshot, String param1String) {}
//
//        public void onChildRemoved(DataSnapshot param1DataSnapshot) {}
//    }); }
//
//    public void onBackPressed() {
//        super.onBackPressed();
//        HashMap hashMap = new HashMap();
//        hashMap.put("online", "0");
//        if (!this.chatflag.equals(Integer.valueOf(0)) && this.chatflag.equals(Integer.valueOf(1))) {
//            this.useronline.child(this.cruntuser).child(this.post_id).setValue(hashMap);
//            hashMap = new HashMap();
//            hashMap.put("readstatus", "true");
//            this.userref.child(this.cruntuser).child(this.post_id).child(this.reciver_id).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                public void onComplete(@NonNull Task<Void> param1Task) {}
//            });
//            return;
//        }
//    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_chat);
        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.fab);
//        Bundle bundle = getIntent().getExtras();
//        this.reciver_id = (String)bundle.get("id");
//        this.image = (String)bundle.get("image");
//        this.status = bundle.getInt("status");
//        this.post_id = (String)bundle.get("push_id");
//        this.title = (String)bundle.get("title");
//        this.timestamp = (String)bundle.get("time_stamp");
//        this.chatflag = bundle.getString("chatflag");
//        this.chatflag = bundle.getString("chatflag");
//        getSupportActionBar().setDisplayOptions(16);
//        getSupportActionBar().setDisplayShowCustomEnabled(true);
//        getSupportActionBar().setCustomView(2131492891);
//        View view = getSupportActionBar().getCustomView();
//        final CircleImageView civActionbar = (CircleImageView)view.findViewById(R.id.chatuserimage);
//        final TextView txtusername = (TextView)view.findViewById(R.id.chatusername);
//        ImageButton imageButton = (ImageButton)view.findViewById(R.id.btnchatback);
//        this.OnlineStatus = (TextView)view.findViewById(R.id.txtlastseen);
//        this.input = (EditText)findViewById(R.id.input);
//        checkstatus();
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        this.userref = FirebaseDatabase.getInstance().getReference("user");
//        this.cruntuser = firebaseUser.getUid();
//        this.mdatabaseref_notification = FirebaseDatabase.getInstance().getReference("notifications");
//        this.useronline = FirebaseDatabase.getInstance().getReference("online");
//        this.query_readstarus = this.useronline.child(this.reciver_id).child(this.post_id);
//        this.mlinearlayout = new LinearLayoutManager(this);
//        this.userprofileref = FirebaseDatabase.getInstance().getReference("userprofile").child(this.reciver_id);
//        this.userprofileref.addListenerForSingleValueEvent(new ValueEventListener() {
//            public void onCancelled(DatabaseError param1DatabaseError) {}
//
//            @SuppressLint({"SetTextI18n"})
//            public void onDataChange(DataSnapshot param1DataSnapshot) {
//                model_user_profile.setEmail(((Model_User_profile)param1DataSnapshot.getValue(Model_User_profile.class)).getEmail());
//                txtusername.setText(((Model_User_profile)param1DataSnapshot.getValue(Model_User_profile.class)).getFirstname() + " " + ((Model_User_profile)param1DataSnapshot.getValue(Model_User_profile.class)).getLastname());
//                model_user_profile.setPhonenumber(((Model_User_profile)param1DataSnapshot.getValue(Model_User_profile.class)).getPhonenumber());
//                imageprofile = ((Model_User_profile)param1DataSnapshot.getValue(Model_User_profile.class)).getImage();
//                if (imageprofile.equals("no")) {
//                    civActionbar.setImageBitmap(BitmapFactory.decodeResource(getBaseContext().getResources(), 2131623939));
//                    return;
//                }
//                civActionbar.setImageBitmap(Chat.decodeBase64(imageprofile));
//            }
//        });
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View param1View) {
//                if (Build.VERSION.SDK_INT >= 21)
//                    finishAfterTransition();
//            }
//        });
//        textView.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View param1View) {
//                Intent intent = new Intent(getBaseContext(), user_profile.class);
//                intent.putExtra("rec_id", reciver_id);
//                intent.putExtra("image", imageprofile);
//                intent.putExtra("email", model_user_profile.getEmail());
//                intent.putExtra("phone", model_user_profile.getPhonenumber());
//                intent.putExtra("displayname", txtusername.getText());
//                startActivity(intent);
//            }
//        });
//        circleImageView.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = 21)
//            public void onClick(View param1View) {
//                Intent intent = new Intent(getBaseContext(), UserImage.class);
//                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(Chat.this, new Pair[] { Pair.create(civActionbar, "imageprofile") });
//                intent.putExtra("image", Chat.encodeTobase64(((BitmapDrawable)civActionbar.getDrawable()).getBitmap()));
//                startActivity(intent, activityOptions.toBundle());
//            }
//        });
//        this.databaseReference = FirebaseDatabase.getInstance().getReference("chat").child(this.cruntuser).child(this.post_id).child(this.reciver_id);
//        final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("user").child(this.cruntuser);
//        this.recyclerView = (RecyclerView)findViewById(2131296656);
//        this.mrefreashlayout = (SwipeRefreshLayout)findViewById(2131296380);
//        this.messageAdapter = new MessageListAdapter(this, this.messageList);
//        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        this.recyclerView.setAdapter(this.messageAdapter);
//        final String time_stamp = this.userref.push().getKey();
//        loaddata();
//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View param1View) {
//                sender_model = new Sender_Model(image, post_id, title, time_stamp, reciver_id, input.getText().toString(), "true");
//                sender_model1 = new Sender_Model(image, post_id, title, time_stamp, cruntuser, input.getText().toString(), readStatus);
//                dbref.addListenerForSingleValueEvent(new ValueEventListener() {
//                    public void onCancelled(DatabaseError param2DatabaseError) {}
//
//                    public void onDataChange(DataSnapshot param2DataSnapshot) {
//                        if (param2DataSnapshot.hasChild(post_id)) {
//                            userref.child(reciver_id).child(post_id).child(cruntuser).setValue(sender_model1);
//                            userref.child(cruntuser).child(post_id).child(reciver_id).setValue(sender_model);
//                            return;
//                        }
//                        userref.child(reciver_id).child(post_id).child(cruntuser).setValue(sender_model1);
//                        userref.child(cruntuser).child(post_id).child(reciver_id).setValue(sender_model);
//                    }
//                });
//                FirebaseDatabase.getInstance().getReference("chat").child(cruntuser).child(post_id).child(reciver_id).push().setValue(new ChatMessage(input.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid()));
//                FirebaseDatabase.getInstance().getReference("chat").child(reciver_id).child(post_id).child(cruntuser).push().setValue(new ChatMessage(input.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid()));
//                if (notificationflag != 1 && notificationflag == 0) {
//                    HashMap hashMap = new HashMap();
//                    hashMap.put("from", cruntuser);
//                    hashMap.put("type", "new message request");
//                    mdatabaseref_notification.child(reciver_id).push().setValue(hashMap);
//                }
//                input.setText("");
//            }
//        });
//        this.mrefreashlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            public void onRefresh() {
//                if (swipecount == 0) {
//                    Chat.access$102(Chat.this, mlastkey);
//                    Chat.access$302(Chat.this, prevkey);
//                    Chat.access$008(Chat.this);
//                }
//                Chat.access$502(Chat.this, 0);
//                loadmoredata();
//            }
//        });
    }

//    protected void onStart() {
//        super.onStart();
//        HashMap hashMap = new HashMap();
//        hashMap.put("online", "1");
//        this.useronline.child(this.cruntuser).child(this.post_id).setValue(hashMap);
//        if (!this.chatflag.equals("0") && this.chatflag.equals("1")) {
//            hashMap = new HashMap();
//            hashMap.put("readstatus", "true");
//            this.userref.child(this.cruntuser).child(this.post_id).child(this.reciver_id).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                public void onComplete(@NonNull Task<Void> param1Task) {}
//            });
//        }
//        this.query_readstarus.addValueEventListener(new ValueEventListener() {
//            public void onCancelled(DatabaseError param1DatabaseError) {}
//
//            public void onDataChange(DataSnapshot param1DataSnapshot) {
//                for (DataSnapshot ds : param1DataSnapshot.getChildren()) {
//                    Map map = (Map)param1DataSnapshot.getValue();
//                    if (map != ds)
//                        online = (String)map.get("online");
//                }
//                if (online.equals("1")) {
//                    notificationflag = 1;
//                    readStatus = "true";
//                    return;
//                }
//                if (online.equals("3") || online.equals(null) || online.equals("0")) {
//                    readStatus = "false";
//                    notificationflag = 0;
//                    return;
//                }
//            }
//        });
//    }
//
//    protected void onStop() {
//        super.onStop();
//        HashMap hashMap = new HashMap();
//        hashMap.put("online", "0");
//        this.useronline.child(this.cruntuser).child(this.post_id).setValue(hashMap);
//    }
}
