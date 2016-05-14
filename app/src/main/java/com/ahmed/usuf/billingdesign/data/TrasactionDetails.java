package com.ahmed.usuf.billingdesign.data;

/**
 * Created by myousuff on 5/14/16.
 */
public class TrasactionDetails {
    private int billNumber;
    private int finalTotal;
    private String date;
    private int discountedTotal;

    public TrasactionDetails(int finalTotal, String date, int discountedTotal) {
        this.finalTotal = finalTotal;
        this.date = date;
        this.discountedTotal = discountedTotal;
    }

    public int getDiscountedTotal() {

        return discountedTotal;
    }

    public void setDiscountedTotal(int discountedTotal) {
        this.discountedTotal = discountedTotal;
    }

    public TrasactionDetails(int finalTotal, String date) {
        this.finalTotal = finalTotal;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public TrasactionDetails(){

    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getFinalTotal() {
        return finalTotal;
    }

    public void setFinalTotal(int finalTotal) {
        this.finalTotal = finalTotal;
    }

    public int getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(int billNumber) {
        this.billNumber = billNumber;
    }
}