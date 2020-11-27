package com.oopcows.trackandtrigger.dashboard.todolists;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.oopcows.trackandtrigger.R;
import com.oopcows.trackandtrigger.dashboard.DashboardActivity;
import com.oopcows.trackandtrigger.dashboard.DashboardRecyclerView;
import com.oopcows.trackandtrigger.helpers.Todo;
import com.oopcows.trackandtrigger.helpers.TodoList;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class TodoListAdapter extends DashboardRecyclerView {

    private final ArrayList<TodoList> todoLists;

    public TodoListAdapter(DashboardActivity dashboardActivity, RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager, ArrayList<TodoList> todoLists) {
        super(dashboardActivity, recyclerView, layoutManager, todoLists);
        this.todoLists = todoLists;
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


    // @subs do funs here, this function is called whenever holder is selected
    // make the holder.itemView look like it is selected
    @Override
    protected void onHolderSelected(RecyclerView.ViewHolder holder) {
        System.out.println("I am selected yo");
    }

}
