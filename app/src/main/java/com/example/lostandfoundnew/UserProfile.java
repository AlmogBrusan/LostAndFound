package com.example.lostandfoundnew;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import de.hdodenhof.circleimageview.CircleImageView;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserProfile extends AppCompatActivity {
    CircleImageView circleImageView;
    Context context;
    DatabaseReference databaseReference;
    String displayname;
    String email;
    FirebaseAuth firebaseAuth;
    String image;
    ImageView imgonline;
    List<ItemModel> item_modelfound = new ArrayList();
    List<ItemModel> item_modellost = new ArrayList();
    String lastonline;
    String phone;
    String profileid;
    RecyclerView recyclerViewfound;
    RecyclerView recyclerViewlost;
    FirebaseUser user;
    TextView textView1, textView2;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        Log.d("UserProfile ","before showdata1");
        setContentView(R.layout.activity_user_profile);
        recyclerViewfound = findViewById(R.id.recyclerlistadds);
        recyclerViewlost = findViewById(R.id.recyclerlistaddslost);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        linearLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
        recyclerViewfound.setLayoutManager(linearLayoutManager);
        linearLayoutManager = new LinearLayoutManager(getBaseContext());
        linearLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
        recyclerViewlost.setLayoutManager(linearLayoutManager);
        firebaseAuth = FirebaseAuth.getInstance();
        circleImageView = findViewById(R.id.imageview);
        imgonline = findViewById(R.id.useronline);
        user = firebaseAuth.getCurrentUser();
        textView1 = findViewById(R.id.emailprofile);
        textView2 = findViewById(R.id.phonenumberprofile);
        databaseReference = FirebaseDatabase.getInstance().getReference("database");
        context = this;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            image = (String)bundle.get("image");
            phone = (String)bundle.get("phone");
            email = (String)bundle.get("email");
            profileid = (String)bundle.get("rec_id");
            displayname = (String)bundle.get("displayname");
        }
        ((TextView)findViewById(R.id.displayname)).setText(displayname);
        if (image.equals("no")) {
            circleImageView.setImageBitmap(BitmapFactory.decodeResource(getBaseContext().getResources(), R.mipmap.default_profile));
        } else {
            circleImageView.setImageBitmap(decodeBase64(image));
        }
        textView1.setText(email);
        textView2.setText(phone);
        checkstatus();
        Log.v("UserProfile","before showdata");

        showdatafound();
        Log.v("UserProfile","after showdata");
        showdatalost();

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = 21)
            public void onClick(View param1View) {
                Intent intent = new Intent(getBaseContext(), UserImage.class);
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(UserProfile.this, new Pair[] { Pair.create(circleImageView, "imageprofile") });
                intent.putExtra("image", ItemShow.encodeTobase64(((BitmapDrawable)circleImageView.getDrawable()).getBitmap()));
                startActivity(intent, activityOptions.toBundle());
            }
        });
    }

    private void checkstatus() { FirebaseDatabase.getInstance().getReference("onlineuser").child(this.profileid).child("conection").addValueEventListener(new ValueEventListener() {
        public void onCancelled(DatabaseError param1DatabaseError) {}

        public void onDataChange(DataSnapshot param1DataSnapshot) {
            lastonline = param1DataSnapshot.getValue().toString();
            if (lastonline.equals("true")) {
                imgonline.setBackgroundResource(R.drawable.online);
                return;
            }
            imgonline.setBackgroundResource(R.drawable.offline);
        }
    }); }

    public static Bitmap decodeBase64(String paramString) {
        byte[] arrayOfByte = Base64.decode(paramString, 0);
        return BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length);
    }

    public static String encodeTobase64(Bitmap paramBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        paramBitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
    }

    private void showdatafound() { this.databaseReference.orderByChild("user_id").equalTo(this.profileid).addValueEventListener(new ValueEventListener() {
        public void onCancelled(DatabaseError param1DatabaseError) {
            String str = param1DatabaseError.getMessage().toString();
            Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
        }

        public void onDataChange(DataSnapshot param1DataSnapshot) {
            item_modelfound.clear();
            for (DataSnapshot dataSnapshot : param1DataSnapshot.getChildren()) {
                ItemModel item_Model = new ItemModel();
                item_Model.setTitle((dataSnapshot.getValue(ItemModel.class)).getTitle());
                item_Model.setAddress((dataSnapshot.getValue(ItemModel.class)).getAddress());
                item_Model.setLatitude((dataSnapshot.getValue(ItemModel.class)).latitude);
                item_Model.setLongitude((dataSnapshot.getValue(ItemModel.class)).longitude);
                item_Model.setDescription((dataSnapshot.getValue(ItemModel.class)).getDescription());
                item_Model.setCategories((dataSnapshot.getValue(ItemModel.class)).getCategories());
                item_Model.setStatus((dataSnapshot.getValue(ItemModel.class)).getStatus());
                item_Model.setId((dataSnapshot.getValue(ItemModel.class)).getId());
                item_Model.setDate((dataSnapshot.getValue(ItemModel.class)).getDate());
                item_Model.setPhone((dataSnapshot.getValue(ItemModel.class)).getPhone());
                item_Model.setUser_id((dataSnapshot.getValue(ItemModel.class)).getUser_id());
                if ((dataSnapshot.getValue(ItemModel.class)).getImageurl1().equals("no")) {
                    item_Model.setImageurl1(UserProfile.encodeTobase64(((BitmapDrawable)getResources().getDrawable(R.mipmap.no_image_availible)).getBitmap()));
                } else {
                    item_Model.setImageurl1((dataSnapshot.getValue(ItemModel.class)).getImageurl1());
                }
                item_modelfound.add(item_Model);
            }
            if (item_modelfound.isEmpty()) {
                Toast.makeText(getApplicationContext(), " No Found Item availible !", Toast.LENGTH_SHORT).show();
                return;
            }
            Collections.reverse(item_modelfound);
            recyclerViewfound.setAdapter(new ProfileRecyclerAdapter(context, item_modelfound));
        }
    }); }

    private void showdatalost() {
        FirebaseDatabase.getInstance().getReference("lostitem").orderByChild("user_id")
                .equalTo(this.profileid).addValueEventListener(new ValueEventListener() {

        public void onCancelled(DatabaseError param1DatabaseError) {

            String str = param1DatabaseError.getMessage().toString();
            Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
        }

        public void onDataChange(DataSnapshot param1DataSnapshot) {
            item_modellost.clear();
            for (DataSnapshot dataSnapshot : param1DataSnapshot.getChildren()) {
                ItemModel item_Model = new ItemModel();
                item_Model.setTitle((dataSnapshot.getValue(ItemModel.class)).getTitle());
                item_Model.setAddress((dataSnapshot.getValue(ItemModel.class)).getAddress());
                item_Model.setLatitude((dataSnapshot.getValue(ItemModel.class)).latitude);
                item_Model.setLongitude((dataSnapshot.getValue(ItemModel.class)).longitude);
                item_Model.setDescription((dataSnapshot.getValue(ItemModel.class)).getDescription());
                item_Model.setCategories((dataSnapshot.getValue(ItemModel.class)).getCategories());
                item_Model.setStatus((dataSnapshot.getValue(ItemModel.class)).getStatus());
                item_Model.setId((dataSnapshot.getValue(ItemModel.class)).getId());
                item_Model.setDate((dataSnapshot.getValue(ItemModel.class)).getDate());
                item_Model.setPhone((dataSnapshot.getValue(ItemModel.class)).getPhone());
                item_Model.setUser_id((dataSnapshot.getValue(ItemModel.class)).getUser_id());
                if ((dataSnapshot.getValue(ItemModel.class)).getImageurl1().equals("no")) {
                    item_Model.setImageurl1(UserProfile.encodeTobase64(((BitmapDrawable)getResources().getDrawable(R.mipmap.no_image_availible)).getBitmap()));
                } else {
                    item_Model.setImageurl1((dataSnapshot.getValue(ItemModel.class)).getImageurl1());
                }
                item_modellost.add(item_Model);
            }
            if (item_modellost.isEmpty()) {
                Toast.makeText(getApplicationContext(), " No Found Item availible !", Toast.LENGTH_SHORT).show();
                return;
            }
            Collections.reverse(item_modellost);
            recyclerViewlost.setAdapter(new ProfileRecyclerAdapter(context, item_modellost));
        }
    }); }


}
