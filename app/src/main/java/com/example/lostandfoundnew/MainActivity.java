package com.example.lostandfoundnew;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;

public class MainActivity extends AppCompatActivity {
    public static final int ERROR_DIALOG_REQ = 9001;

    Activity activity;
    Button btnFound;
    Context context;
    FirebaseAuth firebaseAuth;
    DatabaseReference lastOnlineRef;
    Location location;
    private LocationManager locationManager;
    FlowingDrawer mDrawer;
    DatabaseReference myConnectionsRef;
    RelativeLayout relativeLayout;
    SlidingRootNavBuilder slidingRootNavBuilder;
    DatabaseReference stopnotifyref;
    TextView txtaboutus;
    TextView txtpostfound;
    TextView txtpostlost;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_main);
        this.btnFound = findViewById(R.id.btnfound);
        Button button1 = findViewById(R.id.btnlost);
        Button button2 = findViewById(R.id.btnloginmenu);
        TextView textView1 = findViewById(R.id.txtviewinboxmenu);
        this.firebaseAuth = FirebaseAuth.getInstance();
        TextView textView2 = findViewById(R.id.txtviewprofilemenu);
        this.relativeLayout = findViewById(R.id.mainrelativelayout);
        this.txtpostfound = findViewById(R.id.txtpostfound);
        this.txtpostlost = findViewById(R.id.txtpostlost);
        this.txtaboutus = findViewById(R.id.txtaboutus);
        this.activity = this;
        this.context = this;
        this.stopnotifyref = FirebaseDatabase.getInstance().getReference("token");
        textView1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (MainActivity.this.firebaseAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(MainActivity.this.getBaseContext(), Telephony.Sms.Inbox.class);
                    MainActivity.this.startActivity(intent);
                    return;
                }

                Snackbar snackbar = Snackbar.make(MainActivity.this.relativeLayout, "You Are not Login", Snackbar.LENGTH_SHORT).setAction("Login", new View.OnClickListener() {
                    public void onClick(View param2View) {
                        Intent intent = new Intent(MainActivity.this.getBaseContext(), Login.class);
                        MainActivity.this.startActivity(intent);
                    }
                });
                snackbar.setActionTextColor(MainActivity.this.getResources().getColor(R.color.colorPrimary));
                snackbar.show();
            }
        });
        AppRate.with(this).setInstallDays(0).setLaunchTimes(3).setRemindInterval(2).setShowLaterButton(true).setDebug(false).setOnClickButtonListener(new OnClickButtonListener() {
            public void onClickButton(int param1Int) { Log.d(MainActivity.class.getName(), Integer.toString(param1Int)); }
        }).monitor();
        AppRate.showRateDialogIfMeetsConditions(this);
        this.txtaboutus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                Intent intent = new Intent(MainActivity.this.getBaseContext(), About_Us.class);
                MainActivity.this.startActivity(intent);
            }
        });
        this.txtpostfound.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (MainActivity.this.firebaseAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(MainActivity.this.getBaseContext(), Item_Upload.class);
                    intent.putExtra("status", 0);
                    MainActivity.this.startActivity(intent);
                    return;
                }
                Snackbar snackbar = Snackbar.make(MainActivity.this.relativeLayout, "You Are not Login", Snackbar.LENGTH_LONG).setAction("Login", new View.OnClickListener() {
                    public void onClick(View param2View) {
                        Intent intent = new Intent(MainActivity.this.getBaseContext(), Login.class);
                        MainActivity.this.startActivity(intent);
                    }
                });
                snackbar.setActionTextColor(MainActivity.this.getResources().getColor(R.color.colorPrimary));
                snackbar.show();
            }
        });
        this.txtpostlost.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (MainActivity.this.firebaseAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(MainActivity.this.getBaseContext(), Item_Upload.class);
                    intent.putExtra("status", 1);
                    MainActivity.this.startActivity(intent);
                    return;
                }
                Snackbar snackbar = Snackbar.make(MainActivity.this.relativeLayout, "You Are not Login", Snackbar.LENGTH_SHORT).setAction("Login", new View.OnClickListener() {
                    public void onClick(View param2View) {
                        Intent intent = new Intent(MainActivity.this.getBaseContext(), Login.class);
                        MainActivity.this.startActivity(intent);
                    }
                });
                snackbar.setActionTextColor(MainActivity.this.getResources().getColor(R.color.colorPrimary));
                snackbar.show();
            }
        });
        if (this.firebaseAuth.getCurrentUser() != null) {
            button2.setText("log out");
            button2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View param1View) {
                    String str = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    MainActivity.this.stopnotifyref.child(str).child("token_id").setValue("notlogin");
                    MainActivity.this.lastOnlineRef.setValue(ServerValue.TIMESTAMP);
                    MainActivity.this.firebaseAuth.signOut();
                    MainActivity.this.finish();
                    Intent intent = new Intent(MainActivity.this.getBaseContext(), MainActivity.class);
                    MainActivity.this.startActivity(intent);
                    Toast.makeText(MainActivity.this.getApplicationContext(), "logout", 0).show();
                }
            });
        } else {
            button2.setText("log in");
            button2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View param1View) {
                    Intent intent = new Intent(MainActivity.this.getBaseContext(), Login.class);
                    MainActivity.this.startActivity(intent);
                }
            });
        }
        textView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (MainActivity.this.firebaseAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(MainActivity.this.getBaseContext(), Item_show.class);
                    MainActivity.this.startActivity(intent);
                    return;
                }
                Snackbar snackbar = Snackbar.make(MainActivity.this.relativeLayout, "You Are not Login", 0).setAction("Login", new View.OnClickListener() {
                    public void onClick(View param2View) {
                        Intent intent = new Intent(MainActivity.this.getBaseContext(), Login.class);
                        MainActivity.this.startActivity(intent);
                    }
                });
                snackbar.setActionTextColor(MainActivity.this.getResources().getColor(R.color.colorPrimary));
                snackbar.show();
            }
        });
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.main_menu);

        final ImageButton dawerButton = (ImageButton)getSupportActionBar().getCustomView().findViewById(R.id.drawerbutton);
        this.mDrawer = (FlowingDrawer)findViewById(R.id.drawerlayout);
        this.mDrawer.setTouchMode(1);
        this.mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
            public void onDrawerSlide(float param1Float, int param1Int) { Log.i("MainActivity", "openRatio=" + param1Float + " ,offsetPixels=" + param1Int); }

            public void onDrawerStateChange(int param1Int1, int param1Int2) {
                if (param1Int2 == 0) {
                    Log.i("MainActivity", "Drawer STATE_CLOSED");
                    dawerButton.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.ic_menubar));
                    return;
                }
                if (param1Int2 == 8) {
                    dawerButton.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.ic_back));
                    return;
                }
            }
        });
        this.btnFound.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                Intent intent = new Intent(MainActivity.this.getApplicationContext(), Found_item.class);
                MainActivity.this.startActivity(intent);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                Intent intent = new Intent(MainActivity.this.getApplicationContext(), Lost_item.class);
                MainActivity.this.startActivity(intent);
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (MainActivity.this.mDrawer.isMenuVisible()) {
                    MainActivity.this.mDrawer.closeMenu();
                    return;
                }
                MainActivity.this.mDrawer.openMenu();
            }
        });
        if (this.firebaseAuth.getCurrentUser() != null) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            this.myConnectionsRef = firebaseDatabase.getReference("onlineuser").child(this.firebaseAuth.getCurrentUser().getUid()).child("conection");
            this.lastOnlineRef = firebaseDatabase.getReference("onlineuser").child(this.firebaseAuth.getCurrentUser().getUid()).child("conection");
            firebaseDatabase.getReference(".info/connected").addValueEventListener(new ValueEventListener() {
                public void onCancelled(DatabaseError param1DatabaseError) { System.err.println("Listener was cancelled at .info/connected"); }

                public void onDataChange(DataSnapshot param1DataSnapshot) {
                    if (((Boolean)param1DataSnapshot.getValue(Boolean.class)).booleanValue()) {
                        DatabaseReference databaseReference = MainActivity.this.myConnectionsRef;
                        databaseReference.onDisconnect().removeValue();
                        MainActivity.this.lastOnlineRef.onDisconnect().setValue(ServerValue.TIMESTAMP);
                        databaseReference.setValue(Boolean.TRUE);
                    }
                }
            });
        }
    }
}
