package com.superbazar.ui.OrderDetails.Model;

public class OrderDetailModel {
    String prodId,order_id,prodName,prodDetails,quantity,price,prodImage,prdStatus,date_time;

    public OrderDetailModel(String prodId, String order_id, String prodName, String prodDetails,
                            String quantity, String price, String prodImage, String prdStatus, String date_time) {
        this.prodName = prodName;
        this.prodId = prodId;
        this.order_id = order_id;
        this.prodDetails = prodDetails;
        this.quantity = quantity;
        this.price = price;
        this.prodImage = prodImage;
        this.prdStatus = prdStatus;
        this.date_time = date_time;
    }

    public String getProdId() {
        return prodId;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getProdName() {
        return prodName;
    }

    public String getProdDetails() {
        return prodDetails;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return price;
    }

    public String getProdImage() {
        return prodImage;
    }

    public String getPrdStatus() {
        return prdStatus;
    }

    public String getDate_time() {
        return date_time;
    }

}
