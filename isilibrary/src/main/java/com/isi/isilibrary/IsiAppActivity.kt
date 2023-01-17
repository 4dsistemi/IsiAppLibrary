package com.isi.isilibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.CallSuper
import com.google.gson.Gson
import android.annotation.SuppressLint
import android.content.*
import com.google.gson.reflect.TypeToken
import com.isi.isiapi.classes.AppAndAppActivation
import android.content.pm.PackageManager
import com.google.android.flexbox.FlexboxLayout
import com.daimajia.androidanimations.library.YoYo
import com.daimajia.androidanimations.library.Techniques
import android.content.pm.ApplicationInfo
import android.content.res.Configuration
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.*
import com.isi.isiapi.HttpRequest
import java.lang.Exception
import java.util.ArrayList
import kotlin.math.abs
import kotlin.system.exitProcess

open class IsiAppActivity : AppCompatActivity() {
    private var x1 = 0f
    private var y1 = 0f
    var closing = true
    private var mainView: ViewGroup? = null
    private var inflate: View? = null
    private var underMenu: View? = null
    private var lateralMenu: View? = null
    private var scrolling = true
    fun setScrolling(scrolling: Boolean) {
        this.scrolling = scrolling
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        super.dispatchTouchEvent(ev)
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                y1 = ev.y
                x1 = ev.x
            }
            MotionEvent.ACTION_UP -> {
                val y2 = ev.y
                val deltay = abs(y2 - y1)
                val x2 = ev.x
                val deltaX = x2 - x1
                if (scrolling) {
                    if (abs(deltaX) > MIN_DISTANCE && x2 > x1) {
                        getPackageNameSlide(0)
                    } else if (abs(deltaX) > MIN_DISTANCE && x2 < x1) {
                        getPackageNameSlide(1)
                    } else if (deltay > MIN_DISTANCE && y1 < 100) {
                        getApplicationListActive(202)
                    }
                } else {
                    scrolling = true
                }
            }
        }
        return false
    }

    override fun onBackPressed() {
        if (inflate != null) {
            mainView!!.removeView(inflate)
            inflate = null
        } else if (underMenu != null) {
            mainView!!.removeView(underMenu)
            underMenu = null
        } else {
            super.onBackPressed()
        }
    }

    private fun lateralMenu(applications: ArrayList<AppAndAppActivation>) {
        val inflater = (getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        mainView = this@IsiAppActivity.window.decorView.rootView as ViewGroup
        lateralMenu = inflater.inflate(R.layout.menu_lateral, mainView, false)
        val lateralLayout = lateralMenu!!.findViewById<LinearLayout>(R.id.lateral_left)
        val lateralLayoutRight = lateralMenu!!.findViewById<LinearLayout>(R.id.lateral_right)
        for (i in 0..2) {
            for (app in applications) {
                if (app.appActivation == null) continue
                if (app.appActivation.position_in_menu - 1 == i) {
                    try {
                        val myin = lateralLayout.getChildAt(i) as LinearLayout
                        val b = myin.getChildAt(0) as ImageButton
                        val icon = packageManager.getApplicationIcon(app.application.Package)
                        b.setImageDrawable(icon)
                        b.setOnClickListener {
                            val launchIntent =
                                packageManager.getLaunchIntentForPackage(app.application.Package)
                            if (launchIntent != null) {
                                startActivity(launchIntent)
                                overridePendingTransition(
                                    R.anim.slide_from_left,
                                    R.anim.slide_to_right
                                )
                            }
                        }
                    } catch (ignored: Exception) {
                    }
                    break
                }
            }
        }
        for (i in 3..5) {
            for (app in applications) {
                if (app.appActivation == null) continue
                if (app.appActivation.position_in_menu - 1 == i) {
                    try {
                        val `in` = lateralLayoutRight.getChildAt(i - 3) as LinearLayout
                        val b = `in`.getChildAt(0) as ImageButton
                        val icon = packageManager.getApplicationIcon(app.application.Package)
                        b.setImageDrawable(icon)
                        b.setOnClickListener {
                            val launchIntent =
                                packageManager.getLaunchIntentForPackage(app.application.Package)
                            if (launchIntent != null) {
                                startActivity(launchIntent) //null pointer check in case package name was not found
                                overridePendingTransition(
                                    R.anim.slide_from_left,
                                    R.anim.slide_to_right
                                )
                            }
                        }
                    } catch (ignored: Exception) {
                    }
                    break
                }
            }
        }
        mainView!!.addView(lateralMenu)
        val bottomUp = AnimationUtils.loadAnimation(
            applicationContext,
            R.anim.bottom_up
        )
        lateralMenu!!.startAnimation(bottomUp)
    }

    private fun isPackageExisted(targetPackage: String?): Boolean {
        val pm = packageManager
        try {
            pm.getPackageInfo(targetPackage!!, PackageManager.GET_META_DATA)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return false
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        if (isPackageExisted("com.isi.isiapp")) {
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (lateralMenu == null) {
                    getApplicationListActive(210)
                }
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun updateGUI(applications: ArrayList<AppAndAppActivation>) {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflate = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            inflater.inflate(R.layout.menu_layout, mainView, false)
        } else {
            inflater.inflate(R.layout.menu_layout_portrait, mainView, false)
        }
        val closeMenu = inflate!!.findViewById<Button>(R.id.closeMenuButton)
        closeMenu.setOnClickListener {
            mainView!!.removeView(inflate)
            inflate = null
        }
        val thisAppImageView = inflate!!.findViewById<ImageView>(R.id.thisAppImageView)
        try {
            val pkg = packageName //your package name
            val icon = packageManager.getApplicationIcon(pkg)
            thisAppImageView.setImageDrawable(icon)
        } catch (ignored: PackageManager.NameNotFoundException) {
        }
        val appName = inflate!!.findViewById<TextView>(R.id.thisAppName)
        appName.text = getApplicationListName(null)
        val flexboxLayout = inflate!!.findViewById<FlexboxLayout>(R.id.serviceFlex)
        for (pack in applications) {
            if (pack.application.Package == packageName) {
                continue
            }
            val packInflater = (getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            val packInflate = packInflater.inflate(R.layout.service_flex_cell, null)
            val imageApp = packInflate.findViewById<ImageView>(R.id.appImage)
            try {
                val appIcon = packageManager.getApplicationIcon(pack.application.Package)
                imageApp.setImageDrawable(appIcon)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            val appNameSecondary = packInflate.findViewById<TextView>(R.id.appName)
            appNameSecondary.text = pack.application.name
            packInflate.setOnClickListener {
                val launchIntent =
                    packageManager.getLaunchIntentForPackage(pack.application.Package)
                if (launchIntent != null) {
                    mainView!!.removeView(inflate)
                    inflate = null
                    startActivity(launchIntent) //null pointer check in case package name was not found
                }
            }
            flexboxLayout.addView(packInflate)
        }
        val logout = inflate!!.findViewById<ImageButton>(R.id.logoutButton)
        logout.setOnClickListener {
            try {
                mainView!!.removeView(inflate)
                val intent2 = Intent("timeoutService")
                intent2.putExtra("time_out", 1)
                sendBroadcast(intent2)
            } catch (ignored: Exception) {
            }
        }
        YoYo.with(Techniques.SlideInDown).duration(700).repeat(0).playOn(inflate)
        mainView = this@IsiAppActivity.window.decorView.rootView as ViewGroup
        mainView!!.addView(inflate)
    }

    private val guestReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val message = intent.getIntExtra("time_out", 0)
            if (message != 0) {
                runOnUiThread {
                    doSomethingOnTimeout()
                    if (closing) {
                        finish()
                        exitProcess(0)
                    } else {
                        val launchIntent =
                            packageManager.getLaunchIntentForPackage("com.isi.isiapp")
                        if (launchIntent != null) {
                            startActivity(launchIntent) //null pointer check in case package name was not found
                            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
                        }
                    }
                }
            }
        }
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(guestReceiver, IntentFilter("timeoutService"))
    }

    private fun getApplicationListActive(code: Int) {
        try {
            val myIntent = Intent()
            myIntent.setClassName("com.isi.isiapp", "com.isi.isiapp.PackageActivity")
            myIntent.putExtra("intent", "getApplicationsActive")
            startActivityForResult(myIntent, code)
        } catch (ignored: Exception) {
        }
    }

    private fun getPackageNameSlide(code: Int) {
        try {
            val myIntent = Intent()
            myIntent.setClassName("com.isi.isiapp", "com.isi.isiapp.PackageActivity")
            myIntent.putExtra("package_name", applicationContext.packageName)
            myIntent.putExtra("code", code)
            if (code == 0) {
                startActivityForResult(myIntent, 200)
            } else {
                startActivityForResult(myIntent, 201)
            }
        } catch (ignored: Exception) {
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    val packageName = data.getStringExtra("package_name")
                    if (packageName != null) {
                        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
                        if (launchIntent != null) {
                            startActivity(launchIntent) //null pointer check in case package name was not found
                            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
                        }
                    }
                }
            }
        } else if (requestCode == 201) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    val packageName = data.getStringExtra("package_name")!!
                    val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
                    if (launchIntent != null) {
                        startActivity(launchIntent) //null pointer check in case package name was not found
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                    }
                }
            }
        } else if (requestCode == 202) {
            if (data != null) {
                val packageName = data.getStringExtra("applications_active")
                val listType = object : TypeToken<ArrayList<AppAndAppActivation?>?>() {}.type
                val gson = Gson()
                val applications =
                    gson.fromJson<ArrayList<AppAndAppActivation>>(packageName, listType)
                updateGUI(applications)
            }
        } else if (requestCode == 210) {
            if (data != null) {
                val packageName = data.getStringExtra("applications_active")
                val listType = object : TypeToken<ArrayList<AppAndAppActivation?>?>() {}.type
                val gson = Gson()
                val applications =
                    gson.fromJson<ArrayList<AppAndAppActivation>>(packageName, listType)
                lateralMenu(applications)
            }
        }
    }

    fun doSomethingOnTimeout() {}
    fun sendBroadcast(title: String?, messgae: String?) {
        NotifyBroadcast.sendBroadcast(this, title, messgae)
    }

    private fun getApplicationListName(packageName: String?): String {
        var packageName = packageName
        if (packageName == null) {
            packageName = getPackageName()
        }
        val pm = applicationContext.packageManager
        val ai: ApplicationInfo? = try {
            pm.getApplicationInfo(packageName!!, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
        return (if (ai != null) pm.getApplicationLabel(ai) else "") as String
    }

    fun repaintMenu() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mainView!!.removeView(lateralMenu)
            lateralMenu = null
            getApplicationListActive(210)
        }
    }

    fun initAPI(apikey: String) {
        Companion.apikey = apikey
        isiCashierRequest = HttpRequest(apikey)
    }

    @CallSuper
    fun updateUI(layout: Int) {
        runOnUiThread { setContentView(layout) }
    }

    fun errorPage(layout: Int) {
        runOnUiThread {
            setContentView(R.layout.error_data)
            val reloadButton = findViewById<Button>(R.id.reload_data_button)
            reloadButton.setOnClickListener { updateUI(layout) }
        }
    }

    fun emptyData() {
        runOnUiThread { setContentView(R.layout.empty_data) }
    }

    companion object {
        private const val MIN_DISTANCE = 400
        var apikey = ""
        var isiCashierRequest: HttpRequest? = null
    }
}