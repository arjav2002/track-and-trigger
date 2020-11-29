package com.oopcows.trackandtrigger.dashboard.categories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import com.oopcows.trackandtrigger.R;
import com.oopcows.trackandtrigger.databinding.ActivityCategoryBinding;
import com.oopcows.trackandtrigger.helpers.Category;
import com.oopcows.trackandtrigger.helpers.CategoryItem;

import static com.oopcows.trackandtrigger.helpers.CowConstants.CATEGORY_INTENT_KEY;
import static com.oopcows.trackandtrigger.helpers.CowConstants.CHOOSE_PICTURE_REQUEST_CODE;
import static com.oopcows.trackandtrigger.helpers.CowConstants.SPECIAL_CATEGORIES_NAME_RESIDS;
import static com.oopcows.trackandtrigger.helpers.CowConstants.TAKE_PHOTO_REQUEST_CODE;

public class CategoryActivity extends AppCompatActivity {

    private ActivityCategoryBinding binding;
    private Category category;
    private ItemAdapter itemAdapter;
    private String specialCategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        specialCategoryName = "";

        Intent intent = getIntent();
        category = (Category) intent.getExtras().get(CATEGORY_INTENT_KEY);
        binding.categoryName.setText(category.getCategoryName());
        itemAdapter = new ItemAdapter(this, binding.itemsLayout, new LinearLayoutManager(this), category.getItems());
        binding.addItemButton.setOnClickListener((v) -> {
            category.getItems().add(new CategoryItem("", "", 0));
            itemAdapter.notifyItemChanged(category.getItems().size()-1);
        });

        for(int resid : SPECIAL_CATEGORIES_NAME_RESIDS) {
            if(getString(resid).equalsIgnoreCase(category.getCategoryName())) {
                specialCategoryName = category.getCategoryName();
                break;
            }
        }

    }

    @Override
    public void onBackPressed() {
        if(String.valueOf(binding.categoryName.getText()).isEmpty()) {
            Toast.makeText(this, R.string.empty_category_name, Toast.LENGTH_SHORT).show();
            return;
        }
        if(specialCategoryName.isEmpty()) {
            for (int res_id : SPECIAL_CATEGORIES_NAME_RESIDS) {
                if (String.valueOf(binding.categoryName.getText()).equalsIgnoreCase(getString(res_id))) {
                    Toast.makeText(this, String.valueOf(binding.categoryName.getText()) + " " + getString(R.string.is_reserved_category_name), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        else {
            if(!String.valueOf(binding.categoryName.getText()).equals(specialCategoryName)) {
                Toast.makeText(this, getString(R.string.cannot_change_category_name) + ", " + getString(R.string.change_it_back) + " " + specialCategoryName , Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Category newCategory = new Category(String.valueOf(binding.categoryName.getText()), category.getItems());
        Intent intent = new Intent();
        intent.putExtra(CATEGORY_INTENT_KEY, newCategory);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case TAKE_PHOTO_REQUEST_CODE:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        itemAdapter.getSelectedImageButton().setImageBitmap(selectedImage);
                    }
                    break;
                case CHOOSE_PICTURE_REQUEST_CODE:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage =  data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                System.out.println(picturePath);
                                itemAdapter.getSelectedImageButton().setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }
}