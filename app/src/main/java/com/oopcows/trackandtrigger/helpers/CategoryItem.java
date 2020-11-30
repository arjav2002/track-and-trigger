package com.oopcows.trackandtrigger.helpers;

public class CategoryItem {

    private String itemName;
    private String imgPath;
    private int quantity;

    public CategoryItem(String itemName, String imgPath, int quantity) {
        this.itemName = itemName;
        this.imgPath = imgPath;
        this.quantity = quantity;
    }

    public String getItemName() { return itemName; }
    public String getImgPath() { return imgPath; }
    public int getQuantity() { return quantity; }

    public void increaseQuantity() {
        quantity++;
    }
    public void decreaseQuantity() {
        if(quantity > 0) {
            quantity--;
        }
    }
}
