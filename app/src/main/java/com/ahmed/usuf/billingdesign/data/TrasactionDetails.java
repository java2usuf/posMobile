package com.ahmed.usuf.billingdesign.data;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by myousuff on 5/14/16.
 */
public class TrasactionDetails {
    private int billNumber;
    private int finalTotal;
    private String date;
    private int discountedTotal;
    private int discount;


    @Override
    public String toString() {
        return "TrasactionDetails{" +
                "billNumber=" + billNumber +
                ", finalTotal=" + finalTotal +
                ", date='" + date + '\'' +
                ", discountedTotal=" + discountedTotal +
                ", discount=" + discount +
                '}';
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public TrasactionDetails(int finalTotal, String date, int discount) {
        this.finalTotal = finalTotal;
        this.date = date;
        this.discount = discount;
    }

    public int getDiscountedTotal() {

        return discountedTotal;
    }

    public void setDiscountedTotal(int discountedTotal) {
        this.discountedTotal = discountedTotal;
    }

    public TrasactionDetails(int finalTotal, int discount) {

        LocalDateTime localDateTime = new LocalDateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("d/MM/yy");
        String str = localDateTime.toString(fmt);
        date = str;
        this.discount=discount;
        this.finalTotal = finalTotal;
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
