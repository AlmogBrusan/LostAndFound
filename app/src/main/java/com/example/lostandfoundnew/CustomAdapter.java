package com.example.lostandfoundnew;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<ItemModel> {
    TextView address;
    Context context;
    TextView date;
    List<ItemModel> item_model;
    MyViewHolder myViewHolder;
    ImageView profileimage;
    TextView title;

    private static class MyViewHolder {
        TextView address;
        TextView date;
        ImageView profileimage;
        TextView title;

        public MyViewHolder(TextView title, ImageView profileimage, TextView address, TextView date) {
            this.title = title;
            this.profileimage = profileimage;
            this.address = address;
            this.date = date;
        }
    }

    CustomAdapter(Context context, List<ItemModel> item_model) {
        super(context, 0, item_model);
        this.context = context;
        this.item_model = item_model;
    }

    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = LayoutInflater.from(this.context).inflate(R.layout.card_view, parent, false);
            profileimage =  view.findViewById(R.id.main_card_image);
            title =  view.findViewById(R.id.main_card_title);
            address =  view.findViewById(R.id.main_card_address);
            date =  view.findViewById(R.id.main_card_date);
            title.setText((item_model.get(position)).getTitle());
            address.setText((item_model.get(position)).getAddress());
            date.setText(( item_model.get(position)).getDate());
            profileimage.setImageBitmap(decodeBase64((item_model.get(position)).getImageurl1()));
            myViewHolder = new MyViewHolder(title, profileimage, address, date);
            view.setTag( myViewHolder);
            return view;
        }
        MyViewHolder myViewHolder = (MyViewHolder) view.getTag();
        title = myViewHolder.title;
        address = myViewHolder.address;
        date = myViewHolder.date;
        profileimage = myViewHolder.profileimage;
        title.setText((this.item_model.get(position)).getTitle());
        address.setText(( this.item_model.get(position)).getAddress());
        date.setText(( this.item_model.get(position)).getDate());
        profileimage.setImageBitmap(decodeBase64(( this.item_model.get(position)).getImageurl1()));
        return view;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
