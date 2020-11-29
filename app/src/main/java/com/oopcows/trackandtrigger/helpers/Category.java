package com.oopcows.trackandtrigger.helpers;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

import static com.oopcows.trackandtrigger.helpers.CowConstants.CATEGORIES_TABLE_NAME;
import static com.oopcows.trackandtrigger.helpers.CowConstants.CATEGORY_DATA_COLUMN;
import static com.oopcows.trackandtrigger.helpers.CowConstants.CATEGORY_NAME_COLUMN;

@Entity(tableName = CATEGORIES_TABLE_NAME)
public class Category implements Parcelable {

    @ColumnInfo(name = CATEGORY_DATA_COLUMN)
    private ArrayList<CategoryItem> items;

    @ColumnInfo(name = CATEGORY_NAME_COLUMN)
    @PrimaryKey
    @NonNull
    private String categoryName;

    @Ignore
    public Category(String categoryName) {
        this.categoryName = categoryName;
        items = new ArrayList<CategoryItem>();
    }

    public Category(String categoryName, ArrayList<CategoryItem> items) {
        this.categoryName = categoryName;
        this.items = items;
    }

    protected Category(Parcel in) {
        categoryName = in.readString();
        int size = in.readInt();
        items = new ArrayList<CategoryItem>(size);
        for(int i = 0; i < size; i++) {
            String itemName = in.readString();
            String imgPath = in.readString();
            int quantity = in.readInt();
            items.add(new CategoryItem(itemName, imgPath, quantity));
        }
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public ArrayList<CategoryItem> getItems() { return items; }
    public String getCategoryName() { return categoryName; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(categoryName);
        parcel.writeInt(items.size());
        for(CategoryItem item : items) {
            parcel.writeString(item.getItemName());
            parcel.writeString(item.getImgPath());
            parcel.writeInt(item.getQuantity());
        }
    }
}
