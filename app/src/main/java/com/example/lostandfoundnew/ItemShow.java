package com.example.lostandfoundnew;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Pair;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemShow extends AppCompatActivity {

    CustomAdapter adapter;
    CustomAdapter adapter2;
    int count_Selected = 0;
    DatabaseReference databaseReference;
    DatabaseReference databaseReferencelost;
    FirebaseAuth firebaseAuth;
    FirebasePullRequests firebasePullRequests;
    CircleImageView imageprofile;
    String imageprofiledecode;
    List<ItemModel> item_modelList = new ArrayList();
    List<ItemModel> item_modelList_lost = new ArrayList();
    ListView listView2;
    ListView listViewshow;
    List<ItemModel> listdelete = new ArrayList();
    List<ItemModel> listdelete_lost = new ArrayList();
    TextView phonenumber;
    Query query;
    Query query1;
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    TabHost tabHost;
    TextView txtnoDatafound;
    TextView txtnoDatalost;
    TextView txtusername;
    FirebaseUser user;
    TextView useremail;
    DatabaseReference userprofileref;


    @SuppressLint({"ResourceAsColor"})
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_item_show);
        listViewshow = findViewById(R.id. lv);
        listView2 =  findViewById(R.id. lv2);
        tabHost = findViewById(R.id. tabHost);
        imageprofile = findViewById(R.id. imgitemshow);
        txtnoDatafound = findViewById(R.id. txtnodatalostitem);
        txtnoDatalost = findViewById(R.id. txtnodatafounditem);
        relativeLayout = findViewById(R.id. relativelayoutsnack);
        phonenumber = findViewById(R.id. phonenumberprofileitemshow);
        useremail = findViewById(R.id. emailprofileitemshow);
        txtusername = findViewById(R.id. displaynameitemshow);
        tabHost.setup();
        tabHost.getTabWidget();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("Found items");
        tabSpec.setContent(R.id.Lost_items);
        tabSpec.setIndicator("Found items");
        tabHost.addTab(tabSpec);
        tabSpec = tabHost.newTabSpec("Lost items");
        tabSpec.setContent(R.id.Found_items);
        tabSpec.setIndicator("Lost items");
        tabHost.addTab(tabSpec);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        listViewshow.setChoiceMode(3);
        adapter = new CustomAdapter(this, item_modelList);
        listViewshow.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("database");
        query = databaseReference.orderByChild("user_id").equalTo(user.getUid());
        databaseReferencelost = FirebaseDatabase.getInstance().getReference("lostitem");
        query1 = databaseReferencelost.orderByChild("user_id").equalTo(user.getUid());
        userprofileref = FirebaseDatabase.getInstance().getReference("userprofile").child(user.getUid());
        userprofileref.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(DatabaseError param1DatabaseError) {}

            @SuppressLint({"SetTextI18n"})
            public void onDataChange(DataSnapshot param1DataSnapshot) {
                ItemShow.this.useremail.setText(((UserProfileModel)param1DataSnapshot.getValue(UserProfileModel.class)).getEmail());
                ItemShow.this.txtusername.setText(((UserProfileModel)param1DataSnapshot.getValue(UserProfileModel.class)).getFirstName() + " " + ((UserProfileModel)param1DataSnapshot.getValue(UserProfileModel.class)).getLastName());
                ItemShow.this.phonenumber.setText(((UserProfileModel)param1DataSnapshot.getValue(UserProfileModel.class)).getPhonenumber());
                ItemShow.this.imageprofiledecode = ((UserProfileModel)param1DataSnapshot.getValue(UserProfileModel.class)).getImage();
                if (ItemShow.this.imageprofiledecode.equals("no")) {
                    ItemShow.this.imageprofile.setImageBitmap(BitmapFactory.decodeResource(ItemShow.this.getBaseContext().getResources(), 2131623939));
                    return;
                }
                ItemShow.this.imageprofile.setImageBitmap(ItemShow.decodeBase64(ItemShow.this.imageprofiledecode));
            }
        });
        imageprofile.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = 21)
            public void onClick(View param1View) {
                Intent intent = new Intent(ItemShow.this.getBaseContext(), UserImage.class);
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(ItemShow.this, new Pair[] { Pair.create(ItemShow.this.imageprofile, "imageprofile") });
                intent.putExtra("image", ItemShow.encodeTobase64(((BitmapDrawable)ItemShow.this.imageprofile.getDrawable()).getBitmap()));
                ItemShow.this.startActivity(intent, activityOptions.toBundle());
            }
        });
        firebasePullRequests = new FirebasePullRequests(item_modelList, listViewshow, this, query, databaseReference, user, txtnoDatafound);
        firebasePullRequests.getdata();
        listViewshow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
                Intent intent = new Intent(ItemShow.this.getBaseContext(), ItemDetail.class);
                ItemModel item_Model = (ItemModel)ItemShow.this.item_modelList.get(param1Int);
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
                ItemShow.this.startActivity(intent);
            }
        });
        listViewshow.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            public boolean onActionItemClicked(ActionMode param1ActionMode, MenuItem param1MenuItem) {
                switch (param1MenuItem.getItemId()) {
                    default:
                        return false;
                    case 2131296275:
                        break;
                }
                for (ItemModel item_Model : ItemShow.this.listdelete) {
                    ItemShow.this.adapter.remove(item_Model);
                    ItemShow.this.delet_data(item_Model.getId());
                }
                Snackbar.make(ItemShow.this.relativeLayout, ItemShow.this.count_Selected + " items deleted", Snackbar.LENGTH_SHORT).show();
                ItemShow.this.count_Selected = 0;
                param1ActionMode.finish();
                return true;
            }

            public boolean onCreateActionMode(ActionMode param1ActionMode, Menu param1Menu) {
                param1ActionMode.getMenuInflater().inflate(R.menu.delete_menu, param1Menu);
                return true;
            }

            public void onDestroyActionMode(ActionMode param1ActionMode) {
                while (ItemShow.this.count_Selected > 0) {
                    ItemShow.this.listdelete.remove(0);
                    ItemShow item_show = ItemShow.this;
                    item_show.count_Selected--;
                }
            }

            public void onItemCheckedStateChanged(ActionMode param1ActionMode, int param1Int, long param1Long, boolean param1Boolean) {
                boolean bool1;
                boolean bool2 = true;
                if (ItemShow.this.count_Selected == 0) {
                    ItemShow item_show = ItemShow.this;
                    item_show.count_Selected++;
                    param1ActionMode.setTitle(ItemShow.this.count_Selected + " Items selected");
                    ItemShow.this.listdelete.add(ItemShow.this.item_modelList.get(param1Int));
                    return;
                }
                Iterator iterator = ItemShow.this.listdelete.iterator();
                while (true) {
                    bool1 = bool2;
                    if (iterator.hasNext()) {
                        String str = ((ItemModel)iterator.next()).getId();
                        if (((ItemModel)ItemShow.this.item_modelList.get(param1Int)).getId().equals(str)) {
                            ItemShow item_show = ItemShow.this;
                            item_show.count_Selected--;
                            param1ActionMode.setTitle(ItemShow.this.count_Selected + " item selected");
                            ItemShow.this.listdelete.remove(ItemShow.this.item_modelList.get(param1Int));
                            bool1 = false;
                            break;
                        }
                        continue;
                    }
                    break;
                }
                if (bool1 == true) {
                    ItemShow item_show = ItemShow.this;
                    item_show.count_Selected++;
                    param1ActionMode.setTitle(ItemShow.this.count_Selected + " item selected");
                    ItemShow.this.listdelete.add(ItemShow.this.item_modelList.get(param1Int));
                    return;
                }
            }

            public boolean onPrepareActionMode(ActionMode param1ActionMode, Menu param1Menu) { return false; }
        });
        listView2.setChoiceMode(3);
        adapter2 = new CustomAdapter(this, item_modelList_lost);
        listView2.setAdapter(adapter2);
        databaseReferencelost = FirebaseDatabase.getInstance().getReference("lostitem");
        query1 = databaseReferencelost.orderByChild("user_id").equalTo(user.getUid());
        firebasePullRequests = new FirebasePullRequests(item_modelList_lost, listView2, this, query1, databaseReferencelost, user, txtnoDatalost);
        firebasePullRequests.getdata();
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
                Intent intent = new Intent(ItemShow.this.getBaseContext(), ItemDetail.class);
                ItemModel item_Model = (ItemModel)ItemShow.this.item_modelList_lost.get(param1Int);
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
                ItemShow.this.startActivity(intent);
            }
        });
        listView2.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            public boolean onActionItemClicked(ActionMode param1ActionMode, MenuItem param1MenuItem) {
                switch (param1MenuItem.getItemId()) {
                    default:
                        return false;
                    case 2131296275:
                        break;
                }
                for (ItemModel item_Model : ItemShow.this.listdelete_lost) {
                    ItemShow.this.adapter2.remove(item_Model);
                    ItemShow.this.delet_datalost(item_Model.getId());
                }
                Snackbar.make(ItemShow.this.relativeLayout, ItemShow.this.count_Selected + " items deleted", Snackbar.LENGTH_SHORT).show();
                ItemShow.this.count_Selected = 0;
                param1ActionMode.finish();
                return true;
            }

            public boolean onCreateActionMode(ActionMode param1ActionMode, Menu param1Menu) {
                param1ActionMode.getMenuInflater().inflate(R.menu.delete_menu, param1Menu);
                return true;
            }

            public void onDestroyActionMode(ActionMode param1ActionMode) {
                while (ItemShow.this.count_Selected > 0) {
                    ItemShow.this.listdelete_lost.remove(0);
                    ItemShow item_show = ItemShow.this;
                    item_show.count_Selected--;
                }
            }

            public void onItemCheckedStateChanged(ActionMode param1ActionMode, int param1Int, long param1Long, boolean param1Boolean) {
                boolean bool1;
                boolean bool2 = true;
                if (ItemShow.this.count_Selected == 0) {
                    ItemShow item_show = ItemShow.this;
                    item_show.count_Selected++;
                    param1ActionMode.setTitle(ItemShow.this.count_Selected + " Items selected");
                    ItemShow.this.listdelete_lost.add(ItemShow.this.item_modelList_lost.get(param1Int));
                    return;
                }
                Iterator iterator = ItemShow.this.listdelete_lost.iterator();
                while (true) {
                    bool1 = bool2;
                    if (iterator.hasNext()) {
                        String str = ((ItemModel)iterator.next()).getId();
                        if (((ItemModel)ItemShow.this.item_modelList_lost.get(param1Int)).getId().equals(str)) {
                            ItemShow item_show = ItemShow.this;
                            item_show.count_Selected--;
                            param1ActionMode.setTitle(ItemShow.this.count_Selected + " item selected");
                            ItemShow.this.listdelete_lost.remove(ItemShow.this.item_modelList_lost.get(param1Int));
                            bool1 = false;
                            break;
                        }
                        continue;
                    }
                    break;
                }
                if (bool1 == true) {
                    ItemShow item_show = ItemShow.this;
                    item_show.count_Selected++;
                    param1ActionMode.setTitle(ItemShow.this.count_Selected + " item selected");
                    ItemShow.this.listdelete_lost.add(ItemShow.this.item_modelList_lost.get(param1Int));
                    return;
                }
            }

            public boolean onPrepareActionMode(ActionMode param1ActionMode, Menu param1Menu) { return false; }
        });
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

    public void delet_data(String paramString) {
        FirebaseDatabase.getInstance().getReference("database").child(paramString).removeValue(new DatabaseReference.CompletionListener() {
            public void onComplete(DatabaseError param1DatabaseError, DatabaseReference param1DatabaseReference) { Toast.makeText(ItemShow.this.getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show(); }
        });
        recreate();
    }

    public void delet_datalost(String paramString) {
        FirebaseDatabase.getInstance().getReference("lostitem").child(paramString).removeValue(new DatabaseReference.CompletionListener() {
            public void onComplete(DatabaseError param1DatabaseError, DatabaseReference param1DatabaseReference) { Toast.makeText(ItemShow.this.getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show(); }
        });
        recreate();
    }
}
