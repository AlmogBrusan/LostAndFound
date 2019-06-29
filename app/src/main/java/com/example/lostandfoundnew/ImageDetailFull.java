package com.example.lostandfoundnew;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.ByteArrayOutputStream;

public class ImageDetailFull extends AppCompatActivity {
    int dotcount;
    ImageView[] dots;
    int i;
    String image1;
    String image2;
    String image3;
    String image4;
    String[] imagelist;
    String push_id;
    LinearLayout sliderDotspanel;
    int status;
    LinearLayout toolbaritemdetail;
    ViewPager viewPager;

    ViewpagerAdapterFull viewpagerAdaptor;

    public static String encodeTobase64(Bitmap paramBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        paramBitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
    }

    private void showimageslider() {
        if (status == 1) {
            FirebaseDatabase.getInstance().getReference("lostitem").child(this.push_id).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onCancelled(DatabaseError param1DatabaseError) {}

                public void onDataChange(DataSnapshot param1DataSnapshot) {
                    new Item_Model();
                    image1 = (param1DataSnapshot.getValue(Item_Model.class)).getImageurl1();
                    image2 = (param1DataSnapshot.getValue(Item_Model.class)).getImageurl2();
                    image3 = (param1DataSnapshot.getValue(Item_Model.class)).getImageurl3();
                    image4 = (param1DataSnapshot.getValue(Item_Model.class)).getImageurl4();
                    if (image4.equals("no")) {
                        imagelist = new String[] { image1, image2, image3 };
                    } else {
                        imagelist = new String[] { image1, image2, image3, image4 };
                    }
                    if (image3.equals("no"))
                        imagelist = new String[] { image1, image2 };
                    if (image2.equals("no"))
                        imagelist = new String[] { image1 };
                    if (image1.equals("no")) {
                        Bitmap bitmap = ((BitmapDrawable)getResources().getDrawable(R.mipmap.no_image_availible)).getBitmap();
                        image1 = ImageDetailFull.encodeTobase64(bitmap);
                        imagelist = new String[] { image1 };
                    }
                    viewpagerAdaptor = new ViewpagerAdapterFull(imagelist, getApplicationContext());
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
                }
            });
            return;
        }
        if (status == 0) {
            FirebaseDatabase.getInstance().getReference("database").child(push_id).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onCancelled(DatabaseError param1DatabaseError) {}

                public void onDataChange(DataSnapshot param1DataSnapshot) {
                    new Item_Model();
                    image1 = (param1DataSnapshot.getValue(Item_Model.class)).getImageurl1();
                    image2 = (param1DataSnapshot.getValue(Item_Model.class)).getImageurl2();
                    image3 = (param1DataSnapshot.getValue(Item_Model.class)).getImageurl3();
                    image4 = (param1DataSnapshot.getValue(Item_Model.class)).getImageurl4();
                    if (image4.equals("no")) {
                        imagelist = new String[] { image1, image2, image3 };
                    } else {
                        imagelist = new String[] { image1, image2, image3, image4 };
                    }
                    if (image3.equals("no"))
                        imagelist = new String[] { image1, image2 };
                    if (image2.equals("no"))
                        imagelist = new String[] { image1 };
                    if (image1.equals("no")) {
                        Bitmap bitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.no_image_availible)).getBitmap();
                        image1 = ImageDetailFull.encodeTobase64(bitmap);
                        imagelist = new String[] { image1 };
                    }
                    viewpagerAdaptor = new ViewpagerAdapterFull(imagelist, getApplicationContext());
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
                }
            });
            return;
        }
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_image_detail_full);
        viewPager = findViewById(R.id.imagedetailfull);
        paramBundle = getIntent().getExtras();
        i = ((Integer)paramBundle.get("imageposition")).intValue();
        status = ((Integer)paramBundle.get("status")).intValue();
        push_id = (String)paramBundle.get("push_id");
        sliderDotspanel = findViewById(R.id.sliderdotsfull);
        showimageslider();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int param1Int) {}

            public void onPageScrolled(int param1Int1, float param1Float, int param1Int2) {}

            public void onPageSelected(int param1Int) {
                for (byte b = 0; b < dotcount; b++)
                    dots[b].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),  R.drawable.non_active_dot));
                dots[param1Int].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }
        });
    }
}
