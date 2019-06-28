package com.example.lostandfoundnew;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.core.view.GestureDetectorCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import java.io.ByteArrayOutputStream;

public class UserImage extends AppCompatActivity implements GestureDetector.OnGestureListener {
    Bitmap bitmap;
    GestureDetectorCompat detectorCompat;
    ImageView imgUserProfile;
    RelativeLayout relativeLayout;
    int select = 0;
    public static Bitmap decodeBase64(String paramString) {
        byte[] arrayOfByte = Base64.decode(paramString, 0);
        return BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length);
    }

    public static String encodeTobase64(Bitmap paramBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        paramBitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_user_image);
        hide();
        imgUserProfile = findViewById(R.id.userimagefull);
        relativeLayout = findViewById(R.id.relativelayoutimage);
        bitmap = decodeBase64(getIntent().getExtras().getString("image"));
        imgUserProfile.setImageBitmap(bitmap);
        detectorCompat = new GestureDetectorCompat(this, this);
    }

    public boolean onDown(MotionEvent paramMotionEvent) { return false; }

    @RequiresApi(api = 21)
    public boolean onFling(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2) {
        finishAfterTransition();
        return false;
    }

    public void onLongPress(MotionEvent paramMotionEvent) {}

    public boolean onScroll(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2) { return false; }

    public void onShowPress(MotionEvent paramMotionEvent) {}

    public boolean onSingleTapUp(MotionEvent paramMotionEvent) {
        if (select == 0) {
            relativeLayout.setBackgroundColor(-1);
            select = 1;
            return false;
        }
        relativeLayout.setBackgroundColor(-16777216);
        select = 0;
        return false;
    }

    public boolean onTouchEvent(MotionEvent paramMotionEvent) {
        detectorCompat.onTouchEvent(paramMotionEvent);
        return super.onTouchEvent(paramMotionEvent);
    }
}
