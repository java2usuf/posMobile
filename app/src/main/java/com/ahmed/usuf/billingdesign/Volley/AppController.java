package com.ahmed.usuf.billingdesign.Volley;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.ahmed.usuf.billingdesign.Adapters.LineItem;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed-Mariam on 5/12/2016.
 */
public class AppController extends Application {

    public static final String TAG = "AppController";
    private RequestQueue mRequestQueue;

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public void setEditor(SharedPreferences.Editor editor) {
        this.editor = editor;
    }

    private SharedPreferences.Editor editor;

    public List<LineItem> getBag() {
        return bag;
    }

    public void setBag(List<LineItem> bag) {
        this.bag = bag;
    }

    private List<LineItem> bag = new ArrayList<LineItem>();
    private SharedPreferences sharedpreferences;
    private static AppController mInstance;

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
        }
        mInstance = this;
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
