package com.srm.billpodo.billingsystem.util;

/**
 * Created by Ahmed-Mariam on 5/17/2016.
 */
public class SystemConfig {
    private String ip;
    private String day;
    static SystemConfig config = new SystemConfig();

    public static SystemConfig getInstance() {
        return config;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}