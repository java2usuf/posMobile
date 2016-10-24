package com.srm.billpodo.billingsystem.util;

import com.srm.billpodo.billingsystem.R;
import com.srm.billpodo.billingsystem.singleton.AppController;

/**
 * Created by myousuff on 7/29/16.
 */
public enum Login {

    LOGIN_FAILED(AppConstants.SRM_MC_STORE_FAILED_USER, AppConstants.SRM_MC_STORE_FAILED_PASS, 0, 0, 0, 0, 0, 0),
    MENCITY(AppConstants.SRM_MC_STORE_USER, AppConstants.SRM_MC_STORE_PASS, R.string.brand_menscity_name, R.string.brand_menscity_short_address, R.string.brand_menscity_mobile, R.string.brand_menscity_tin,
            R.string.brand_menscity_bill, R.string.brand_menscity_printer_header),
    SAFIRE(AppConstants.SRM_ST_STORE_USER, AppConstants.SRM_ST_STORE_PASS, R.string.brand_safire_name, R.string.brand_safire_short_address, R.string.brand_safire_mobile, R.string.brand_safire_tin,
            R.string.brand_safire_bill, R.string.brand_safire_printer_header),
    US_FASHION(AppConstants.SRM_US_STORE_USER, AppConstants.SRM_US_STORE_PASS, R.string.brand_srm_fashion_name, R.string.brand_srm_fashion_short_address, R.string.brand_srm_fashion_mobile, R.string.brand_srm_fashion_tin,
            R.string.brand_srm_fashion_bill, R.string.brand_srm_fashion_printer_header),
    US_TAILOR(AppConstants.SRM_US_TAILOR_STORE_USER, AppConstants.SRM_US_TAILOR_STORE_PASS, R.string.brand_srm_tailor_name, R.string.brand_srm_tailor_short_address, R.string.brand_srm_tailor_mobile, R.string.brand_srm_tailor_tin,
            R.string.brand_srm_tailor_bill, R.string.brand_srm_tailor_printer_header);


    private String userName;
    private String password;
    private int storeName;
    private int shortAddress;
    private int mobile;
    private int tin;
    private int bill;
    private int printerHeader;


    Login(String userName, String password, int storeName, int shortAddress, int mobile, int tin, int bill, int printerHeader) {
        this.userName = userName;
        this.password = password;
        this.storeName = storeName;
        this.shortAddress = shortAddress;
        this.mobile = mobile;
        this.tin = tin;
        this.bill = bill;
        this.printerHeader = printerHeader;
    }

    public void saveUserName(){
        AppController.getInstance().getEditor().putString(AppConstants.LASTUSEDUSERNAME,this.userName).commit();
    }

    public static Login fromString(String userName, String password) {
        if (userName != null) {
            for (Login b : Login.values()) {
                if (userName.equalsIgnoreCase(b.userName) && password.equalsIgnoreCase(b.password)) {
                    return b;
                }
            }
        }
        return LOGIN_FAILED;
    }


    public String[] getBrandValues(int key) {
        String[] array = new String[0];
        if (key == R.string.brand_store_name) {
            array = new String[1];
            array[0] = AppController.getInstance().getApplicationContext().getString(this.getStoreName());
        } else if (key == R.string.brand_store_short_address) {
            array = new String[1];
            array[0] = AppController.getInstance().getApplicationContext().getString(this.getShortAddress());

        } else if (key == R.string.brand_store_mobile) {
            array = new String[1];
            array[0] = AppController.getInstance().getApplicationContext().getString(this.getMobile());

        } else if (key == R.string.brand_store_tin) {
            array = new String[1];
            array[0] = AppController.getInstance().getApplicationContext().getString(this.getTin());

        } else if (key == R.string.brand_store_bill) {
            array = new String[1];
            array[0] = AppController.getInstance().getApplicationContext().getString(this.getBill());
        } else if (key == R.string.brand_store_printer_header) {
            array = new String[1];
            array[0] = AppController.getInstance().getApplicationContext().getString(this.getPrinterHeader());
        }


        return array;
    }


    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public int getStoreName() {
        return storeName;
    }

    public int getShortAddress() {
        return shortAddress;
    }

    public int getMobile() {
        return mobile;
    }

    public int getTin() {
        return tin;
    }

    public int getBill() {
        return bill;
    }

    public int getPrinterHeader() {
        return printerHeader;
    }
}