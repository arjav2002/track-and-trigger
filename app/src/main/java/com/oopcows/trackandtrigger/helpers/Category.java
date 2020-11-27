package com.oopcows.trackandtrigger.helpers;

import androidx.room.Ignore;

import java.util.ArrayList;

public class Category {

    private ArrayList<CategoryItem> items;
    private String categoryName;

    @Ignore
    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public Category(String categoryName, ArrayList<CategoryItem> items) {
        this.categoryName = categoryName;
        this.items = items;
    }

    public ArrayList<CategoryItem> getItems() { return items; }
    public String getCategoryName() { return categoryName; }

}
