package com.example.lostandfoundnew;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.yalantis.jellytoolbar.listener.JellyListener;
import com.yalantis.jellytoolbar.widget.JellyToolbar;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LostItem extends AppCompatActivity {

    private static final String TEXT_KEY = "text";
    private static final int TOTAL_ITEM_TO_LOAD = 10;
    CustomAdapter adopter1;
    Context context;
    DatabaseReference databaseReference;
    private AppCompatEditText editText;
    FirebaseAuth firebaseAuth;
    int index = 0;
    List<Item_Model> item_modelList = new ArrayList();
    private int itempos = 0;
    String lastkeyonetime = "";
    ListView listViewshow;
    private String mlastkey = "";
    private String prevkey = "";
    String prevkeyonetime = "";
    ProgressBar progressBar;
    Query query;
    int refreashcount = 0;
    RelativeLayout relativeLayout;
    private int swipecount = 0;
    JellyToolbar toolbar1;
    FirebaseUser user;
    boolean userScrolled = false;
    ValueEventListener valueEventListener;


    @SuppressLint({"ResourceAsColor"})
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_lost_item);
        listViewshow = findViewById(R.id.itemlost);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        FirebaseDatabase.getInstance();
        progressBar = findViewById(R.id.lostprogress);
        relativeLayout = findViewById(R.id.itemlostrelative);
        adopter1 = new CustomAdapter(this, item_modelList);
        listViewshow.setAdapter(adopter1);
        progressBar.setVisibility(View.VISIBLE);
        Button button = findViewById(R.id.btnhavelost);
        final FloatingActionButton fab = findViewById(R.id.btnfilterlost);
        databaseReference = FirebaseDatabase.getInstance().getReference("lostitem");
        query = databaseReference.orderByKey().limitToLast(10);
        TextView textView = findViewById(R.id.txtinterneterrorlost);
        getSupportActionBar().hide();
        toolbar1 = findViewById(R.id.toolbarlost);
        toolbar1.setJellyListener(jellyListener1);
        editText = (AppCompatEditText)LayoutInflater.from(this).inflate(R.layout.search_text, null);
        editText.setBackgroundResource(R.color.transparent);
        toolbar1.getToolbar().setPadding(0, getStatusBarHeight(), 0, 0);
        getWindow().getDecorView().setSystemUiVisibility(1280);
        toolbar1.setContentView(editText);
        if (haveNetworkConnection()) {
            textView.setVisibility(View.GONE);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View param1View) {
                    if (firebaseAuth.getCurrentUser() != null) {
                        Intent intent1 = new Intent(context, ItemUpload.class);
                        intent1.putExtra("status", 1);
                        startActivity(intent1);
                        return;
                    }
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                }
            });
        } else {
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View param1View) { Toast.makeText(context, "Conect to the internet", Toast.LENGTH_SHORT).show(); }
            });
        }
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                FilterFragment filterFragment = FilterFragment.newInstance();
                filterFragment.setParentFab(fab);
                filterFragment.show(getSupportFragmentManager(), filterFragment.getTag());
            }
        });
        fab.hide();
        showdata();
        listViewshow.setOnScrollListener(new AbsListView.OnScrollListener() {
            int scrollcheck = -1;

            public void onScroll(AbsListView param1AbsListView, int param1Int1, int param1Int2, int param1Int3) {
                index = param1Int1;
                if (userScrolled && param1Int1 + param1Int2 == param1Int3 && this.scrollcheck != index) {
                    scrollcheck = scrollitem(index);
                    userScrolled = false;
                }
            }

            public void onScrollStateChanged(AbsListView param1AbsListView, int param1Int) {
                if (param1Int == 1)
                    LostItem.this.userScrolled = true;
            }
        });
        listViewshow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
                Intent intent = new Intent(getBaseContext(), ItemDetail.class);
                Item_Model item_Model = item_modelList.get(param1Int);
                intent.putExtra("title", item_Model.getTitle());
                intent.putExtra("address", item_Model.getAddress());
                intent.putExtra("categories", item_Model.getCategories());
                intent.putExtra("longitude", item_Model.getLongitude());
                intent.putExtra("latitude", item_Model.getLatitude());
                intent.putExtra("description", item_Model.getDescription());
                intent.putExtra("Status", item_Model.getStatus());
                intent.putExtra("phone", item_Model.getPhone());
                intent.putExtra("user_id", item_Model.getUser_id());
                intent.putExtra("push_id", item_Model.getId());
                startActivity(intent);
            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView param1TextView, int param1Int, KeyEvent param1KeyEvent) {
                if (param1Int == 3 || param1Int == 6 || param1KeyEvent.getAction() == 0 || param1KeyEvent.getAction() == 66) {
                    item_modelList.clear();
                    String str = editText.getText().toString().trim();
                    databaseReference.orderByChild("title").startAt(str).endAt(str + "?").addListenerForSingleValueEvent(new ValueEventListener() {
                        public void onCancelled(DatabaseError param2DatabaseError) {}

                        public void onDataChange(DataSnapshot param2DataSnapshot) {
                            item_modelList.clear();
                            for (DataSnapshot dataSnapshot : param2DataSnapshot.getChildren()) {
                                Item_Model item_Model = new Item_Model();
                                item_Model.setTitle((dataSnapshot.getValue(Item_Model.class)).getTitle());
                                item_Model.setAddress((dataSnapshot.getValue(Item_Model.class)).getAddress());
                                item_Model.setLatitude((dataSnapshot.getValue(Item_Model.class)).latitude);
                                item_Model.setLongitude((dataSnapshot.getValue(Item_Model.class)).longitude);
                                item_Model.setDescription((dataSnapshot.getValue(Item_Model.class)).getDescription());
                                item_Model.setCategories((dataSnapshot.getValue(Item_Model.class)).getCategories());
                                item_Model.setStatus((dataSnapshot.getValue(Item_Model.class)).getStatus());
                                item_Model.setId((dataSnapshot.getValue(Item_Model.class)).getId());
                                item_Model.setDate((dataSnapshot.getValue(Item_Model.class)).getDate());
                                item_Model.setPhone((dataSnapshot.getValue(Item_Model.class)).getPhone());
                                item_Model.setUser_id((dataSnapshot.getValue(Item_Model.class)).getUser_id());
                                item_Model.setImageurl1((dataSnapshot.getValue(Item_Model.class)).getImageurl1());
                                if ((dataSnapshot.getValue(Item_Model.class)).getStatus() == 1)
                                    item_modelList.add(item_Model);
                            }
                            if (item_modelList.isEmpty()) {
                                progressBar.setVisibility(View.GONE);
                                return;
                            }
                            listViewshow.setAdapter(new CustomAdapter(getBaseContext(), item_modelList));
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                    FoundItem.hideKeyboard(LostItem.this);
                }
                return false;
            }
        });
    }

    public static String encodeTobase64(Bitmap paramBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        paramBitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
    }

    private int getStatusBarHeight() {
        int i = 0;
        int j = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (j > 0)
            i = getResources().getDimensionPixelSize(j);
        return i;
    }

    private JellyListener jellyListener1 = new JellyListener() {
        public void onCancelIconClicked() {
            if (TextUtils.isEmpty(editText.getText())) {
                toolbar1.collapse();
                return;
            }
            editText.getText().clear();
            item_modelList.clear();
            showdata();
        }
    };

    private boolean haveNetworkConnection() {
        boolean bool = false;
        byte b3 = 0;
        byte b2 = 0;
        NetworkInfo[] arrayOfNetworkInfo = ((ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE)).getAllNetworkInfo();
        int i = arrayOfNetworkInfo.length;
        byte b1 = 0;
        while (b1 < i) {
            NetworkInfo networkInfo = arrayOfNetworkInfo[b1];
            byte b = b3;
            if (networkInfo.getTypeName().equalsIgnoreCase("WIFI")) {
                b = b3;
                if (networkInfo.isConnected())
                    b = 1;
            }
            b3 = b2;
            if (networkInfo.getTypeName().equalsIgnoreCase("MOBILE")) {
                b3 = b2;
                if (networkInfo.isConnected())
                    b3 = 1;
            }
            b1++;
            b2 = b3;
            b3 = b;
        }
        if (b3 != 0 || b2 != 0)
            bool = true;
        return bool;
    }

    private void loadmoredata() { FirebaseDatabase.getInstance().getReference("lostitem").orderByKey().endAt(this.lastkeyonetime).limitToLast(TOTAL_ITEM_TO_LOAD).addValueEventListener(new ValueEventListener() {
        public void onCancelled(DatabaseError param1DatabaseError) {
            String str = param1DatabaseError.getMessage().toString();
            Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
        }

        public void onDataChange(DataSnapshot param1DataSnapshot) {
            for (DataSnapshot ds : param1DataSnapshot.getChildren()) {
                Item_Model item_Model = new Item_Model();
                item_Model.setTitle((ds.getValue(Item_Model.class)).getTitle());
                item_Model.setAddress((ds.getValue(Item_Model.class)).getAddress());
                item_Model.setLatitude((ds.getValue(Item_Model.class)).latitude);
                item_Model.setLongitude((ds.getValue(Item_Model.class)).longitude);
                item_Model.setDescription((ds.getValue(Item_Model.class)).getDescription());
                item_Model.setCategories((ds.getValue(Item_Model.class)).getCategories());
                item_Model.setStatus((ds.getValue(Item_Model.class)).getStatus());
                item_Model.setId((ds.getValue(Item_Model.class)).getId());
                item_Model.setDate((ds.getValue(Item_Model.class)).getDate());
                item_Model.setPhone((ds.getValue(Item_Model.class)).getPhone());
                item_Model.setUser_id((ds.getValue(Item_Model.class)).getUser_id());
                if ((ds.getValue(Item_Model.class)).getImageurl1().equals("no")) {
                    item_Model.setImageurl1(LostItem.encodeTobase64(((BitmapDrawable)getResources().getDrawable(R.drawable.no_image_availible)).getBitmap()));
                } else {
                    item_Model.setImageurl1((ds.getValue(Item_Model.class)).getImageurl1());
                }
                String str = ds.getKey();
                if (!prevkeyonetime.equals(str))
                    item_modelList.add(item_Model);
                if (refreashcount > 0) {
                    lastkeyonetime = str;
                    refreashcount = 0;
                }
                if (prevkeyonetime.equals(str)) {
                    prevkeyonetime = lastkeyonetime;
                    refreashcount = 0;
                }
            }
            if (item_modelList.isEmpty()) {
                Toast.makeText(getApplicationContext(), "No More Items", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }
            listViewshow.setAdapter(new CustomAdapter(getBaseContext(), item_modelList));
            listViewshow.setSelection(index);
            progressBar.setVisibility(View.GONE);
            listViewshow.setEnabled(true);
        }
    }); }

    private int scrollitem(int paramInt) {
        if (swipecount == 0) {
            lastkeyonetime = mlastkey;
            prevkeyonetime = prevkey;
            swipecount++;
            return paramInt;
        }
        refreashcount++;
        progressBar.setVisibility(View.VISIBLE);
        listViewshow.setEnabled(false);
        loadmoredata();
        return paramInt;
    }

    private void showdata() { this.query.addValueEventListener(new ValueEventListener() {
        public void onCancelled(DatabaseError param1DatabaseError) {
            String str = param1DatabaseError.getMessage();
            Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
        }

        public void onDataChange(DataSnapshot param1DataSnapshot) {
            LostItem.this.item_modelList.clear();
            for (DataSnapshot dataSnapshot : param1DataSnapshot.getChildren()) {
                Item_Model item_Model = new Item_Model();
                item_Model.setTitle((dataSnapshot.getValue(Item_Model.class)).getTitle());
                item_Model.setAddress((dataSnapshot.getValue(Item_Model.class)).getAddress());
                item_Model.setLatitude((dataSnapshot.getValue(Item_Model.class)).latitude);
                item_Model.setLongitude((dataSnapshot.getValue(Item_Model.class)).longitude);
                item_Model.setDescription((dataSnapshot.getValue(Item_Model.class)).getDescription());
                item_Model.setCategories((dataSnapshot.getValue(Item_Model.class)).getCategories());
                item_Model.setStatus((dataSnapshot.getValue(Item_Model.class)).getStatus());
                item_Model.setId((dataSnapshot.getValue(Item_Model.class)).getId());
                item_Model.setDate((dataSnapshot.getValue(Item_Model.class)).getDate());
                item_Model.setPhone((dataSnapshot.getValue(Item_Model.class)).getPhone());
                item_Model.setUser_id((dataSnapshot.getValue(Item_Model.class)).getUser_id());
                if ((dataSnapshot.getValue(Item_Model.class)).getImageurl1().equals("no")) {
                    item_Model.setImageurl1(LostItem.encodeTobase64(((BitmapDrawable)LostItem.this.getResources().getDrawable(R.drawable.no_image_availible)).getBitmap()));
                } else {
                    item_Model.setImageurl1((dataSnapshot.getValue(Item_Model.class)).getImageurl1());
                }

                item_modelList.add(item_Model);
            }
            if (item_modelList.isEmpty()) {
                Toast.makeText(getApplicationContext(), " No Found Item availible !", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }
            Collections.reverse(item_modelList);
            listViewshow.setAdapter(new CustomAdapter(getBaseContext(), item_modelList));
            adopter1.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        }
    }); }



    protected void onRestoreInstanceState(Bundle paramBundle) {
        super.onRestoreInstanceState(paramBundle);
        this.editText.setText(paramBundle.getString("text"));
        this.editText.setSelection(this.editText.getText().length());
    }

    protected void onSaveInstanceState(Bundle paramBundle) {
        paramBundle.putString("text", this.editText.getText().toString());
        super.onSaveInstanceState(paramBundle);
    }

    void stopscroling(boolean paramBoolean) {
        if (paramBoolean) {
            this.listViewshow.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View param1View, MotionEvent param1MotionEvent) { return true; }
            });
            return;
        }
        this.listViewshow.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View param1View, MotionEvent param1MotionEvent) { return false; }
        });
    }
}
