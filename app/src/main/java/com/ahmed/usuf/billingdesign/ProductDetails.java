package com.ahmed.usuf.billingdesign;

/**
 * Created by Ahmed-Mariam on 3/30/2016.
 */
public class ProductDetails {
    private String qty;
    private String price;
    private String total;
    private String productName;

    public ProductDetails(String qty,String price,String productName,String total){
        this.qty=qty;
        this.productName=productName;
        this.price=price;
        this.total=total;
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
