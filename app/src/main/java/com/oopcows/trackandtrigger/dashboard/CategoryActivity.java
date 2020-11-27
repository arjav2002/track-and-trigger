package com.oopcows.trackandtrigger.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.oopcows.trackandtrigger.databinding.ActivityCategoryBinding;
import com.oopcows.trackandtrigger.helpers.Category;

import static com.oopcows.trackandtrigger.helpers.CowConstants.CATEGORY_INTENT_KEY;

public class CategoryActivity extends AppCompatActivity {

    private ActivityCategoryBinding binding;
    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        category = (Category) intent.getExtras().get(CATEGORY_INTENT_KEY);
        binding.categoryName.setText(category.getCategoryName());

    }

    @Override
    public void onBackPressed() {
        Category newCategory = new Category(String.valueOf(binding.categoryName.getText()), category.getItems());
        Intent intent = new Intent();
        intent.putExtra(CATEGORY_INTENT_KEY, newCategory);
        setResult(RESULT_OK, intent);
        finish();
    }
}