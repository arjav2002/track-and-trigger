package com.oopcows.trackandtrigger.dashboard.todolists;

import android.database.CursorJoiner;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.oopcows.trackandtrigger.R;
import com.oopcows.trackandtrigger.dashboard.ResultRecyclerView;
import com.oopcows.trackandtrigger.helpers.Todo;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import static com.oopcows.trackandtrigger.helpers.CowConstants.ADD_VIEW_HOLDER;
import static com.oopcows.trackandtrigger.helpers.CowConstants.TODO_VIEW_HOLDER;

public class TodoAdapter extends ResultRecyclerView {

    private final ArrayList<Todo> todos;

    private int itemSelected;
    private TodoListActivity todoListActivity;
    private int[] arr;

    public TodoAdapter(TodoListActivity todoListActivity, RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager, ArrayList<Todo> todos) {
        super(recyclerView, layoutManager, todos);
        this.todos = todos;
        itemSelected = -1;
        this.todoListActivity = todoListActivity;
        arr = new int[5];
    }

    @Override
    public int getItemViewType(int position) {
        return (position == todos.size()) ? ADD_VIEW_HOLDER : TODO_VIEW_HOLDER;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TODO_VIEW_HOLDER)
            return new TodoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_draggable, parent, false));
        return new AddTodoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.add_todo_button, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof TodoHolder) {
            TodoHolder todoHolder = (TodoHolder) holder;
            todoHolder.todo.setText(todos.get(position).getTask());
            todoHolder.checkbox.setChecked(todos.get(position).isDone());
            todoHolder.dragButton.setTouchHelper(touchHelper);
            todoHolder.dragButton.setViewHolder(todoHolder);
            todoHolder.checkbox.setOnClickListener((v) -> {
                int pos = holder.getAdapterPosition();
                todos.get(pos).setDone(!todos.get(pos).isDone());
            });
            todoHolder.removeButton.setOnClickListener((v) -> {
                deleteHolder(holder);
            });
            todoHolder.todo.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String str = String.valueOf(((TodoHolder) holder).dateTimeView.getText());
                    todos.set(holder.getAdapterPosition(), new Todo(String.valueOf(charSequence), todos.get(holder.getAdapterPosition()).isDone(), str==null||str.isEmpty()?"0 0 0 0 0":str.replace("/", " ").replace("\t", " ").replace(":", " "), todos.get(position).getIntent(), todos.get(position).getEventId()));
                }

                @Override
                public void afterTextChanged(Editable editable) {}
            });
            todoHolder.setDateTime.setOnClickListener((v) -> {
                itemSelected = todoHolder.getAdapterPosition();
                todoListActivity.setDateTime();
            });
            arr = Todo.getTimeFromString(todos.get(position).getTimeString());
            System.out.println(arr);
            todoHolder.dateTimeView.setText(arr[2] + "/" + arr[3] + "/" + arr[4] + "\t" + (arr[1] < 10 ? "0"+arr[1] : arr[1]) +":" + (arr[0] < 10 ? "0"+arr[0] : arr[0]));
        }
        else {
            AddTodoHolder addTodoHolder = (AddTodoHolder) holder;
            addTodoHolder.addButton.setOnClickListener((v) -> {
                todos.add(new Todo("", false, "0 0 0 0 0"));
                notifyItemChanged(holder.getAdapterPosition());
            });
        }
    }

    @Override
    public int getItemCount() {
        return todos.size()+1;
    }

    public static class TodoHolder extends RecyclerView.ViewHolder {
        private final DragButton dragButton;
        private final CheckBox checkbox;
        private final EditText todo;
        private final ImageButton removeButton;
        private final Button setDateTime;
        private final TextView dateTimeView;

        public TodoHolder(@NonNull View itemView) {
            super(itemView);
            dragButton = itemView.findViewById(R.id.dragButton);
            checkbox = itemView.findViewById(R.id.checkbox);
            todo = itemView.findViewById(R.id.todo_field);
            removeButton = itemView.findViewById(R.id.remove_todo);
            setDateTime = itemView.findViewById(R.id.set_date_time);
            dateTimeView = itemView.findViewById(R.id.date_time_label);
        }
    }

    public static class AddTodoHolder extends RecyclerView.ViewHolder {
        private final Button addButton;

        public AddTodoHolder(@NonNull View itemView) {
            super(itemView);
            addButton = itemView.findViewById(R.id.add_todo);
        }
    }

    public void setTime(int hour, int minute) {
        arr[1] = hour;
        arr[0] = minute;
    }

    public void setDate(int year, int month, int day) {
        arr[4] = year;
        arr[3] = month;
        arr[2] = day;
    }

    public void finishSettingDateTime() {
        TodoHolder holder = (TodoHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(itemSelected));
        todos.set(itemSelected, new Todo(todos.get(itemSelected).getTask(), todos.get(itemSelected).isDone(), arr[4], arr[3], arr[2], arr[1], arr[0], todos.get(itemSelected).getIntent(), todos.get(itemSelected).getEventId()));
        holder.dateTimeView.setText(arr[2] + "/" + arr[3] + "/" + arr[4] + "\t" + (arr[1] < 10 ? "0"+arr[1] : arr[1]) +":" + (arr[0] < 10 ? "0"+arr[0] : arr[0]));
    }
}
