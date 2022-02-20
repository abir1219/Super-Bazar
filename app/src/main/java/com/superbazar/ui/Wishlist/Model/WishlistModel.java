package com.superbazar.ui.Wishlist.Model;

public class WishlistModel {
    String cartId,productId, productName,desc, quantity, prodImage, actualPrice, offPrice;

    public WishlistModel(String cartId,String productId, String productName,String desc, String quantity,
                     String prodImage, String actualPrice, String offPrice) {
        this.productId = productId;
        this.cartId = cartId;
        this.productName = productName;
        this.desc = desc;
        this.quantity = quantity;
        this.prodImage = prodImage;
        this.actualPrice = actualPrice;
        this.offPrice = offPrice;
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
}
