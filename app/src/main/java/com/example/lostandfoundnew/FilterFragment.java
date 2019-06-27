package com.example.lostandfoundnew;

import android.app.Dialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FilterFragment extends AAH_FabulousFragment {
    int all_filterselect = 1;

    TextView animal_cat;

    int animal_filterselect = 0;

    DatabaseReference databaseReference;

    TextView doc_cat;

    int doc_filterselect = 0;

    int key_filterselect = 0;

    TextView keycat;

    TextView other_cat;

    int other_filterselect = 0;

    TextView phone_cat;

    int phone_filterselect = 0;

    Query query;

    ValueEventListener valueEventListener;

    public static FilterFragment newInstance() { return new FilterFragment(); }

    public void setupDialog(Dialog paramDialog, int paramInt) {
        View view = View.inflate(getContext(), R.layout.fragment_filter, null);
        RelativeLayout relativeLayout = view.findViewById(R.id.rl_content);
        LinearLayout linearLayout = view.findViewById(R.id.ll_buttons);
        keycat = view.findViewById(R.id.keys_cat);
        doc_cat = view.findViewById(R.id.keys_doc);
        phone_cat = view.findViewById(R.id.keys_phone);
        animal_cat = view.findViewById(R.id.keys_animal);
        other_cat = view.findViewById(R.id.keys_other);
        keycat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (key_filterselect == 0) {
                    keycat.setBackground(getResources().getDrawable(R.drawable.filter_select));
                    key_filterselect = 1;
                    return;
                }
                keycat.setBackground(getResources().getDrawable(R.drawable.filter));
                key_filterselect = 0;
            }
        });
        this.doc_cat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (doc_filterselect == 0) {
                    doc_cat.setBackground(getResources().getDrawable(R.drawable.filter_select));
                    doc_filterselect = 1;
                    return;
                }
                doc_cat.setBackground(getResources().getDrawable(R.drawable.filter));
                doc_filterselect = 0;
            }
        });
        this.phone_cat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (phone_filterselect == 0) {
                    phone_cat.setBackground(getResources().getDrawable(R.drawable.filter_select));
                    phone_filterselect = 1;
                    return;
                }
                phone_cat.setBackground(getResources().getDrawable(R.drawable.filter));
                phone_filterselect = 0;
            }
        });
        this.animal_cat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (animal_filterselect == 0) {
                    animal_cat.setBackground(getResources().getDrawable(R.drawable.filter_select));
                    animal_filterselect = 1;
                    return;
                }
                animal_cat.setBackground(getResources().getDrawable(R.drawable.filter));
                animal_filterselect = 0;
            }
        });
        this.other_cat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (other_filterselect == 0) {
                    other_cat.setBackground(getResources().getDrawable(R.drawable.filter_select));
                    other_filterselect = 1;
                    return;
                }
                other_cat.setBackground(getResources().getDrawable(R.drawable.filter));
                other_filterselect = 0;
            }
        });
        view.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) { closeFilter("closed"); }
        });
        setAnimationDuration(600);
        setPeekHeight(300);
        setViewgroupStatic(linearLayout);
        setViewMain(relativeLayout);
        setMainContentView(view);
        super.setupDialog(paramDialog, paramInt);
    }
}
