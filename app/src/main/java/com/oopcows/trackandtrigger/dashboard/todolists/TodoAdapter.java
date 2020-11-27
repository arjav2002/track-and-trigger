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

    public TodoAdapter(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager, ArrayList<Todo> todos) {
        super(recyclerView, layoutManager, todos);
        this.todos = todos;
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
                    todos.set(holder.getAdapterPosition(), new Todo(String.valueOf(charSequence), todos.get(holder.getAdapterPosition()).isDone()));
                }

                @Override
                public void afterTextChanged(Editable editable) {}
            });
        }
        else {
            AddTodoHolder addTodoHolder = (AddTodoHolder) holder;
            addTodoHolder.addButton.setOnClickListener((v) -> {
                todos.add(new Todo("", false));
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

        public TodoHolder(@NonNull View itemView) {
            super(itemView);
            dragButton = itemView.findViewById(R.id.dragButton);
            checkbox = itemView.findViewById(R.id.checkbox);
            todo = itemView.findViewById(R.id.todo_field);
            removeButton = itemView.findViewById(R.id.remove_todo);
        }
    }

    public static class AddTodoHolder extends RecyclerView.ViewHolder {
        private final Button addButton;

        public AddTodoHolder(@NonNull View itemView) {
            super(itemView);
            addButton = itemView.findViewById(R.id.add_todo);
        }
    }

    @Override
    protected void deleteHolder(RecyclerView.ViewHolder holder) {
        int pos = holder.getAdapterPosition();
        todos.remove(todos.get(pos));
        notifyItemRemoved(pos);
    }

}
