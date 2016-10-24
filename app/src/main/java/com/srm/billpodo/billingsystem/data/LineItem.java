package com.srm.billpodo.billingsystem.data;

/**
 * Created by Ahmed-Mariam on 3/30/2016.
 */
public class LineItem {
    private String qty;
    private String price;
    private String total;
    private String productName;
    private String percentage;
    private boolean returnProduct;


    public boolean isReturnProduct() {
        return returnProduct;
    }

    public void setReturnProduct(boolean returnProduct) {
        this.returnProduct = returnProduct;
    }



    public LineItem(String qty, String price, String total, String productName,boolean returnProduct) {
        this.qty = qty;
        this.price = price;
        this.total = total;
        this.productName = productName;
        this.returnProduct = returnProduct;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
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
