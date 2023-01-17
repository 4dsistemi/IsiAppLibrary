package com.isi.isilibrary

import android.app.Activity
import android.content.Intent

object NotifyBroadcast {
    fun sendBroadcast(a: Activity, title: String?, message: String?) {
        val i = Intent("notify_isiapp_interface")
        i.putExtra("title", title)
        i.putExtra("message", message)
        a.sendBroadcast(i)
    }
}