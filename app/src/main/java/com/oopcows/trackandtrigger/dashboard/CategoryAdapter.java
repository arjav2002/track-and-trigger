package com.oopcows.trackandtrigger.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oopcows.trackandtrigger.R;
import com.oopcows.trackandtrigger.dashboard.DashboardActivity;
import com.oopcows.trackandtrigger.dashboard.DashboardRecyclerView;
import com.oopcows.trackandtrigger.dashboard.TodoListAdapter;
import com.oopcows.trackandtrigger.helpers.Category;

import java.util.ArrayList;

public class CategoryAdapter extends DashboardRecyclerView {

    private final ArrayList<Category> categories;

    public CategoryAdapter(DashboardActivity dashboardActivity, RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager, ArrayList<Category> categories) {
        super(dashboardActivity, recyclerView, layoutManager, categories);
        this.categories = categories;
    }

    // @subs do funs here, make category button look like it is selected
    @Override
    protected void onHolderSelected(RecyclerView.ViewHolder holder) {

    }

    @Override
    protected NormalViewHolder createNormalViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryHolder(LayoutInflater.from(dashboardActivity).inflate(R.layout.category_button, parent, false));
    }

    @Override
    protected SearchResultViewHolder createSearchViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchCategoryHolder(LayoutInflater.from(dashboardActivity).inflate(R.layout.category_search_result, parent, false));
    }

    @Override
    protected void onNormalBind(@NonNull NormalViewHolder normalViewHolder, int position) {
        CategoryHolder categoryHolder = (CategoryHolder) normalViewHolder;
        categoryHolder.categoryButton.setText(categories.get(position).getCategoryName());
        categoryHolder.categoryButton.setOnClickListener((v) -> {
            dashboardActivity.gotoCategoryActivity(categories.get(position));
        });
    }

    @Override
    protected void onSearchBind(@NonNull SearchResultViewHolder searchResultViewHolder, int position) {
        SearchCategoryHolder holder = (SearchCategoryHolder) searchResultViewHolder;
        holder.categoryName.setText(categories.get(position).getCategoryName());
    }

    @Override
    protected boolean holderContainsString(@NonNull SearchResultViewHolder searchResultViewHolder, String searchString) {
        SearchCategoryHolder holder = (SearchCategoryHolder) searchResultViewHolder;
        return String.valueOf(holder.categoryName.getText()).contains(searchString);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    private static class CategoryHolder extends NormalViewHolder {
        private final Button categoryButton;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            categoryButton = itemView.findViewById(R.id.category_button);
        }
    }

    private static class SearchCategoryHolder extends SearchResultViewHolder {
        private final TextView categoryName;

        public SearchCategoryHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_text);
        }
    }

}
