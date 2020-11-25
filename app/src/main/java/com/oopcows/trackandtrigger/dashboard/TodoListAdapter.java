package com.oopcows.trackandtrigger.dashboard;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oopcows.trackandtrigger.R;
import com.oopcows.trackandtrigger.helpers.Todo;
import com.oopcows.trackandtrigger.helpers.TodoList;

import java.util.ArrayList;

import static com.oopcows.trackandtrigger.helpers.CowConstants.TODO_LIST_INTENT_KEY;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoListHolder> {

    private ArrayList<TodoList> todoLists;
    private DashboardActivity dashboardActivity;

    public TodoListAdapter(DashboardActivity dashboardActivity, ArrayList<TodoList> todoLists) {
        this.todoLists = todoLists;
        this.dashboardActivity = dashboardActivity;
    }

    // @subs called to create each todolist grid cell based on position
    @NonNull
    @Override
    public TodoListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View todoListCell = LayoutInflater.from(dashboardActivity).inflate(R.layout.todo_list_view, parent, false);
        return new TodoListHolder(todoListCell, parent);
    }

    // @subs puts stuff inside each ViewHolder, ie, Each todo List grid
    @Override
    public void onBindViewHolder(@NonNull TodoListHolder holder, int position) {
        TodoList tl = todoLists.get(position);
        holder.heading.setText(tl.getHeading());
        holder.todosLayout.removeAllViews();
        for(Todo todo : tl.getTodos()) {
            TextView todoView = (TextView) LayoutInflater.from(dashboardActivity).inflate(R.layout.todo_view, holder.todosLayout, false);
            todoView.setText(todo.getTask());
            holder.todosLayout.addView(todoView);
        }
        holder.updateList(tl);
        holder.itemView.setOnClickListener((v) -> {
            dashboardActivity.gotoTodoListActivity(holder.todoList);
        });
    }

    @Override
    public int getItemCount() {
        return todoLists.size();
    }

    // @subs this is a class to encapsulate each TodoList cell of the todoList grid
    public static class TodoListHolder extends RecyclerView.ViewHolder {
        private final TextView heading;
        private final LinearLayout todosLayout;
        private TodoList todoList;
        private final ViewGroup parent;
        public TodoListHolder(@NonNull View itemView, @NonNull ViewGroup parent) {
            super(itemView);
            heading = itemView.findViewById(R.id.heading);
            todosLayout = itemView.findViewById(R.id.todos_layout);
            this.parent = parent;
        }
        public void updateList(TodoList todoList) {
            this.todoList = todoList;
            heading.setText(todoList.getHeading());
            for(Todo todo : todoList.getTodos()) {
                todosLayout.addView(LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_view, parent, false));
            }
        }
    }

}
