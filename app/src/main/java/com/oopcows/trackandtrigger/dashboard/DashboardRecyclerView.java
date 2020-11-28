package com.oopcows.trackandtrigger.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.oopcows.trackandtrigger.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

import static com.oopcows.trackandtrigger.helpers.CowConstants.NORMAL_VIEW_HOLDER;
import static com.oopcows.trackandtrigger.helpers.CowConstants.SEARCH_RESULT_VIEW_HOLDER;

public abstract class DashboardRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final ArrayList<Object> dataSet;
    protected DashboardActivity dashboardActivity;
    protected RecyclerView recyclerView;
    protected ItemTouchHelper touchHelper;
    private String searchString;
    private int viewHolderType = 0;

    @SuppressWarnings("unchecked")
    public DashboardRecyclerView(DashboardActivity dashboardActivity, RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager, Object mDataSet) {
        dataSet = (ArrayList<Object>) mDataSet;
        this.dashboardActivity = dashboardActivity;
        touchHelper = new ItemTouchHelper(
                new ItemTouchHelper.Callback() {

                    @Override
                    public boolean isLongPressDragEnabled() {
                        return true;
                    }

                    @Override
                    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                        return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
                    }

                    @Override
                    public boolean onMove(@NotNull RecyclerView recyclerView,
                                          @NotNull RecyclerView.ViewHolder viewHolder, @NotNull RecyclerView.ViewHolder target) {
                        final int fromPosition = viewHolder.getAdapterPosition();
                        final int toPosition = target.getAdapterPosition();
                        if (fromPosition < toPosition) {
                            for (int i = fromPosition; i < toPosition; i++) {
                                Collections.swap(dataSet, i, i + 1);
                            }
                        } else {
                            for (int i = fromPosition; i > toPosition; i--) {
                                Collections.swap(dataSet, i, i - 1);
                            }
                        }
                        notifyItemMoved(fromPosition, toPosition);
                        return true;
                    }

                    @Override
                    public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                        deleteHolder(viewHolder);
                    }

                    @Override
                    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder,
                                                  int actionState) {


                        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                            onHolderSelected(viewHolder);
                        }

                        super.onSelectedChanged(viewHolder, actionState);
                    }

                });
        touchHelper.attachToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(this);
    }

    @NonNull
    @Override
    public final int getItemViewType (int position) {
        return searchString==null || searchString.isEmpty()? NORMAL_VIEW_HOLDER : SEARCH_RESULT_VIEW_HOLDER;
    }

    @NonNull
    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == NORMAL_VIEW_HOLDER) {
            return createNormalViewHolder(parent, viewType);
        }
        return createSearchViewHolder(parent, viewType);
    }

    @NonNull
    @Override
    public final void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof SearchResultViewHolder) {
            onSearchBind((SearchResultViewHolder) viewHolder, position);
            if(holderContainsString((SearchResultViewHolder) viewHolder, searchString)) {
                viewHolder.itemView.setVisibility(View.VISIBLE);
            }
            else {
                viewHolder.itemView.setVisibility(View.GONE);
            }
        }
        else {
            onNormalBind((NormalViewHolder) viewHolder, position);
        }
    }

    private void deleteHolder(RecyclerView.ViewHolder holder) {
        dataSet.remove(holder.getAdapterPosition());
        notifyItemRemoved(holder.getAdapterPosition());
    }

    protected abstract void onHolderSelected(RecyclerView.ViewHolder holder);

    protected abstract NormalViewHolder createNormalViewHolder(@NonNull ViewGroup parent, int viewType);
    protected abstract SearchResultViewHolder createSearchViewHolder(@NonNull ViewGroup parent, int viewType);
    protected abstract void onNormalBind(@NonNull NormalViewHolder normalViewHolder, int position);
    protected abstract void onSearchBind(@NonNull SearchResultViewHolder searchResultViewHolder, int position);
    protected abstract boolean holderContainsString(@NonNull SearchResultViewHolder searchResultViewHolder, String searchString);

    public void searchString(String searchString) {
        this.searchString = searchString;
        notifyDataSetChanged();
    }

    protected static abstract class NormalViewHolder extends RecyclerView.ViewHolder {
        public NormalViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    protected static abstract class SearchResultViewHolder extends RecyclerView.ViewHolder {
        public SearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
