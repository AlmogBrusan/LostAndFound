package com.example.lostandfoundnew;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;

public class ViewpagerAdapter extends PagerAdapter {
    private Context context;

    String[] imgArray = new String[0];

    private LayoutInflater layoutInflater;

    public ViewpagerAdapter(String[] paramArrayOfString, Context paramContext) {
        this.imgArray = paramArrayOfString;
        this.context = paramContext;
    }

    public static Bitmap decodeBase64(String paramString) {
        byte[] arrayOfByte = Base64.decode(paramString, 0);
        return BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length);
    }

    public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject) { paramViewGroup.removeView((RelativeLayout)paramObject); }

    public int getCount() { return this.imgArray.length; }

    public Object instantiateItem(ViewGroup paramViewGroup, int paramInt) {
        if (this.imgArray[paramInt].equals("no"))
            return null;
        Context context1 = this.context;
        this.layoutInflater = (LayoutInflater)context1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = this.layoutInflater.inflate(R.layout.swipe_layout, paramViewGroup, false);
        ((ImageView)view.findViewById(R.id.imgview_pager)).setImageBitmap(decodeBase64(this.imgArray[paramInt]));
        paramViewGroup.addView(view);
        return view;
    }

    public boolean isViewFromObject(View paramView, Object paramObject) { return (paramView == (RelativeLayout)paramObject); }
}
