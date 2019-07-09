package com.example.lostandfoundnew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.provider.Telephony;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;


public class MainActivity extends AppCompatActivity implements UserAdapter.RecyclerCallBack {
    public static final int ERROR_DIALOG_REQ = 9001;

    Button btnFound, btnLost, btnLoginMenu;
    FirebaseAuth firebaseAuth;
    DatabaseReference lastOnlineRef;
    Location location;
    private LocationManager locationManager;
    FlowingDrawer mDrawer;
    DatabaseReference myConnectionsRef;
    RelativeLayout relativeLayout;
    SlidingRootNavBuilder slidingRootNavBuilder;
    DatabaseReference stopnotifyref;
    FragmentManager fragmentManager = getSupportFragmentManager();
    TextView txtaboutus;
    TextView txtpostfound;
    TextView txtpostlost;
    TextView tVinboxMenu, tVprofileMenu;
    ImageButton drawerButton;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_main);

        btnFound = findViewById(R.id.btnfound);
        btnLost = findViewById(R.id.btnlost);
        btnLoginMenu = findViewById(R.id.btnloginmenu);
        tVinboxMenu = findViewById(R.id.txtviewinboxmenu);
        firebaseAuth = FirebaseAuth.getInstance();
        tVprofileMenu = findViewById(R.id.txtviewprofilemenu);
        relativeLayout = findViewById(R.id.mainrelativelayout);
        txtpostfound = findViewById(R.id.txtpostfound);
        txtpostlost = findViewById(R.id.txtpostlost);
        txtaboutus = findViewById(R.id.txtaboutus);

        stopnotifyref = FirebaseDatabase.getInstance().getReference("token");

        txtaboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AboutUs.class);
                startActivity(intent);
            }
        });


        tVinboxMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (firebaseAuth.getCurrentUser() != null) {

                    getSupportFragmentManager().beginTransaction().add(R.id.chatContainer,new ChatsFragment()).commit();
                    return;

                }

                Snackbar snackbar = Snackbar.make(relativeLayout, "You Are not Login", Snackbar.LENGTH_SHORT).setAction("Login", new View.OnClickListener() {
                    public void onClick(View param2View) {
                        Intent intent = new Intent(getBaseContext(), Login.class);
                        startActivity(intent);
                    }
                });
                snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));
                snackbar.show();
            }
        });

        txtpostfound.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(getBaseContext(), ItemUpload.class);
                    intent.putExtra("status", 0);
                    startActivity(intent);
                    return;
                }
                Snackbar snackbar = Snackbar.make(relativeLayout, "You Are not Login", Snackbar.LENGTH_LONG).setAction("Login", new View.OnClickListener() {
                    public void onClick(View param2View) {
                        Intent intent = new Intent(getBaseContext(), Login.class);
                        startActivity(intent);
                    }
                });
                snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));
                snackbar.show();
            }
        });
        txtpostlost.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(getBaseContext(), ItemUpload.class);
                    intent.putExtra("status", 1);
                    startActivity(intent);
                    return;
                }
                Snackbar snackbar = Snackbar.make(relativeLayout, "You Are not Login", Snackbar.LENGTH_SHORT).setAction("Login", new View.OnClickListener() {
                    public void onClick(View param2View) {
                        Intent intent = new Intent(getBaseContext(), Login.class);
                        startActivity(intent);
                    }
                });
                snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));
                snackbar.show();
            }
        });
        if (firebaseAuth.getCurrentUser() != null) {
            btnLoginMenu.setText(getResources().getString(R.string.logout));
            btnLoginMenu.setOnClickListener(new View.OnClickListener() {
                public void onClick(View param1View) {
                    String str = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    stopnotifyref.child(str).child("token_id").setValue("notlogin");
                    lastOnlineRef.setValue(ServerValue.TIMESTAMP);
                    firebaseAuth.signOut();
                    finish();
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "logout", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            btnLoginMenu.setText("log in");
            btnLoginMenu.setOnClickListener(new View.OnClickListener() {
                public void onClick(View param1View) {
                    Intent intent = new Intent(getBaseContext(), Login.class);
                    startActivity(intent);
                }
            });
        }
        tVprofileMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(getBaseContext(), ItemShow.class);
                    startActivity(intent);
                    return;
                }
                Snackbar snackbar = Snackbar.make(relativeLayout, "You Are not Login", Snackbar.LENGTH_SHORT).setAction("Login", new View.OnClickListener() {
                    public void onClick(View param2View) {
                        Intent intent = new Intent(getBaseContext(), Login.class);
                        startActivity(intent);
                    }
                });
                snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));
                snackbar.show();
            }
        });

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.main_menu);


        drawerButton = getSupportActionBar().getCustomView().findViewById(R.id.drawerbutton);


        mDrawer = findViewById(R.id.drawerlayout);
        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
               mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {

                    public void onDrawerSlide(float param1Float, int param1Int)
                    { Log.i("MainActivity", "openRatio=" + param1Float + " ,offsetPixels=" + param1Int); }

                    public void onDrawerStateChange(int param1Int1, int param1Int2) {
                        if (param1Int2 == ElasticDrawer.STATE_CLOSED) {
                            Log.i("MainActivity", "Drawer STATE_CLOSED");
                            drawerButton.setBackground(getResources().getDrawable(R.drawable.ic_menu_black_24dp));

                        }
                        if (param1Int2 == ElasticDrawer.STATE_OPEN) {
                            drawerButton.setBackground(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));

                        }
                    }
                });


        btnFound.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                Intent intent = new Intent(getApplicationContext(), FoundItem.class);
                startActivity(intent);
            }
        });
        btnLost.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                Intent intent = new Intent(getApplicationContext(), LostItem.class);
                startActivity(intent);
            }
        });
        drawerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (mDrawer.isMenuVisible()) {
                    mDrawer.closeMenu();
                    }
                mDrawer.openMenu();
            }
        });
        if (firebaseAuth.getCurrentUser() != null) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            myConnectionsRef = firebaseDatabase.getReference("onlineuser").child(firebaseAuth.getCurrentUser().getUid()).child("conection");
            lastOnlineRef = firebaseDatabase.getReference("onlineuser").child(firebaseAuth.getCurrentUser().getUid()).child("conection");
            firebaseDatabase.getReference(".info/connected").addValueEventListener(new ValueEventListener() {
                public void onCancelled(DatabaseError param1DatabaseError)
                { System.err.println("Listener was cancelled at .info/connected"); }

                public void onDataChange(DataSnapshot param1DataSnapshot) {
                    if (((Boolean)param1DataSnapshot.getValue(Boolean.class)).booleanValue()) {
                        DatabaseReference databaseReference = myConnectionsRef;
                        databaseReference.onDisconnect().removeValue();
                        lastOnlineRef.onDisconnect().setValue(ServerValue.TIMESTAMP);
                        databaseReference.setValue(Boolean.TRUE);
                    }
                }
            });
        }
    }



    @Override
        public void ChatFragmentOnItemClicked(String userId) {

        MessageFragment messageFragment = MessageFragment.newInstance( userId );
        //fragment for chat getting user id
        fragmentManager.beginTransaction().add( R.id.chatContainer, messageFragment, "11" ).addToBackStack( null ).commit();

    }


}
