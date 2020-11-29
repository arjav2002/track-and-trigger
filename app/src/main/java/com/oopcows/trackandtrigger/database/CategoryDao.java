package com.oopcows.trackandtrigger.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.oopcows.trackandtrigger.helpers.Category;

import java.util.List;

import static com.oopcows.trackandtrigger.helpers.CowConstants.CATEGORIES_TABLE_NAME;
import static com.oopcows.trackandtrigger.helpers.CowConstants.CATEGORY_NAME_COLUMN;

@Dao
public interface CategoryDao {

    @Query("SELECT * from " + CATEGORIES_TABLE_NAME)
    List<Category> getCategories();

    @Query("SELECT * FROM " + CATEGORIES_TABLE_NAME + " WHERE " + CATEGORY_NAME_COLUMN + " = :categoryName limit 1")
    Category getCategory(String categoryName);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCategory(Category category);

    @Update
    void updateCategory(Category category);

    @Delete
    void deleteCategory(Category category);

}
