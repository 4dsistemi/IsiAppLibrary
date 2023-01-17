package com.isi.isilibrary

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.HorizontalScrollView

class IsiAppHorizontalScrollView : HorizontalScrollView {
    var isDispatchEventEnable = true
    private var activity: IsiAppActivity? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    fun setActivity(activity: IsiAppActivity?) {
        this.activity = activity
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (activity != null && !isDispatchEventEnable) {
            activity!!.setScrolling(false)
        }
        return super.dispatchTouchEvent(ev)
    }
}