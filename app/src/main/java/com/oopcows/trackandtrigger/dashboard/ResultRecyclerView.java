package com.oopcows.trackandtrigger.dashboard;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.oopcows.trackandtrigger.dashboard.todolists.TodoAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public abstract class ResultRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<Object> dataSet;
    protected ItemTouchHelper touchHelper;
    protected RecyclerView recyclerView;

    @SuppressWarnings("unchecked")
    public ResultRecyclerView(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager, Object mDataSet) {
        dataSet = (ArrayList<Object>) mDataSet;
        touchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean isLongPressDragEnabled() {
                        return false;
                    }

                    @Override
                    public boolean onMove(@NotNull RecyclerView recyclerView,
                                          @NotNull RecyclerView.ViewHolder viewHolder, @NotNull RecyclerView.ViewHolder target) {
                        if(target instanceof TodoAdapter.AddTodoHolder) return false;
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
                });
        touchHelper.attachToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(this);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    protected abstract void deleteHolder(RecyclerView.ViewHolder holder);
}
