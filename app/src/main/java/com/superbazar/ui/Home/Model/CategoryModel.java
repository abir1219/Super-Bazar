package com.superbazar.ui.Home.Model;

public class CategoryModel {
    private String id,name,category_image;

    public CategoryModel(String id, String name, String category_image) {
        this.id = id;
        this.name = name;
        this.category_image = category_image;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory_image() {
        return category_image;
    }

}
