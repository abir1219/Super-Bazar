package com.superbazar.ui.OrderList.Model;

public class OrderListModel {
    String orderId,order_number,dateTime,totalAmount,OrderStatus;

    public OrderListModel(String orderId,String order_number, String dateTime, String totalAmount,String OrderStatus) {
        this.orderId = orderId;
        this.order_number = order_number;
        this.dateTime = dateTime;
        this.totalAmount = totalAmount;
        this.OrderStatus = OrderStatus;
    }

    public String getOrder_number() {
        return order_number;
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
