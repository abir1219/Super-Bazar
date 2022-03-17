package com.superbazar.ui.SearchProduct.Model;

public class SearchProductModel {
    private String prodid,name,prodDesc,image;

    public SearchProductModel(String prodid, String name, String prodDesc, String image) {
        this.prodid = prodid;
        this.name = name;
        this.prodDesc = prodDesc;
        this.image = image;
    }


    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getProdid() {
        return prodid;
    }

    public String getProdDesc() {
        return prodDesc;
    }
}
