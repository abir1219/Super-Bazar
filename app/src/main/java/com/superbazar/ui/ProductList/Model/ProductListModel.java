package com.superbazar.ui.ProductList.Model;

public class ProductListModel {
    String id,prodName,prodImage,actualPrice,shopPrice;

    public ProductListModel(String id, String prodName, String prodImage, String actualPrice, String shopPrice) {
        this.id = id;
        this.prodName = prodName;
        this.prodImage = prodImage;
        this.actualPrice = actualPrice;
        this.shopPrice = shopPrice;
    }

    public String getId() {
        return id;
    }

    public String getProdName() {
        return prodName;
    }

    public String getProdImage() {
        return prodImage;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public String getShopPrice() {
        return shopPrice;
    }
}
