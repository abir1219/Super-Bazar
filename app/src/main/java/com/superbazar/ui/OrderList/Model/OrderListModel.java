package com.superbazar.ui.OrderList.Model;

public class OrderListModel {
    String orderId,dateTime,totalAmount,OrderStatus;

    public OrderListModel(String orderId, String dateTime, String totalAmount,String OrderStatus) {
        this.orderId = orderId;
        this.dateTime = dateTime;
        this.totalAmount = totalAmount;
        this.OrderStatus = OrderStatus;
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

    public String getOrderStatus() {
        return OrderStatus;
    }
}
