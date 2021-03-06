package com.superbazar.ui.Cart.Model;

public class CartModel {
    String cartId,productId, productName,desc, quantity, prodImage, actualPrice, offPrice,total,totalTax,ProductRating;


    public CartModel(String cartId, String productId, String productName, String desc, String quantity,
                     String prodImage, String actualPrice, String offPrice, String total,String ProductRating) {
        this.cartId = cartId;
        this.productId = productId;
        this.productName = productName;
        this.desc = desc;
        this.quantity = quantity;
        this.prodImage = prodImage;
        this.actualPrice = actualPrice;
        this.offPrice = offPrice;
        this.total = total;
        this.ProductRating = ProductRating;
    }

    public String getProductRating() {
        return ProductRating;
    }

    public String getCartId() {
        return cartId;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getDesc() {
        return desc;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getProdImage() {
        return prodImage;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public String getOffPrice() {
        return offPrice;
    }

    public String getTotal() {
        return total;
    }

    public String getTotalTax() {
        return totalTax;
    }
}
