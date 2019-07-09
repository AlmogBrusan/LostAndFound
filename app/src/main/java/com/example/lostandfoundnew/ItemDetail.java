package com.example.lostandfoundnew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.Timer;
import java.util.TimerTask;

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
    private LocationManager locationManager;
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
    ViewpagerAdapter viewpagerAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        savedInstanceState = getIntent().getExtras();
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
        if (savedInstanceState != null) {
            title = (String)savedInstanceState.get("title");
            address = (String)savedInstanceState.get("address");
            categories = (String)savedInstanceState.get("categories");
            longitude = (Double)savedInstanceState.get("longitude");
            latitude = (Double)savedInstanceState.get("latitude");
            description = (String)savedInstanceState.get("description");
            status = ((Integer)savedInstanceState.get("Status")).intValue();
            phone = (String)savedInstanceState.get("phone");
            user_id = (String)savedInstanceState.get("user_id");
            push_id = (String)savedInstanceState.get("push_id");
        }
        latLng1 = new LatLng(latitude.doubleValue(), longitude.doubleValue());
        getpermission();
        showimageslider();
        textView1.setText(title);
        textView2.setText(address);
        textView3.setText("( " + phone + " )");
        textView4.setText(description);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.detail_menu);
        getSupportActionBar().getCustomView();
        FirebaseDatabase.getInstance().getReference("userprofile").child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(DatabaseError param1DatabaseError) {}

            public void onDataChange(DataSnapshot param1DataSnapshot) {
                String str1 = (param1DataSnapshot.getValue(UserProfileModel.class)).getFirstName();
                String str2 = (param1DataSnapshot.getValue(UserProfileModel.class)).getLastName();
                txtname.setText(str1 + " " + str2);
                profile_image = (param1DataSnapshot.getValue(UserProfileModel.class)).getImage();
                profile_phone = (param1DataSnapshot.getValue(UserProfileModel.class)).getPhoneNumber();
                profileid = (param1DataSnapshot.getValue(UserProfileModel.class)).getEmail();
            }
        });
        txtname.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                Intent intent = new Intent(getBaseContext(), UserProfile.class);
                intent.putExtra("rec_id", user_id);
                intent.putExtra("image", profile_image);
                intent.putExtra("email", profileid);
                intent.putExtra("phone", profile_phone);
                intent.putExtra("displayname", txtname.getText());
                startActivity(intent);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int param1Int) {}

            public void onPageScrolled(int param1Int1, float param1Float, int param1Int2) {}

            public void onPageSelected(int param1Int) {
                for (byte b = 0; b < dotcount; b++)
                    dots[b].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                dots[param1Int].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }
        });
        viewPager.setOnItemClickListener(new ClickViewpager.OnItemClickListener() {
            public void onItemClick(int param1Int) {
                Intent intent = new Intent(getBaseContext(), ImageDetailFull.class);
                intent.putExtra("imageposition", param1Int);
                intent.putExtra("status", status);
                intent.putExtra("push_id", push_id);
                startActivity(intent);
            }
        });
        frameLayout1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Toast.makeText(getApplicationContext(), "You need to login First", Toast.LENGTH_SHORT).show();
                    return;
                }
                Bitmap bitmap = getResizedBitmap(ItemDetail.decodeBase64(image1), 250);
                compressedimage = ItemDetail.encodeTobase64(bitmap);
                Intent intent = new Intent(getApplicationContext(), Chat.class);
                intent.putExtra("id", user_id);
                intent.putExtra("image", compressedimage);
                intent.putExtra("push_id", push_id);
                intent.putExtra("title", title);
                intent.putExtra("status", status);
                intent.putExtra("chatflag", chatflag);
                startActivity(intent);
            }
        });
        btnsms.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                Intent intent = new Intent("android.intent.action.SENDTO");
                intent.setData(Uri.parse("smsto:" + Uri.encode(phone)));
                startActivity(intent);
            }
        });
        frameLayout2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) { callpermission(); }
        });
        if (user_id.equals(currentuser))
            toolbaritemdetail.setVisibility(View.GONE);
    }

    @SuppressLint({"MissingPermission"})
    private void call() { startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + this.phone))); }

    private void callpermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.CALL_PHONE") == 0) {
            this.mCall_permissiongrannted = true;
            call();
            return;
        }
        ActivityCompat.requestPermissions(this, new String[] { "android.permission.CALL_PHONE" }, 4321);
    }

    public static Bitmap decodeBase64(String paramString) {
        byte[] arrayOfByte = Base64.decode(paramString, 0);
        return BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length);
    }

    public static String encodeTobase64(Bitmap paramBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        paramBitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
    }

    private void getpermission() {
        String[] arrayOfString = new String[2];
        arrayOfString[0] = "android.permission.ACCESS_FINE_LOCATION";
        arrayOfString[1] = "android.permission.ACCESS_COARSE_LOCATION";
        if (ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.ACCESS_FINE_LOCATION") == 0) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.ACCESS_COARSE_LOCATION") == 0) {
                this.mLocation_permissiongrannted = true;
                initmap();
                return;
            }
            ActivityCompat.requestPermissions(this, arrayOfString, 1234);
            return;
        }
        ActivityCompat.requestPermissions(this, arrayOfString, 1234);
    }

    private void initmap() {
        supportMapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.detailmap);
        try {
            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                public void onMapReady(GoogleMap param1GoogleMap) {
                    mMap = param1GoogleMap;
                    if (mLocation_permissiongrannted && ActivityCompat.checkSelfPermission(ItemDetail.this, "android.permission.ACCESS_FINE_LOCATION") != 0 && ActivityCompat.checkSelfPermission(ItemDetail.this, "android.permission.ACCESS_COARSE_LOCATION") != 0)
                        return;
                    mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    mMap.getUiSettings().isCompassEnabled();
                    mMap.getUiSettings().isMapToolbarEnabled();
                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    MarkerOptions markerOptions = (new MarkerOptions()).position(latLng1);
                    mMap.addMarker(markerOptions);
                    movecamera(new LatLng(latLng1.latitude, latLng1.longitude), 15.0F, "My Location");
                }
            });
            return;
        } catch (Exception supportMapFragment) {
            Toast.makeText(getApplicationContext(), "errorcode map", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private boolean islocationenable() {
        locationManager = (LocationManager)this.getSystemService(LOCATION_SERVICE);
        boolean bool1 = false;
        boolean bool2 = false;
        try {
            boolean bool = locationManager.isProviderEnabled("gps");
            bool1 = bool;
        } catch (Exception exception) {}
        try {
            boolean bool = locationManager.isProviderEnabled("network");
            bool2 = bool;
        } catch (Exception locationManager) {}
        if (!bool1 && !bool2) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Gps network not enabled").setPositiveButton("Open Location Setting", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                    Intent intent = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
                    startActivity(intent);
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface param1DialogInterface, int param1Int) { param1DialogInterface.dismiss(); }
            });
            builder.show();
            return false;
        }
        return true;
    }

    private void showimageslider() {
        if (this.status == 1) {
            FirebaseDatabase.getInstance().getReference("lostitem").child(this.push_id).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onCancelled(DatabaseError param1DatabaseError) {}

                public void onDataChange(DataSnapshot param1DataSnapshot) {
                    new ItemModel();
                    image1 = (param1DataSnapshot.getValue(ItemModel.class)).getImageurl();

                    if (image1.equals("no")) {
                        Bitmap bitmap = ((BitmapDrawable)getResources().getDrawable(R.mipmap.no_image_availible)).getBitmap();
                        image1 = encodeTobase64(bitmap);
                        imagelist = new String[] { image1 };
                    }else {
                        imagelist = new String[] { image1 };
                    }
                    viewpagerAdaptor = new ViewpagerAdapter(imagelist, getApplicationContext());
                    viewPager.setAdapter(viewpagerAdaptor);
                    dotcount = viewpagerAdaptor.getCount();
                    dots = new ImageView[dotcount];
                    for (byte b = 0; b < dotcount; b++) {
                        dots[b] = new ImageView(getApplicationContext());
                        dots[b].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
                        layoutParams.setMargins(8, 0, 8, 0);
                        sliderDotspanel.addView(dots[b], layoutParams);
                    }
                    dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                    (new Timer()).scheduleAtFixedRate(new MyTimerTask(), 4000L, 5000L);
                }
            });
            return;
        }
        if (this.status == 0) {
            FirebaseDatabase.getInstance().getReference("database").child(this.push_id).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onCancelled(DatabaseError param1DatabaseError) {}

                public void onDataChange(DataSnapshot param1DataSnapshot) {
                    new ItemModel();
                    image1 = (param1DataSnapshot.getValue(ItemModel.class)).getImageurl();

                    if (image1.equals("no")) {
                        Bitmap bitmap = ((BitmapDrawable)getResources().getDrawable(R.mipmap.no_image_availible)).getBitmap();
                        image1 = ItemDetail.encodeTobase64(bitmap);
                        imagelist = new String[] { image1 };
                    }
                    viewpagerAdaptor = new ViewpagerAdapter(imagelist, getApplicationContext());
                    viewPager.setAdapter(viewpagerAdaptor);
                    dotcount = viewpagerAdaptor.getCount();
                    dots = new ImageView[dotcount];
                    for (byte b = 0; b < dotcount; b++) {
                        dots[b] = new ImageView(getApplicationContext());
                        dots[b].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
                        layoutParams.setMargins(8, 0, 8, 0);
                        sliderDotspanel.addView(dots[b], layoutParams);
                    }
                    dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                    (new Timer()).scheduleAtFixedRate(new MyTimerTask(), 4000L, 5000L);
                }
            });
            return;
        }
    }

    public Bitmap getResizedBitmap(Bitmap paramBitmap, int paramInt) {
        int i = paramBitmap.getWidth();
        int j = paramBitmap.getHeight();
        float f = i / j;
        if (f > 1.0F) {
            j = (int)(paramInt / f);
            i = paramInt;
            paramInt = j;
            return Bitmap.createScaledBitmap(paramBitmap, i, paramInt, true);
        }
        i = (int)(paramInt * f);
        return Bitmap.createScaledBitmap(paramBitmap, i, paramInt, true);
    }

    public boolean isServiceOK() {
        int i = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (i == 0)
            return true;
        if (GoogleApiAvailability.getInstance().isUserResolvableError(i)) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, i, 9001).show();
            return false;
        }
        Toast.makeText(this, "You cant make map request", Toast.LENGTH_SHORT).show();
        return false;
    }

    public void movecamera(LatLng paramLatLng, float paramFloat, String paramString) {
        if (paramString.equals("My Location"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(paramLatLng, paramFloat));
    }

    public void onConnectionFailed(@NonNull ConnectionResult paramConnectionResult) {}



    private class MyTimerTask extends TimerTask {
        private MyTimerTask() {}

        public void run() { runOnUiThread(new Runnable() {
            public void run() {
                if (viewpagerAdaptor.getCount() == 4) {
                    if (viewPager.getCurrentItem() == 0) {
                        viewPager.setCurrentItem(1);
                        return;
                    }
                    if (viewPager.getCurrentItem() == 1) {
                        viewPager.setCurrentItem(2);
                        return;
                    }
                    if (viewPager.getCurrentItem() == 2) {
                        viewPager.setCurrentItem(3);
                        return;
                    }
                    if (viewPager.getCurrentItem() == 3) {
                        viewPager.setCurrentItem(0);
                        return;
                    }
                    return;
                }
                if (viewpagerAdaptor.getCount() == 3) {
                    if (viewPager.getCurrentItem() == 0) {
                        viewPager.setCurrentItem(1);
                        return;
                    }
                    if (viewPager.getCurrentItem() == 1) {
                        viewPager.setCurrentItem(2);
                        return;
                    }
                    if (viewPager.getCurrentItem() == 2) {
                        viewPager.setCurrentItem(0);
                        return;
                    }
                    return;
                }
                if (viewpagerAdaptor.getCount() == 2) {
                    if (viewPager.getCurrentItem() == 0) {
                        viewPager.setCurrentItem(1);
                        return;
                    }
                    if (viewPager.getCurrentItem() == 1) {
                        viewPager.setCurrentItem(0);

                    }
                }
            }
        }); }
    }

    final Runnable r = new Runnable() {
        public void run() {
            if (viewpagerAdaptor.getCount() == 4) {
                if (viewPager.getCurrentItem() == 0) {
                    viewPager.setCurrentItem(1);
                    return;
                }
                if (viewPager.getCurrentItem() == 1) {
                    viewPager.setCurrentItem(2);
                    return;
                }
                if (viewPager.getCurrentItem() == 2) {
                    viewPager.setCurrentItem(3);
                    return;
                }
                if (viewPager.getCurrentItem() == 3) {
                    viewPager.setCurrentItem(0);
                    return;
                }
                return;
            }
            if (viewpagerAdaptor.getCount() == 3) {
                if (viewPager.getCurrentItem() == 0) {
                    viewPager.setCurrentItem(1);
                    return;
                }
                if (viewPager.getCurrentItem() == 1) {
                    viewPager.setCurrentItem(2);
                    return;
                }
                if (viewPager.getCurrentItem() == 2) {
                    viewPager.setCurrentItem(0);
                    return;
                }
                return;
            }
            if (viewpagerAdaptor.getCount() == 2) {
                if (viewPager.getCurrentItem() == 0) {
                    viewPager.setCurrentItem(1);
                    return;
                }
                if (viewPager.getCurrentItem() == 1) {
                    viewPager.setCurrentItem(0);

                }
            }
        }
    };
}

