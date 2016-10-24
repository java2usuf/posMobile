package com.srm.billpodo.billingsystem.singleton;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.epson.epos2.printer.Printer;
import com.google.common.base.Strings;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.FirebaseDatabase;
import com.srm.billpodo.billingsystem.data.LineItem;
import com.srm.billpodo.billingsystem.data.Trasaction;
import com.srm.billpodo.billingsystem.util.AppConstants;
import com.srm.billpodo.billingsystem.util.Login;
import com.srm.billpodo.billingsystem.util.SystemConfig;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ahmed-Mariam on 5/12/2016.
 */
public class AppController extends Application implements Application.ActivityLifecycleCallbacks {

    public static final String TAG = "AppController";
    private RequestQueue mRequestQueue;
    private boolean isDiscountOn = false;
    private SharedPreferences.Editor editor;
    private List<LineItem> bag = new ArrayList<LineItem>();
    private Trasaction txnDetails = new Trasaction();

    private static AppController mInstance;
    private SharedPreferences sharedpreferences;
    private static Context context;
    private Login login;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseDatabase database;

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public Printer getmPrinter() {
        return mPrinter;
    }

    public void setmPrinter(Printer mPrinter) {
        this.mPrinter = mPrinter;
    }

    private Printer mPrinter = null;

    public boolean isDiscountOn() {
        return isDiscountOn;
    }

    public static Context getAppContext() {
        return context;
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
        StringBuffer buffer = new StringBuffer();

        if (days > 0) {
            for (int i = 1; i < days; i++) {
                now.minusDays(i);
                buffer.append(format.print(now) + "\n");
            }
        } else {
            buffer.append(format.print(now));
        }

        return buffer.toString();
    }

    public void setEditor(SharedPreferences.Editor editor) {
        this.editor = editor;
    }


    public Trasaction getTxnDetails() {
        return txnDetails;
    }

    public void setTxnDetails(Trasaction txnDetails) {
        this.txnDetails = txnDetails;
    }


    public List<LineItem> getBag() {
        return bag;
    }

    public void setBag(List<LineItem> bag) {
        this.bag = bag;
    }

    public int getTotal() {
        int totalCount = 0;
        for (LineItem details : AppController.getInstance().getBag()) {
            totalCount += Integer.parseInt(details.getTotal());
        }
        return totalCount;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        sharedpreferences = getSharedPreferences(AppConstants.APPLICATION_PERFERENCE, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        SystemConfig.getInstance().setIp(sharedpreferences.getString(AppConstants.PRINTER_IP, AppConstants.PRINTER_DEFAULT_IP));
        SystemConfig.getInstance().setDay(sharedpreferences.getString(AppConstants.Number_of_Days, AppConstants.DEFAULT_NUMBER_OF_DAYS));
        mInstance = this;
        context = getApplicationContext();
        //initilize app
        FirebaseApp.initializeApp(context);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        database = FirebaseDatabase.getInstance();

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

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        DateFormat formatter = new SimpleDateFormat(AppConstants.DD_MM_YYYY);
        Date today = new Date();
        String todaysDate = formatter.format(today);
        String storedDate = sharedpreferences.getString(AppConstants.TODAYS_DATE, "");

        if (Strings.isNullOrEmpty(storedDate)) {
            editor.putString(AppConstants.TODAYS_DATE, todaysDate);
            editor.putInt(AppConstants.BILLNO, 1);
            editor.commit();
        } else if (!todaysDate.equals(sharedpreferences.getString(AppConstants.TODAYS_DATE, ""))) {
            editor.putInt(AppConstants.BILLNO, 1);
            editor.commit();
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

}
