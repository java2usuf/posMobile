package com.ahmed.usuf.billingdesign.utili;

import com.ahmed.usuf.billingdesign.Volley.AppController;
import com.ahmed.usuf.billingdesign.data.TrasactionDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed-Mariam on 5/17/2016.
 */
public class SystemConfig {
    private String ip;
    private int day;
    static SystemConfig config;


    public SystemConfig(String ip, String day) {
        this.ip = ip;
        this.day = Integer.parseInt(day);
    }

    public static SystemConfig getInstance(){
        return config;
    }

    public int getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = Integer.parseInt(day);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}