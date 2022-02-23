package com.superbazar.ui.Wishlist.Model;

public class WishlistModel {
    String cartId,productId, productName,desc,  prodImage, actualPrice, offPrice;

    public WishlistModel(String cartId,String productId, String productName,String desc,
                     String prodImage, String actualPrice, String offPrice) {
        this.productId = productId;
        this.productName = productName;
        this.desc = desc;
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
