package com.srm.billpodo.billingsystem.util;

import android.util.Log;

import java.io.IOException;

import static com.srm.billpodo.billingsystem.singleton.AppController.getInstance;

/**
 * Created by myousuff on 5/15/16.
 */
public class Utility{

    public static String getBrandProperty(int key) {
        try {
            Log.i("getBrandProperty -->", ""+key);
            Object[] values = getInstance().getLogin().getBrandValues(key);
            return String.format(getInstance().getApplicationContext().getResources().getString(key),values);
        } catch (Exception e) {
            Log.e("getBrandProperty -->", "NO KEEY FOUND ****" + key,e);
            return "";
        }
    }

    public static String getStringValue(int key){
        return getInstance().getApplicationContext().getResources().getString(key);
    }
}
