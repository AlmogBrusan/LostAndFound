package com.example.lostandfoundnew;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ItemDetail extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String CAll_Per = "android.permission.CALL_PHONE";
    private static final String COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";
    private static final int Callrequest_code = 4321;
    private static final String FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40.0D, -60.0D), new LatLng(78.0D, 45.0D));
    private static final int locationrequest_code = 1234;
    final float DEFOULt_ZOOM = 15.0F;

    String address;
    SupportMapFragment supportMapFragment;
    FrameLayout btnsms;
    String categories;
    String chatflag = "0";
    String compressedimage;
    Location current_location;
    String currentuser = "empty User";
    String description;
    int dotcount;
    ImageView[] dots;
    String email;
    String image;
    String image1;
    String image2;
    String image3;
    String image4;
    String[] imagelist;
    LatLng latLng1;
    Double latitude;
    Double longitude;
    private boolean mCall_permissiongrannted = false;
    private GeoDataClient mGeoDataClient;
    private boolean mLocation_permissiongrannted = false;
    GoogleMap mMap;
    private FusedLocationProviderClient mfusedLocationProviderClient;
    String phone;
    String profile_image;
    String profile_phone;
    String profileid;
    String push_id;
    LinearLayout sliderDotspanel;
    int status;
    String title;
    LinearLayout toolbaritemdetail;
    TextView txtname;
    String user_id;
    ClickViewpager viewPager;
    ViewpagerAdaptor viewpagerAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        paramBundle = getIntent().getExtras();
        TextView textView1 = findViewById(R.id. txtdetailtittle);
        TextView textView2 = findViewById(R.id. txtdetailaddress);
        TextView textView3 = findViewById(R.id. txtdetailcategories);
        TextView textView4 = findViewById(R.id. txtdetaildescription);
        toolbaritemdetail = findViewById(R.id. toolbaritemdetail);
        txtname = findViewById(R.id. detail_name);
        FrameLayout frameLayout1 = findViewById(R.id. btnmessage);
        viewPager = findViewById(R.id. viewpager);
        sliderDotspanel = findViewById(R.id. sliderdots);
        btnsms = findViewById(R.id. btnsms);
        FrameLayout frameLayout2 = findViewById(R.id. btncall);
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null)
            currentuser = firebaseUser.getUid().toString();
        if (paramBundle != null) {
            title = (String)paramBundle.get("title");
            address = (String)paramBundle.get("address");
            categories = (String)paramBundle.get("categories");
            longitude = (Double)paramBundle.get("longitude");
            latitude = (Double)paramBundle.get("latitude");
            description = (String)paramBundle.get("description");
            status = ((Integer)paramBundle.get("Status")).intValue();
            phone = (String)paramBundle.get("phone");
            user_id = (String)paramBundle.get("user_id");
            push_id = (String)paramBundle.get("push_id");
        }
        latLng1 = new LatLng(latitude.doubleValue(), longitude.doubleValue());
        getpermission();
        showimageslider();
        textView1.setText(title);
        textView2.setText(address);
        textView3.setText("( " + categories + " )");
        textView4.setText(description);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.detail_menu);
        getSupportActionBar().getCustomView();
        FirebaseDatabase.getInstance().getReference("userprofile").child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(DatabaseError param1DatabaseError) {}

            public void onDataChange(DataSnapshot param1DataSnapshot) {
                String str1 = ((Model_User_profile)param1DataSnapshot.getValue(Model_User_profile.class)).getFirstname();
                String str2 = ((Model_User_profile)param1DataSnapshot.getValue(Model_User_profile.class)).getLastname();
                ItemDetail.this.txtname.setText(str1 + " " + str2);
                ItemDetail.this.profile_image = ((Model_User_profile)param1DataSnapshot.getValue(Model_User_profile.class)).getImage();
                ItemDetail.this.profile_phone = ((Model_User_profile)param1DataSnapshot.getValue(Model_User_profile.class)).getPhonenumber();
                ItemDetail.this.profileid = ((Model_User_profile)param1DataSnapshot.getValue(Model_User_profile.class)).getEmail();
            }
        });
        txtname.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                Intent intent = new Intent(ItemDetail.this.getBaseContext(), user_profile.class);
                intent.putExtra("rec_id", ItemDetail.this.user_id);
                intent.putExtra("image", ItemDetail.this.profile_image);
                intent.putExtra("email", ItemDetail.this.profileid);
                intent.putExtra("phone", ItemDetail.this.profile_phone);
                intent.putExtra("displayname", ItemDetail.this.txtname.getText());
                ItemDetail.this.startActivity(intent);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int param1Int) {}

            public void onPageScrolled(int param1Int1, float param1Float, int param1Int2) {}

            public void onPageSelected(int param1Int) {
                for (byte b = 0; b < ItemDetail.this.dotcount; b++)
                    ItemDetail.this.dots[b].setImageDrawable(ContextCompat.getDrawable(ItemDetail.this.getApplicationContext(), R.drawable.non_active_dot));
                ItemDetail.this.dots[param1Int].setImageDrawable(ContextCompat.getDrawable(ItemDetail.this.getApplicationContext(), R.drawable.active_dot));
            }
        });
        viewPager.setOnItemClickListener(new ClickViewpager.OnItemClickListener() {
            public void onItemClick(int param1Int) {
                Intent intent = new Intent(ItemDetail.this.getBaseContext(), imageDetailFull.class);
                intent.putExtra("imageposition", param1Int);
                intent.putExtra("status", ItemDetail.this.status);
                intent.putExtra("push_id", ItemDetail.this.push_id);
                ItemDetail.this.startActivity(intent);
            }
        });
        frameLayout1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Toast.makeText(ItemDetail.this.getApplicationContext(), "You need to login First", Toast.LENGTH_SHORT).show();
                    return;
                }
                Bitmap bitmap = ItemDetail.this.getResizedBitmap(ItemDetail.decodeBase64(ItemDetail.this.image1), 250);
                ItemDetail.this.compressedimage = ItemDetail.encodeTobase64(bitmap);
                Intent intent = new Intent(ItemDetail.this.getApplicationContext(), Chat.class);
                intent.putExtra("id", ItemDetail.this.user_id);
                intent.putExtra("image", ItemDetail.this.compressedimage);
                intent.putExtra("push_id", ItemDetail.this.push_id);
                intent.putExtra("title", ItemDetail.this.title);
                intent.putExtra("status", ItemDetail.this.status);
                intent.putExtra("chatflag", ItemDetail.this.chatflag);
                ItemDetail.this.startActivity(intent);
            }
        });
        btnsms.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                Intent intent = new Intent("android.intent.action.SENDTO");
                intent.setData(Uri.parse("smsto:" + Uri.encode(ItemDetail.this.phone)));
                ItemDetail.this.startActivity(intent);
            }
        });
        frameLayout2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) { ItemDetail.this.callpermission(); }
        });
        if (user_id.equals(currentuser))
            toolbaritemdetail.setVisibility(View.GONE);
    }
    }
}
