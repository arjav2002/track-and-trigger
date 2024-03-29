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

import static com.oopcows.trackandtrigger.helpers.CowConstants.CATEGORY_DRAWABLE_RESIDS;

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
        int index = dashboardActivity.getSpecialCategoryIndex(
                String.valueOf(categoryHolder.categoryButton.getText()));
        if(index != -1) {
            categoryHolder.categoryButton.setCompoundDrawablesWithIntrinsicBounds(
                    CATEGORY_DRAWABLE_RESIDS[index], 0, 0, 0);
        }
    }

    @Override
    protected void onSearchBind(@NonNull SearchResultViewHolder searchResultViewHolder, int position) {
        SearchCategoryHolder holder = (SearchCategoryHolder) searchResultViewHolder;
        holder.categoryName.setText(categories.get(position).getCategoryName());
    }

    @Override
    protected boolean holderContainsString(@NonNull SearchResultViewHolder searchResultViewHolder, String searchString) {
        SearchCategoryHolder holder = (SearchCategoryHolder) searchResultViewHolder;
        return containsIgnoreCase(String.valueOf(holder.categoryName.getText()), searchString);
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

    // @subs maybe display rest of the items of the category and highlight the matching ones?
    // could do the same for todolistadapter but see if you can come up with something better
    private static class SearchCategoryHolder extends SearchResultViewHolder {
        private final TextView categoryName;

        public SearchCategoryHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_text);
        }
    }

}
