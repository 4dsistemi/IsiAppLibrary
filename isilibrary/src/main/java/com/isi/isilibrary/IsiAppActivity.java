package com.isi.isilibrary;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.isi.isiapi.isicashier.HttpRequest;
import com.isi.isilibrary.application.ApplicationList;
import com.isi.isilibrary.backActivity.BackActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class IsiAppActivity extends AppCompatActivity{

    private float x1;
    private float y1;
    private static final int MIN_DISTANCE = 400;
    public boolean closing = true;
    private ViewGroup mainView;
    private View inflate = null;
    private View underMenu = null;
    private View lateralMenu = null;
    public static String apikey = "";
    public static String serial = "";
    public static HttpRequest isiCashierRequest;

    private boolean scrolling = true;

    public void setScrolling(boolean scrolling) {
        this.scrolling = scrolling;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        super.dispatchTouchEvent(ev);

        switch(ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                y1 = ev.getY();
                x1 = ev.getX();

                break;

            case MotionEvent.ACTION_UP:

                float y2 = ev.getY();
                float deltay = Math.abs(y2-y1);
                float x2 = ev.getX();
                float deltaX = x2 - x1;

                if(scrolling){
                    if (Math.abs(deltaX) > MIN_DISTANCE && x2 > x1)
                    {
                        getPackageNameSlide(0);
                    }else if(Math.abs(deltaX) > MIN_DISTANCE && x2 < x1){
                        getPackageNameSlide(1);
                    }else if(deltay > MIN_DISTANCE && y1 < 100){
                        getApplicationListActive(202);
                    }
                }else{
                    scrolling = true;
                }

                break;
        }

        return false;
    }

    @Override
    public void onBackPressed() {

        if(inflate != null){

            mainView.removeView(inflate);

            inflate = null;

        }else if(underMenu != null){

            mainView.removeView(underMenu);

            underMenu = null;

        }else{
            super.onBackPressed();
        }
    }

    private void lateralMenu(final ArrayList<ApplicationList> applications){

        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;

        mainView = ((ViewGroup) IsiAppActivity.this.getWindow().getDecorView().getRootView());

        lateralMenu = inflater.inflate(R.layout.menu_lateral, mainView, false);

        ConstraintLayout lateralLayout = lateralMenu.findViewById(R.id.lateral_left);
        ConstraintLayout lateralLayoutRight = lateralMenu.findViewById(R.id.lateral_right);

        for (int i = 0; i < 3; i++) {

            for (final ApplicationList app : applications){

                if(app.getAppActivation().PositionInMenu - 1 == i){
                    try {
                        ImageButton b = (ImageButton) lateralLayout.getChildAt(i);
                        Drawable icon = getPackageManager().getApplicationIcon(app.Package);

                        b.setImageDrawable(icon);

                        b.setOnClickListener(v -> {

                            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(app.Package);
                            if (launchIntent != null) {
                                startActivity(launchIntent);//null pointer check in case package name was not found
                                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                            }

                        });

                    } catch (Exception ignored) {}

                    break;
                }

            }

        }

        for (int i = 3; i < 6; i++) {

            for (final ApplicationList app : applications){

                if(app.getAppActivation().PositionInMenu - 1 == i){
                    try {
                        ImageButton b = (ImageButton) lateralLayoutRight.getChildAt(i - 3);
                        Drawable icon = getPackageManager().getApplicationIcon(app.Package);

                        b.setImageDrawable(icon);

                        b.setOnClickListener(v -> {

                            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(app.Package);
                            if (launchIntent != null) {
                                startActivity(launchIntent);//null pointer check in case package name was not found
                                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                            }

                        });

                    } catch (Exception ignored) {

                    }

                    break;
                }

            }

        }

        mainView.addView(lateralMenu);

        Animation bottomUp = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bottom_up);
        lateralMenu.startAnimation(bottomUp);


    }

    public boolean isPackageExisted(String targetPackage){
        PackageManager pm=getPackageManager();
        try {
            pm.getPackageInfo(targetPackage,PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(isPackageExisted("com.isi.isiapp")){
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                if(lateralMenu == null){
                    getApplicationListActive(210);
                }
            }
        }
    }

    @SuppressLint("InflateParams")
    private void updateGUI(ArrayList<ApplicationList> applications){

        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            inflate = inflater.inflate(R.layout.menu_layout, null);
        }else{
            inflate = inflater.inflate(R.layout.menu_layout_portrait, null);
        }



        Button closeMenu = inflate.findViewById(R.id.closeMenuButton);

        closeMenu.setOnClickListener(v -> {
            mainView.removeView(inflate);

            inflate = null;

        });

        ImageView thisAppImageView = inflate.findViewById(R.id.thisAppImageView);

        try {
            String pkg = getPackageName();//your package name
            Drawable icon = getPackageManager().getApplicationIcon(pkg);
            thisAppImageView.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException ignored) {

        }

        TextView appName = inflate.findViewById(R.id.thisAppName);

        appName.setText(getApplicationListName(null));

        FlexboxLayout flexboxLayout = inflate.findViewById(R.id.serviceFlex);

        for (final ApplicationList pack : applications){

            if(pack.Package.equals(getPackageName())){
                continue;
            }

            LayoutInflater packInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
            assert packInflater != null;
            View packInflate = packInflater.inflate(R.layout.service_flex_cell, null);

            ImageView imageApp = packInflate.findViewById(R.id.appImage);

            try {
                Drawable appIcon = getPackageManager().getApplicationIcon(pack.Package);

                imageApp.setImageDrawable(appIcon);

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            TextView appNameSecondary = packInflate.findViewById(R.id.appName);

            appNameSecondary.setText(pack.Name);

            packInflate.setOnClickListener(v -> {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(pack.Package);
                if (launchIntent != null) {
                    mainView.removeView(inflate);

                    inflate = null;
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }
            });

            flexboxLayout.addView(packInflate);
        }

        ImageButton logout = inflate.findViewById(R.id.logoutButton);

        logout.setOnClickListener(v -> {
            try{
                mainView.removeView(inflate);

                Intent intent2 = new Intent("timeoutService");
                intent2.putExtra("time_out", 1);
                sendBroadcast(intent2);

            }catch (Exception ignored){

            }
        });


        YoYo.with(Techniques.SlideInDown).duration(700).repeat(0).playOn(inflate);

        mainView = ((ViewGroup) IsiAppActivity.this.getWindow().getDecorView().getRootView());

        mainView.addView(inflate);

    }

    private final BroadcastReceiver guestReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final int message = intent.getIntExtra("time_out", 0);

            if(message != 0){

                runOnUiThread(() -> {

                    doSomethingOnTimeout();

                    if(closing){
                        finish();
                        System.exit(0);
                    }else{
                        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.isi.isiapp");
                        if (launchIntent != null) {
                            startActivity(launchIntent);//null pointer check in case package name was not found
                            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                        }
                    }


                });
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerReceiver(guestReceiver, new IntentFilter("timeoutService"));

    }

    private void getApplicationListActive(int code){

        try{
            Intent myIntent = new Intent();
            myIntent.setClassName("com.isi.isiapp", "com.isi.isiapp.PackageActivity");
            myIntent.putExtra("intent", "getApplicationsActive");
            startActivityForResult(myIntent, code);
        }catch (Exception ignored){

        }

    }

    private void getPackageNameSlide(int code){

        try{
            Intent myIntent = new Intent();
            myIntent.setClassName("com.isi.isiapp", "com.isi.isiapp.PackageActivity");
            myIntent.putExtra("package_name", getApplicationContext().getPackageName());
            myIntent.putExtra("code", code);
            if(code == 0){
                startActivityForResult(myIntent, 200);
            }else{
                startActivityForResult(myIntent, 201);
            }
        }catch (Exception ignored){

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 200){

            if(resultCode == RESULT_OK){

                if(data != null){
                    String packageName = data.getStringExtra("package_name");

                    if(packageName != null){
                        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
                        if (launchIntent != null) {
                            startActivity(launchIntent);//null pointer check in case package name was not found
                            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                        }
                    }

                }

            }

        }else if(requestCode == 201){

            if(resultCode == RESULT_OK){

                if(data != null){
                    String packageName = data.getStringExtra("package_name");

                    assert packageName != null;
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
                    if (launchIntent != null) {
                        startActivity(launchIntent);//null pointer check in case package name was not found
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    }
                }

            }

        }else if (requestCode == 202){

            if(data != null){
                String packageName = data.getStringExtra("applications_active");

                Type listType = new TypeToken<ArrayList<ApplicationList>>() {}.getType();
                Gson gson = new Gson();

                ArrayList<ApplicationList> applications = gson.fromJson(packageName, listType);

                updateGUI(applications);
            }


        }else if(requestCode == 210){

            if(data != null){
                String packageName = data.getStringExtra("applications_active");

                Type listType = new TypeToken<ArrayList<ApplicationList>>() {}.getType();
                Gson gson = new Gson();

                ArrayList<ApplicationList> applications = gson.fromJson(packageName, listType);

                lateralMenu(applications);
            }

        }
    }

    public void doSomethingOnTimeout(){

    }

    public void sendBroadcast(String title, String messgae){
       NotifyBroadcast.sendBroadcast(this, title, messgae);
    }

    private String getApplicationListName(String packageName) {

        if(packageName == null){
            packageName = getPackageName();
        }

        final PackageManager pm = getApplicationContext().getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo( packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }
        return (String) (ai != null ? pm.getApplicationLabel(ai) : "");
    }

    public void repaintMenu(){

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){

            mainView.removeView(lateralMenu);

            lateralMenu = null;

            getApplicationListActive(210);
        }

    }

    public void initAPI(String apikey, boolean debug, String serial){
        IsiAppActivity.apikey = apikey;
        IsiAppActivity.serial = serial;
        IsiAppActivity.isiCashierRequest = new HttpRequest(apikey);
        IsiAppActivity.isiCashierRequest.setDebug(debug);
    }

    @CallSuper
    public void updateUI(int layout){
        runOnUiThread(() -> setContentView(layout));
    }


    public void errorPage(int layout){
        runOnUiThread(() -> {
            setContentView(R.layout.error_data);

            Button reloadButton = findViewById(R.id.reload_data_button);

            reloadButton.setOnClickListener(v -> updateUI(layout));
        });

    }

    public void emptyData(){
        runOnUiThread(() -> setContentView(R.layout.empty_data));
    }

    private SweetAlertDialog pDialog;

    public void startLoader(String title){
        final String titleIn = title == null ? "Aggiorno dati..." : title;
        runOnUiThread(() -> {
            pDialog = new SweetAlertDialog(IsiAppActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText(titleIn);
            pDialog.setCancelable(false);
            pDialog.show();
        });

    }

    public void stopLoader(){
        runOnUiThread(() -> {
            if(pDialog != null){
                pDialog.dismissWithAnimation();
            }
        });
    }
}

