package com.oopcows.trackandtrigger.helpers;

public class CategoryItem {

    private String itemName;
    private String imgPath;
    private int quantity;
    private String downPath;

    public CategoryItem(String itemName, String imgPath, int quantity, String downPath) {
        this.itemName = itemName;
        this.imgPath = imgPath;
        this.quantity = quantity;
        this.downPath = downPath;
    }

    public String getItemName() { return itemName; }
    public String getImgPath() { return imgPath; }
    public int getQuantity() { return quantity; }
    public String getDownPath() { return downPath; }

    public void increaseQuantity() {
        quantity++;
    }
    public void decreaseQuantity() {
        if(quantity > 0) {
            quantity--;
        }
    }
}