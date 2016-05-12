package com.ahmed.usuf.billingdesign.Adapters;

/**
 * Created by Ahmed-Mariam on 3/30/2016.
 */
public class LineItem {
    private String qty;
    private String price;
    private String billNo;

    public LineItem(String qty, String price, String billNo, String total, String productName) {
        this.qty = qty;
        this.price = price;
        this.billNo = billNo;
        this.total = total;
        this.productName = productName;
    }

    private String total;
    private String productName;

    public String getBillNo() {

        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPrice() {
        return price;
    }

    public String getProductName() {
        return productName;
    }

    public String getQty() {
        return qty;
    }

    public String getTotal() {
        return total;
    }
}
