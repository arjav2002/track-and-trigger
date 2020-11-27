package com.oopcows.trackandtrigger.dashboard;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public abstract class DashboardRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final ArrayList<Object> dataSet;
    protected DashboardActivity dashboardActivity;
    protected RecyclerView recyclerView;
    protected ItemTouchHelper touchHelper;

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

    private void deleteHolder(RecyclerView.ViewHolder holder) {
        dataSet.remove(holder.getAdapterPosition());
        notifyItemRemoved(holder.getAdapterPosition());
    }

    protected abstract void onHolderSelected(RecyclerView.ViewHolder holder);

}
