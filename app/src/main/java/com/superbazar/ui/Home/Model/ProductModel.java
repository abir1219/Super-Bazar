package com.superbazar.ui.Home.Model;

public class ProductModel {
    String id,name,image,actualPrice,shopPrice,categoryName;

    public ProductModel(String id, String name, String image, String actualPrice, String shopPrice,String categoryName) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.actualPrice = actualPrice;
        this.shopPrice = shopPrice;
        this.categoryName = categoryName;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public String getShopPrice() {
        return shopPrice;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
