package com.oopcows.trackandtrigger.dashboard.todolists;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryHolder(LayoutInflater.from(dashboardActivity).inflate(R.layout.category_button, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof CategoryHolder) {
            CategoryHolder categoryHolder = (CategoryHolder) holder;
            categoryHolder.categoryButton.setText(categories.get(position).getCategoryName());
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    private static class CategoryHolder extends RecyclerView.ViewHolder {

        private final Button categoryButton;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            categoryButton = itemView.findViewById(R.id.category_button);
        }
    }

}
