package com.isi.isilibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class IsiAppHorizontalScrollView extends HorizontalScrollView {

    private boolean dispatchEventEnable = true;
    private IsiAppActivity activity = null;

    public IsiAppHorizontalScrollView(Context context) {
        super(context);
    }

    public IsiAppHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IsiAppHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public IsiAppHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setDispatchEventEnable(boolean dispatchEventEnable) {
        this.dispatchEventEnable = dispatchEventEnable;
    }

    public void setActivity(IsiAppActivity activity) {
        this.activity = activity;
    }

    public boolean isDispatchEventEnable() {
        return dispatchEventEnable;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        super.dispatchTouchEvent(ev);
        if(activity != null && !dispatchEventEnable){
            activity.setScrolling(false);
        }
        return false;
    }
}
