package com.isi.isilibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class IsiAppScrollView extends ScrollView {

    private boolean dispatchEventEnable = true;
    private IsiAppActivity activity = null;

    public IsiAppScrollView(Context context) {
        super(context);
    }

    public IsiAppScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IsiAppScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public IsiAppScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        if(dispatchEventEnable){
            super.dispatchTouchEvent(ev);
            return true;
        }else{
            if(activity != null){
                activity.setScrolling(false);
            }
            return false;

        }
    }
}
