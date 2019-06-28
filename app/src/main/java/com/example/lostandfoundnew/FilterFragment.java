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
    DatabaseReference databaseReference;
    Query query;
    ValueEventListener valueEventListener;
    TextView docCategory;
    TextView animalCategory;
    TextView keyCategory;
    TextView otherCategory;
    TextView phoneCategory;
    int all_filterselect = 1;
    int animal_filterselect = 0;
    int doc_filterselect = 0;
    int key_filterselect = 0;
    int other_filterselect = 0;
    int phone_filterselect = 0;
    

    public static FilterFragment newInstance() { return new FilterFragment(); }

    public void setupDialog(Dialog paramDialog, int paramInt) {
        View view = View.inflate(getContext(), R.layout.fragment_filter, null);
        RelativeLayout relativeLayout = view.findViewById(R.id.rl_content);
        LinearLayout linearLayout = view.findViewById(R.id.ll_buttons);
        keyCategory = view.findViewById(R.id.keys_cat);
        docCategory = view.findViewById(R.id.keys_doc);
        phoneCategory = view.findViewById(R.id.keys_phone);
        animalCategory = view.findViewById(R.id.keys_animal);
        otherCategory = view.findViewById(R.id.keys_other);
        keyCategory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (key_filterselect == 0) {
                    keyCategory.setBackground(getResources().getDrawable(R.drawable.filter_select));
                    key_filterselect = 1;
                    return;
                }
                keyCategory.setBackground(getResources().getDrawable(R.drawable.filter));
                key_filterselect = 0;
            }
        });
        this.docCategory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (doc_filterselect == 0) {
                    docCategory.setBackground(getResources().getDrawable(R.drawable.filter_select));
                    doc_filterselect = 1;
                    return;
                }
                docCategory.setBackground(getResources().getDrawable(R.drawable.filter));
                doc_filterselect = 0;
            }
        });
        this.phoneCategory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (phone_filterselect == 0) {
                    phoneCategory.setBackground(getResources().getDrawable(R.drawable.filter_select));
                    phone_filterselect = 1;
                    return;
                }
                phoneCategory.setBackground(getResources().getDrawable(R.drawable.filter));
                phone_filterselect = 0;
            }
        });
        this.animalCategory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (animal_filterselect == 0) {
                    animalCategory.setBackground(getResources().getDrawable(R.drawable.filter_select));
                    animal_filterselect = 1;
                    return;
                }
                animalCategory.setBackground(getResources().getDrawable(R.drawable.filter));
                animal_filterselect = 0;
            }
        });
        this.otherCategory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (other_filterselect == 0) {
                    otherCategory.setBackground(getResources().getDrawable(R.drawable.filter_select));
                    other_filterselect = 1;
                    return;
                }
                otherCategory.setBackground(getResources().getDrawable(R.drawable.filter));
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
