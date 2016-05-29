package com.ahmed.usuf.billingdesign.singleton;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.ahmed.usuf.billingdesign.data.LineItem;
import com.ahmed.usuf.billingdesign.utili.AppConstants;
import com.ahmed.usuf.billingdesign.data.TrasactionDetails;
import com.ahmed.usuf.billingdesign.utili.SystemConfig;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.common.base.Strings;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Created by Ahmed-Mariam on 5/12/2016.
 */
public class AppController extends Application implements
        Application.ActivityLifecycleCallbacks  {

    public static final String TAG = "AppController";
    private RequestQueue mRequestQueue;
    private boolean isDiscountOn=false;
    private SharedPreferences.Editor editor;
    private List<LineItem> bag = new ArrayList<LineItem>();
    private static AppController mInstance;
    private TrasactionDetails txnDetails = new TrasactionDetails();
    private SharedPreferences sharedpreferences;

    public boolean isDiscountOn() {
        return isDiscountOn;
    }

    public void setIsDiscountOn(boolean isDiscountOn) {
        this.isDiscountOn = isDiscountOn;
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public String getLastDates(int days) {

        DateTimeFormatter format = DateTimeFormat.forPattern("d/MM/yy");
        LocalDateTime now = new LocalDateTime();
        StringBuffer buffer=new StringBuffer();

        if (days>0) {
            for (int i = 1; i < days; i++) {
                now.minusDays(i);
                buffer.append(format.print(now)+"\n");
            }
        }else{
            buffer.append(format.print(now));
        }

        return buffer.toString();
    }

    public void setEditor(SharedPreferences.Editor editor) {
        this.editor = editor;
    }


    public TrasactionDetails getTxnDetails() {
        return txnDetails;
    }

    public void setTxnDetails(TrasactionDetails txnDetails) {
        this.txnDetails = txnDetails;
    }


    public List<LineItem> getBag() {
        return bag;
    }

    public void setBag(List<LineItem> bag) {
        this.bag = bag;
    }

    public int getTotal(){
        int totalCount =  0;
        for (LineItem details: AppController.getInstance().getBag()){
            totalCount+=Integer.parseInt(details.getTotal());
        }
        return totalCount;
    }





    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        sharedpreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        SystemConfig.getInstance().setIp(sharedpreferences.getString(AppConstants.PRINTER_IP, AppConstants.PRINTER_DEFAULT_IP));
        SystemConfig.getInstance().setDay(sharedpreferences.getString(AppConstants.Number_of_Days, AppConstants.DEFAULT_NUMBER_OF_DAYS));


        mInstance = this;
    }

    public String getProperty(String key,Context context) throws IOException {
        Properties properties = new Properties();;
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open("app.properties");
        properties.load(inputStream);
        return properties.getProperty(key);
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }



    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }
    public SharedPreferences getSharedpreferences() {
        return sharedpreferences;
    }

    public void setSharedpreferences(SharedPreferences sharedpreferences) {
        this.sharedpreferences = sharedpreferences;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        System.out.println("onActivityCreated ------- 333sfsfs###");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        System.out.println("onActivityStarted -------333 ###");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        DateFormat formatter = new SimpleDateFormat(AppConstants.DD_MM_YYYY);
        Date today = new Date();
        String todaysDate = formatter.format(today);
        String storedDate = sharedpreferences.getString(AppConstants.TODAYS_DATE, "");

        if(Strings.isNullOrEmpty(storedDate)){
            editor.putString(AppConstants.TODAYS_DATE,todaysDate);
            editor.putInt(AppConstants.BILLNO, 1);
            editor.commit();
        }else if(!todaysDate.equals(sharedpreferences.getString(AppConstants.TODAYS_DATE,""))){
            editor.putInt(AppConstants.BILLNO,1);
            editor.commit();
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        System.out.println("onActivityPaused -------33 ###");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        System.out.println("onActivityStopped -------33 ###");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        System.out.println("onActivitySaveInstanceState-------33 ###");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        System.out.println("onActivityDestroyed ------- 333###");
    }
}
