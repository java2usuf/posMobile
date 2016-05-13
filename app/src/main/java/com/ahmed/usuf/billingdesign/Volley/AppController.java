package com.ahmed.usuf.billingdesign.Volley;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.ahmed.usuf.billingdesign.Adapters.LineItem;
import com.ahmed.usuf.billingdesign.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Ahmed-Mariam on 5/12/2016.
 */
public class AppController extends Application {

    public static final String TAG = "AppController";
    private RequestQueue mRequestQueue;
    private boolean isDiscountOn=false;
    private SharedPreferences.Editor editor;
    private List<LineItem> bag = new ArrayList<LineItem>();
    private SharedPreferences sharedpreferences;
    private static AppController mInstance;


    public List<LineItem> getBag() {
        return bag;
    }

    public void setBag(List<LineItem> bag) {
        this.bag = bag;
    }

    public boolean isDiscountOn() {
        return isDiscountOn;
    }

    public void setIsDiscountOn(boolean isDiscountOn) {
        this.isDiscountOn = isDiscountOn;
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public void setEditor(SharedPreferences.Editor editor) {
        this.editor = editor;
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
        sharedpreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        if(sharedpreferences.getInt("billno",0) == 0){
            editor.putInt("billno",1);
            editor.commit();
        }
        mInstance = this;
    }

    public static String getProperty(String key,Context context) throws IOException {
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

}
