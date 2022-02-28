package com.superbazar.ui.OrderList.Model;

public class OrderListModel {
    String orderId,dateTime,totalAmount;

    public OrderListModel(String orderId, String dateTime, String totalAmount) {
        this.orderId = orderId;
        this.dateTime = dateTime;
        this.totalAmount = totalAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getTotalAmount() {
        return totalAmount;
    }
}
