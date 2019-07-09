package com.example.lostandfoundnew;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProfileRecyclerAdapter extends RecyclerView.Adapter {
    Context context;

    List<ItemModel> list;

    View.OnClickListener mClickListener;

    ProfileRecyclerAdapter(Context paramContext, List<ItemModel> paramList) {
        this.list = paramList;
        this.context = paramContext;
    }

    public static Bitmap decodeBase64(String paramString) {
        byte[] arrayOfByte = Base64.decode(paramString, 0);
        return BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length);
    }

    public int getItemCount() { return this.list.size(); }

    public void onBindViewHolder(RecyclerView.ViewHolder paramViewHolder, int paramInt) {
        ItemModel item_Model = (ItemModel)this.list.get(paramInt);
        ((MyViewHolder)paramViewHolder).bind(item_Model);
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
        return new MyViewHolder(LayoutInflater.from(this.context).inflate(R.layout.useradds, paramViewGroup, false));
    }

    public void setClickListener(View.OnClickListener paramOnClickListener) {
        mClickListener = paramOnClickListener; }

    private static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtTittle;

        MyViewHolder(View param1View) {
            super(param1View);
            imageView = param1View.findViewById(R.id.addimage);
            txtTittle = param1View.findViewById(R.id.addtittle);
        }

        void bind(ItemModel param1Item_Model) {
            imageView.setImageBitmap(ProfileRecyclerAdapter.decodeBase64(param1Item_Model.getImageurl1()));
            txtTittle.setText(param1Item_Model.getTitle());
            int a=0;
        }
    }
}
