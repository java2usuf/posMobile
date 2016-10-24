package com.srm.billpodo.billingsystem.data;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

/**
 * Created by myousuff on 5/14/16.
 */
public class Trasaction {

    private String date;
    private int billNumber;
    private int totalBeforeDiscount;
    private int totalAfterDiscount;
    private int disacountGiven;
    private int discountProvidedInAmount;
    private String discountProvidedInPercentage;
    private List<LineItem> lineItems;
    private String uniqueRandomNumber;
    private int totalQuantity;

    public Trasaction(){

    }

    public Trasaction(int totalBeforeDiscount, int totalAfterDiscount) {

        LocalDateTime localDateTime = new LocalDateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("d/MM/yy");
        String str = localDateTime.toString(fmt);
        date = str;

        this.totalBeforeDiscount = totalBeforeDiscount;
        this.totalAfterDiscount = totalAfterDiscount;
    }

    public int getTotalAfterDiscount() {
        return totalAfterDiscount;
    }

    public void setTotalAfterDiscount(int totalAfterDiscount) {
        this.totalAfterDiscount = totalAfterDiscount;
    }

    public String getDate() {
        return date;
    }

    public int getDisacountGiven() {
        return disacountGiven;
    }

    public void setDisacountGiven(int disacountGiven) {
        this.disacountGiven = disacountGiven;
    }

    public int getDiscountProvidedInAmount() {
        return discountProvidedInAmount;
    }

    public void setDiscountProvidedInAmount(int discountProvidedInAmount) {
        this.discountProvidedInAmount = discountProvidedInAmount;
    }

    public String getDiscountProvidedInPercentage() {
        return discountProvidedInPercentage;
    }

    public void setDiscountProvidedInPercentage(String discountProvidedInPercentage) {
        this.discountProvidedInPercentage = discountProvidedInPercentage+"";
        this.discountProvidedInPercentage+="%";
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public String getUniqueRandomNumber() {
        return uniqueRandomNumber;
    }

    public void setUniqueRandomNumber(String uniqueRandomNumber) {
        this.uniqueRandomNumber = uniqueRandomNumber;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTotalBeforeDiscount() {
        return totalBeforeDiscount;
    }

    public void setTotalBeforeDiscount(int totalBeforeDiscount) {
        this.totalBeforeDiscount = totalBeforeDiscount;
    }

    public int getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(int billNumber) {
        this.billNumber = billNumber;
    }



}
