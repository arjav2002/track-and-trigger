package com.oopcows.trackandtrigger.dashboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.oopcows.trackandtrigger.R;
import com.oopcows.trackandtrigger.helpers.Todo;
import com.oopcows.trackandtrigger.helpers.TodoList;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

import static com.oopcows.trackandtrigger.helpers.CowConstants.TODO_LIST_INTENT_KEY;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoListHolder> {

    private ArrayList<TodoList> todoLists;
    private DashboardActivity dashboardActivity;
    private RecyclerView recyclerView;
    private ItemTouchHelper touchHelper;

    public TodoListAdapter(DashboardActivity dashboardActivity, RecyclerView recyclerView, ArrayList<TodoList> todoLists) {
        this.todoLists = todoLists;
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
                                Collections.swap(todoLists, i, i + 1);
                            }
                        } else {
                            for (int i = fromPosition; i > toPosition; i--) {
                                Collections.swap(todoLists, i, i - 1);
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
                            if (viewHolder instanceof TodoListHolder) {
                                onTodoListSelected((TodoListHolder) viewHolder);
                            }
                        }

                        super.onSelectedChanged(viewHolder, actionState);
                    }

                });
        touchHelper.attachToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public TodoListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View todoListCell = LayoutInflater.from(dashboardActivity).inflate(R.layout.todo_list_view, parent, false);
        return new TodoListHolder(todoListCell);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoListHolder holder, int position) {
        TodoList tl = todoLists.get(position);

        holder.heading.setText(tl.getHeading());
        holder.todosLayout.removeAllViews();
        for (Todo todo : tl.getTodos()) {
            TextView todoView = (TextView) LayoutInflater.from(dashboardActivity).inflate(R.layout.todo_view, holder.todosLayout, false);
            todoView.setText(todo.getTask());
            holder.todosLayout.addView(todoView);
        }

        holder.itemView.setOnLongClickListener((v) -> {return true;});
        holder.itemView.setOnClickListener((v) -> {
            dashboardActivity.gotoTodoListActivity(todoLists.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return todoLists.size();
    }

    // @subs fyi this is a class to encapsulate each TodoList cell of the todoList grid
    public static class TodoListHolder extends RecyclerView.ViewHolder {
        private final TextView heading;
        private final LinearLayout todosLayout;
        public TodoListHolder(@NonNull View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.heading);
            todosLayout = itemView.findViewById(R.id.todos_layout);
        }
    }

    private void deleteHolder(RecyclerView.ViewHolder holder) {
        todoLists.remove(holder.getAdapterPosition());
        notifyItemRemoved(holder.getAdapterPosition());
    }

    // @subs do funs here, this function is called whenever holder is selected
    // make the holder.itemView look like it is selected
    private void onTodoListSelected(TodoListHolder holder) {
        System.out.println("I am selected yo");
    }

}
