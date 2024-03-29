package com.example.lostandfoundnew;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FirebasePullRequests {

    Context context;

    DatabaseReference databaseReference;

    List<ItemModel> item_models = new ArrayList();

    ListView listView;

    Query query;

    TextView textView;

    FirebaseUser user = null;

    ValueEventListener valueEventListener;

    public FirebasePullRequests(ListView paramListView, Context paramContext, Query paramQuery, DatabaseReference paramDatabaseReference, TextView paramTextView) {
        this.listView = paramListView;
        this.context = paramContext;
        this.query = paramQuery;
        this.databaseReference = paramDatabaseReference;
        this.textView = paramTextView;
    }

    public FirebasePullRequests(List<ItemModel> paramList, ListView paramListView, Context paramContext, Query paramQuery, DatabaseReference paramDatabaseReference, FirebaseUser paramFirebaseUser, TextView paramTextView) {
        this.item_models = paramList;
        this.listView = paramListView;
        this.context = paramContext;
        this.query = paramQuery;
        this.databaseReference = paramDatabaseReference;
        this.user = paramFirebaseUser;
        this.textView = paramTextView;
        this.valueEventListener = this.valueEventListener;
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

    public void getdata() {
        if (this.user == null)
            return;
        this.valueEventListener = new ValueEventListener() {
            public void onCancelled(DatabaseError param1DatabaseError) {}

            public void onDataChange(DataSnapshot param1DataSnapshot) { FirebasePullRequests.this.showdata(param1DataSnapshot); }
        };
        this.query.addValueEventListener(this.valueEventListener);
    }

    public void showdata(DataSnapshot paramDataSnapshot) {
        for (DataSnapshot dataSnapshot : paramDataSnapshot.getChildren()) {
            ItemModel item_Model = new ItemModel();
            item_Model.setTitle(((ItemModel)dataSnapshot.getValue(ItemModel.class)).getTitle());
            item_Model.setAddress(((ItemModel)dataSnapshot.getValue(ItemModel.class)).getAddress());
            item_Model.setLatitude(((ItemModel)dataSnapshot.getValue(ItemModel.class)).latitude);
            item_Model.setLongitude(((ItemModel)dataSnapshot.getValue(ItemModel.class)).longitude);
            item_Model.setDescription(((ItemModel)dataSnapshot.getValue(ItemModel.class)).getDescription());
            item_Model.setCategories(((ItemModel)dataSnapshot.getValue(ItemModel.class)).getCategories());
            item_Model.setStatus(((ItemModel)dataSnapshot.getValue(ItemModel.class)).getStatus());
            item_Model.setId(((ItemModel)dataSnapshot.getValue(ItemModel.class)).getId());
            item_Model.setPhone(((ItemModel)dataSnapshot.getValue(ItemModel.class)).getPhone());
            item_Model.setUser_id(((ItemModel)dataSnapshot.getValue(ItemModel.class)).getUser_id());
            item_Model.setImageurl(((ItemModel)dataSnapshot.getValue(ItemModel.class)).getImageurl());
            this.item_models.add(item_Model);
        }
        if (this.item_models.isEmpty()) {
            this.textView.setVisibility(View.VISIBLE);
            return;
        }
        this.listView.setAdapter(new CustomAdapter(this.context, this.item_models));
    }
}
