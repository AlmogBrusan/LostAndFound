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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

public class FoundItem extends AppCompatActivity {
    private static final String TEXT_KEY = "text";

    Activity activity;
    CustomAdapter adopter1;
    Context context;
    DatabaseReference databaseReference;
    private AppCompatEditText editText;
    FirebaseAuth firebaseAuth;
    int index;
    List<ItemModel> item_modelList = new ArrayList();
    private int itempos = 0;
    String lastkeyonetime = "";
    ListView listViewshow;
    int mCurrentPage = 0;
    boolean mLastPage = false;
    boolean mLoading = true;
    int mPreviousTotal = 0;
    int mVisibleThreshold = 5;
    private String mlastkey = "";
    private String prevkey = "";
    String prevkeyonetime = "";
    ProgressBar progressBar;
    Query query;
    int refreashcount = 0;
    RelativeLayout relativeLayout;
    private int swipecount = 0;
    JellyToolbar toolbar1;
    TextView txtinternet_error;
    FirebaseUser user;
    boolean userScrolled = false;
    Button btnHaveFound;
    FloatingActionButton fab;


    @SuppressLint({"NewApi", "ResourceAsColor"})
    //@RequiresApi(api = 23)
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_found_item);

        listViewshow = findViewById(R.id.itemfound);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("database");
        query = databaseReference.orderByKey().limitToLast(10);
        progressBar = findViewById(R.id.foundprogress);
        relativeLayout = findViewById(R.id.item_found_relative);
        adopter1 = new CustomAdapter(this, item_modelList);
        listViewshow.setAdapter(adopter1);
        progressBar.setVisibility(View.VISIBLE);
        btnHaveFound = findViewById(R.id.btnhavefound);
         fab = findViewById(R.id.btntest);
        getSupportActionBar().hide();
        activity = this;
        toolbar1 = findViewById(R.id.toolbarfound);
        toolbar1.setJellyListener(jellyListener1);
        context = this;
        txtinternet_error = findViewById(R.id.txtinterneterror);
        editText = (AppCompatEditText)LayoutInflater.from(this).inflate(R.layout.search_text, null);
        editText.setBackgroundResource(R.color.transparent);
        toolbar1.getToolbar().setPadding(0, getStatusBarHeight(), 0, 0);
        getWindow().getDecorView().setSystemUiVisibility(1280);
        toolbar1.setContentView(editText);
        if (haveNetworkConnection()) {
            txtinternet_error.setVisibility(View.GONE);
            btnHaveFound.setOnClickListener(new View.OnClickListener() {
                public void onClick(View param1View) {
                    if (firebaseAuth.getCurrentUser() != null) {
                        Intent intent1 = new Intent(FoundItem.this, ItemUpload.class);
                        intent1.putExtra("status", 0);
                        startActivity(intent1);
                        return;
                    }
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                }
            });
        } else {
            btnHaveFound.setOnClickListener(new View.OnClickListener() {
                public void onClick(View param1View) { Toast.makeText(FoundItem.this, "Conect to the internet", Toast.LENGTH_SHORT).show(); }
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
                if (userScrolled && param1Int1 + param1Int2 == param1Int3 && scrollcheck != index) {
                    scrollcheck = scrollitem(index);
                    userScrolled = false;
                }
            }

            public void onScrollStateChanged(AbsListView param1AbsListView, int param1Int) {
                if (param1Int == 1)
                    userScrolled = true;
            }
        });
        listViewshow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
                Intent intent = new Intent(getBaseContext(), ItemDetail.class);
                ItemModel item_Model = item_modelList.get(param1Int);
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
                    databaseReference.orderByChild("title").startAt(str).endAt(str + "?").limitToLast(100).addListenerForSingleValueEvent(new ValueEventListener() {
                        public void onCancelled(DatabaseError param2DatabaseError) {}

                        public void onDataChange(DataSnapshot param2DataSnapshot) {
                            item_modelList.clear();
                            for (DataSnapshot dataSnapshot : param2DataSnapshot.getChildren()) {
                                ItemModel item_Model = new ItemModel();
                                item_Model.setImageurl1((dataSnapshot.getValue(ItemModel.class)).getImageurl1());
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
                                if ((dataSnapshot.getValue(ItemModel.class)).getStatus() == 0)
                                    item_modelList.add(item_Model);
                            }
                            if (item_modelList.isEmpty()) {
                                Toast.makeText(getApplicationContext(), " No Found Item availible !", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                return;
                            }
                            listViewshow.setAdapter(new CustomAdapter(getBaseContext(), item_modelList));
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                    FoundItem.hideKeyboard(activity);
                }
                return false;
            }
        });
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

    public static void hideKeyboard(Activity paramActivity) {
        InputMethodManager inputMethodManager = (InputMethodManager)paramActivity.getSystemService(INPUT_METHOD_SERVICE);
        View view2 = paramActivity.getCurrentFocus();
        View view1 = view2;
        if (view2 == null)
            view1 = new View(paramActivity);
        inputMethodManager.hideSoftInputFromWindow(view1.getWindowToken(), 0);
    }

    private void loadmoredata() { FirebaseDatabase.getInstance().getReference("database").orderByKey().endAt(lastkeyonetime).limitToLast(10).addValueEventListener(new ValueEventListener() {
        public void onCancelled(DatabaseError param1DatabaseError) {
            String str = param1DatabaseError.getMessage();
            Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
        }

        public void onDataChange(DataSnapshot param1DataSnapshot) {
            for (DataSnapshot ds : param1DataSnapshot.getChildren()) {
                ItemModel item_Model = new ItemModel();
                item_Model.setTitle((ds.getValue(ItemModel.class)).getTitle());
                item_Model.setAddress((ds.getValue(ItemModel.class)).getAddress());
                item_Model.setLatitude((ds.getValue(ItemModel.class)).latitude);
                item_Model.setLongitude((ds.getValue(ItemModel.class)).longitude);
                item_Model.setDescription((ds.getValue(ItemModel.class)).getDescription());
                item_Model.setCategories((ds.getValue(ItemModel.class)).getCategories());
                item_Model.setStatus((ds.getValue(ItemModel.class)).getStatus());
                item_Model.setId((ds.getValue(ItemModel.class)).getId());
                item_Model.setDate((ds.getValue(ItemModel.class)).getDate());
                item_Model.setPhone((ds.getValue(ItemModel.class)).getPhone());
                item_Model.setUser_id((ds.getValue(ItemModel.class)).getUser_id());
                if ((ds.getValue(ItemModel.class)).getImageurl1().equals("no")) {
                    item_Model.setImageurl1(encodeTobase64(((BitmapDrawable)getResources().getDrawable(R.drawable.no_image_availible)).getBitmap()));
                } else {
                    item_Model.setImageurl1((ds.getValue(ItemModel.class)).getImageurl1());
                }
                String str = ds.getKey();
                if (!prevkeyonetime.equals(str))
                    item_modelList.add(item_Model);
                if (refreashcount == 1) {
                    lastkeyonetime = str;
                     FoundItem foundItem = FoundItem.this;
                    foundItem.refreashcount--;
                }
                if (prevkeyonetime.equals(str))
                    prevkeyonetime = lastkeyonetime;
            }
            if (item_modelList.isEmpty()) {
                Toast.makeText(getApplicationContext(), " No Found Item availible !", Toast.LENGTH_SHORT).show();
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
        if (this.swipecount == 0) {
            this.lastkeyonetime = this.mlastkey;
            this.prevkeyonetime = this.prevkey;
            this.swipecount++;
            return paramInt;
        }
        this.refreashcount++;
        this.progressBar.setVisibility(View.VISIBLE);
        this.listViewshow.setEnabled(false);
        loadmoredata();
        return paramInt;
    }

    private void showdata() { this.query.addValueEventListener(new ValueEventListener() {
        public void onCancelled(DatabaseError param1DatabaseError) {
            String str = param1DatabaseError.getMessage().toString();
            Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
        }

        public void onDataChange(DataSnapshot param1DataSnapshot) {
            item_modelList.clear();
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
                    item_Model.setImageurl1(FoundItem.encodeTobase64(((BitmapDrawable)getResources().getDrawable(R.drawable.no_image_availible)).getBitmap()));
                } else {
                    item_Model.setImageurl1((dataSnapshot.getValue(ItemModel.class)).getImageurl1());
                }

                if (itempos == 1) {
                    String str = dataSnapshot.getKey();
//                    FoundItem.access$402(FoundItem.this, str);
//                    FoundItem.access$502(FoundItem.this, str);
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
        editText.setText(paramBundle.getString("text"));
        editText.setSelection(editText.getText().length());
    }

    protected void onSaveInstanceState(Bundle paramBundle) {
        paramBundle.putString("text", editText.getText().toString());
        super.onSaveInstanceState(paramBundle);
    }
}
