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
                    ItemDetail.this.mMap = param1GoogleMap;
                    if (ItemDetail.this.mLocation_permissiongrannted && ActivityCompat.checkSelfPermission(ItemDetail.this, "android.permission.ACCESS_FINE_LOCATION") != 0 && ActivityCompat.checkSelfPermission(ItemDetail.this, "android.permission.ACCESS_COARSE_LOCATION") != 0)
                        return;
                    ItemDetail.this.mMap.setMyLocationEnabled(true);
                    ItemDetail.this.mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    ItemDetail.this.mMap.getUiSettings().isCompassEnabled();
                    ItemDetail.this.mMap.getUiSettings().isMapToolbarEnabled();
                    ItemDetail.this.mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    MarkerOptions markerOptions = (new MarkerOptions()).position(ItemDetail.this.latLng1);
                    ItemDetail.this.mMap.addMarker(markerOptions);
                    ItemDetail.this.movecamera(new LatLng(latLng1.latitude, latLng1.longitude), 15.0F, "My Location");
                }
            });
            return;
        } catch (Exception supportMapFragment) {
            Toast.makeText(getApplicationContext(), "errorcode map", 0).show();
            return;
        }
    }

    private boolean islocationenable() {
        locationManager = (LocationManager)this.context.getSystemService("location");
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
                    ItemDetail.this.context.startActivity(intent);
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
                    new Item_Model();
                    ItemDetail.this.image1 = ((Item_Model)param1DataSnapshot.getValue(Item_Model.class)).getImageurl1();
                    ItemDetail.this.image2 = ((Item_Model)param1DataSnapshot.getValue(Item_Model.class)).getImageurl2();
                    ItemDetail.this.image3 = ((Item_Model)param1DataSnapshot.getValue(Item_Model.class)).getImageurl3();
                    ItemDetail.this.image4 = ((Item_Model)param1DataSnapshot.getValue(Item_Model.class)).getImageurl4();
                    if (ItemDetail.this.image4.equals("no")) {
                        ItemDetail.this.imagelist = new String[] { ItemDetail.this.image1, ItemDetail.this.image2, ItemDetail.this.image3 };
                    } else {
                        ItemDetail.this.imagelist = new String[] { ItemDetail.this.image1, ItemDetail.this.image2, ItemDetail.this.image3, ItemDetail.this.image4 };
                    }
                    if (ItemDetail.this.image3.equals("no"))
                        ItemDetail.this.imagelist = new String[] { ItemDetail.this.image1, ItemDetail.this.image2 };
                    if (ItemDetail.this.image2.equals("no"))
                        ItemDetail.this.imagelist = new String[] { ItemDetail.this.image1 };
                    if (ItemDetail.this.image1.equals("no")) {
                        Bitmap bitmap = ((BitmapDrawable)ItemDetail.this.getResources().getDrawable(R.mipmap.no_image_availible)).getBitmap();
                        ItemDetail.this.image1 = ItemDetail.encodeTobase64(bitmap);
                        ItemDetail.this.imagelist = new String[] { ItemDetail.this.image1 };
                    }
                    ItemDetail.this.viewpagerAdaptor = new ViewpagerAdaptor(ItemDetail.this.imagelist, ItemDetail.this.getApplicationContext());
                    ItemDetail.this.viewPager.setAdapter(ItemDetail.this.viewpagerAdaptor);
                    ItemDetail.this.dotcount = ItemDetail.this.viewpagerAdaptor.getCount();
                    ItemDetail.this.dots = new ImageView[ItemDetail.this.dotcount];
                    for (byte b = 0; b < ItemDetail.this.dotcount; b++) {
                        ItemDetail.this.dots[b] = new ImageView(ItemDetail.this.getApplicationContext());
                        ItemDetail.this.dots[b].setImageDrawable(ContextCompat.getDrawable(ItemDetail.this.getApplicationContext(), R.drawable.non_active_dot));
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
                        layoutParams.setMargins(8, 0, 8, 0);
                        ItemDetail.this.sliderDotspanel.addView(ItemDetail.this.dots[b], layoutParams);
                    }
                    ItemDetail.this.dots[0].setImageDrawable(ContextCompat.getDrawable(ItemDetail.this.getApplicationContext(), R.drawable.active_dot));
                    (new Timer()).scheduleAtFixedRate(new ItemDetail.MyTimerTask(ItemDetail.this, null), 4000L, 5000L);
                }
            });
            return;
        }
        if (this.status == 0) {
            FirebaseDatabase.getInstance().getReference("database").child(this.push_id).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onCancelled(DatabaseError param1DatabaseError) {}

                public void onDataChange(DataSnapshot param1DataSnapshot) {
                    new Item_Model();
                    ItemDetail.this.image1 = ((Item_Model)param1DataSnapshot.getValue(Item_Model.class)).getImageurl1();
                    ItemDetail.this.image2 = ((Item_Model)param1DataSnapshot.getValue(Item_Model.class)).getImageurl2();
                    ItemDetail.this.image3 = ((Item_Model)param1DataSnapshot.getValue(Item_Model.class)).getImageurl3();
                    ItemDetail.this.image4 = ((Item_Model)param1DataSnapshot.getValue(Item_Model.class)).getImageurl4();
                    if (ItemDetail.this.image4.equals("no")) {
                        ItemDetail.this.imagelist = new String[] { ItemDetail.this.image1, ItemDetail.this.image2, ItemDetail.this.image3 };
                    } else {
                        ItemDetail.this.imagelist = new String[] { ItemDetail.this.image1, ItemDetail.this.image2, ItemDetail.this.image3, ItemDetail.this.image4 };
                    }
                    if (ItemDetail.this.image3.equals("no"))
                        ItemDetail.this.imagelist = new String[] { ItemDetail.this.image1, ItemDetail.this.image2 };
                    if (ItemDetail.this.image2.equals("no"))
                        ItemDetail.this.imagelist = new String[] { ItemDetail.this.image1 };
                    if (ItemDetail.this.image1.equals("no")) {
                        Bitmap bitmap = ((BitmapDrawable)ItemDetail.this.getResources().getDrawable(R.mipmap.no_image_availible)).getBitmap();
                        ItemDetail.this.image1 = ItemDetail.encodeTobase64(bitmap);
                        ItemDetail.this.imagelist = new String[] { ItemDetail.this.image1 };
                    }
                    ItemDetail.this.viewpagerAdaptor = new ViewpagerAdaptor(ItemDetail.this.imagelist, ItemDetail.this.getApplicationContext());
                    ItemDetail.this.viewPager.setAdapter(ItemDetail.this.viewpagerAdaptor);
                    ItemDetail.this.dotcount = ItemDetail.this.viewpagerAdaptor.getCount();
                    ItemDetail.this.dots = new ImageView[ItemDetail.this.dotcount];
                    for (byte b = 0; b < ItemDetail.this.dotcount; b++) {
                        ItemDetail.this.dots[b] = new ImageView(ItemDetail.this.getApplicationContext());
                        ItemDetail.this.dots[b].setImageDrawable(ContextCompat.getDrawable(ItemDetail.this.getApplicationContext(), R.drawable.non_active_dot));
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
                        layoutParams.setMargins(8, 0, 8, 0);
                        ItemDetail.this.sliderDotspanel.addView(ItemDetail.this.dots[b], layoutParams);
                    }
                    ItemDetail.this.dots[0].setImageDrawable(ContextCompat.getDrawable(ItemDetail.this.getApplicationContext(), R.drawable.active_dot));
                    (new Timer()).scheduleAtFixedRate(new ItemDetail.MyTimerTask(ItemDetail.this, null), 4000L, 5000L);
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
            this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(paramLatLng, paramFloat));
    }

    public void onConnectionFailed(@NonNull ConnectionResult paramConnectionResult) {}



    private class MyTimerTask extends TimerTask {
        private MyTimerTask() {}

        public void run() { ItemDetail.this.runOnUiThread(new Runnable() {
            public void run() {
                if (ItemDetail.this.viewpagerAdaptor.getCount() == 4) {
                    if (ItemDetail.this.viewPager.getCurrentItem() == 0) {
                        ItemDetail.this.viewPager.setCurrentItem(1);
                        return;
                    }
                    if (ItemDetail.this.viewPager.getCurrentItem() == 1) {
                        ItemDetail.this.viewPager.setCurrentItem(2);
                        return;
                    }
                    if (ItemDetail.this.viewPager.getCurrentItem() == 2) {
                        ItemDetail.this.viewPager.setCurrentItem(3);
                        return;
                    }
                    if (ItemDetail.this.viewPager.getCurrentItem() == 3) {
                        ItemDetail.this.viewPager.setCurrentItem(0);
                        return;
                    }
                    return;
                }
                if (ItemDetail.this.viewpagerAdaptor.getCount() == 3) {
                    if (ItemDetail.this.viewPager.getCurrentItem() == 0) {
                        ItemDetail.this.viewPager.setCurrentItem(1);
                        return;
                    }
                    if (ItemDetail.this.viewPager.getCurrentItem() == 1) {
                        ItemDetail.this.viewPager.setCurrentItem(2);
                        return;
                    }
                    if (ItemDetail.this.viewPager.getCurrentItem() == 2) {
                        ItemDetail.this.viewPager.setCurrentItem(0);
                        return;
                    }
                    return;
                }
                if (ItemDetail.this.viewpagerAdaptor.getCount() == 2) {
                    if (ItemDetail.this.viewPager.getCurrentItem() == 0) {
                        ItemDetail.this.viewPager.setCurrentItem(1);
                        return;
                    }
                    if (ItemDetail.this.viewPager.getCurrentItem() == 1) {
                        ItemDetail.this.viewPager.setCurrentItem(0);
                        return;
                    }
                }
            }
        }); }
    }

    class null implements Runnable {
        null() {}

        public void run() {
            if (ItemDetail.this.viewpagerAdaptor.getCount() == 4) {
                if (ItemDetail.this.viewPager.getCurrentItem() == 0) {
                    ItemDetail.this.viewPager.setCurrentItem(1);
                    return;
                }
                if (ItemDetail.this.viewPager.getCurrentItem() == 1) {
                    ItemDetail.this.viewPager.setCurrentItem(2);
                    return;
                }
                if (ItemDetail.this.viewPager.getCurrentItem() == 2) {
                    ItemDetail.this.viewPager.setCurrentItem(3);
                    return;
                }
                if (ItemDetail.this.viewPager.getCurrentItem() == 3) {
                    ItemDetail.this.viewPager.setCurrentItem(0);
                    return;
                }
                return;
            }
            if (ItemDetail.this.viewpagerAdaptor.getCount() == 3) {
                if (ItemDetail.this.viewPager.getCurrentItem() == 0) {
                    ItemDetail.this.viewPager.setCurrentItem(1);
                    return;
                }
                if (ItemDetail.this.viewPager.getCurrentItem() == 1) {
                    ItemDetail.this.viewPager.setCurrentItem(2);
                    return;
                }
                if (ItemDetail.this.viewPager.getCurrentItem() == 2) {
                    ItemDetail.this.viewPager.setCurrentItem(0);
                    return;
                }
                return;
            }
            if (ItemDetail.this.viewpagerAdaptor.getCount() == 2) {
                if (ItemDetail.this.viewPager.getCurrentItem() == 0) {
                    ItemDetail.this.viewPager.setCurrentItem(1);
                    return;
                }
                if (ItemDetail.this.viewPager.getCurrentItem() == 1) {
                    ItemDetail.this.viewPager.setCurrentItem(0);
                    return;
                }
            }
        }
    }

}

