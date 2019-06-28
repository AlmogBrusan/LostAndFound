package com.example.lostandfoundnew;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

public class ClickViewpager extends ViewPager {

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int i);
    }

    private class TapGestureListener extends GestureDetector.SimpleOnGestureListener {
        private TapGestureListener() {
        }

        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (ClickViewpager.this.mOnItemClickListener != null) {
                ClickViewpager.this.mOnItemClickListener.onItemClick(ClickViewpager.this.getCurrentItem());
            }
            return true;
        }
    }

    public ClickViewpager(Context context) {
        super(context);
        setup();
    }

    public ClickViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    private void setup() {
        final GestureDetector tapGestureDetector = new GestureDetector(getContext(), new TapGestureListener());
        setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                tapGestureDetector.onTouchEvent(event);
                return false;
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
