package com.oopcows.trackandtrigger.dashboard;

import android.content.ClipData;
import android.graphics.Canvas;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.oopcows.trackandtrigger.R;
import com.oopcows.trackandtrigger.helpers.Todo;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.LinkedList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoHolder> {

    private LinkedList<Todo> todos;
    private ItemTouchHelper touchHelper;
    private RecyclerView recyclerView;

    public TodoAdapter(RecyclerView recyclerView, LinkedList<Todo> todos) {
        this.todos = todos;
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
                        final int fromPosition = viewHolder.getAdapterPosition();
                        final int toPosition = target.getAdapterPosition();
                        if (fromPosition < toPosition) {
                            for (int i = fromPosition; i < toPosition; i++) {

                                Collections.swap(todos, i, i + 1);
                            }
                        } else {
                            for (int i = fromPosition; i > toPosition; i--) {
                                Collections.swap(todos, i, i - 1);
                            }
                        }
                        notifyItemMoved(fromPosition, toPosition);
                        return true;
                    }

                    @Override
                    public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                        todos.remove(viewHolder.getAdapterPosition());
                        notifyItemRemoved(viewHolder.getAdapterPosition());
                    }


                });
        touchHelper.attachToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public TodoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TodoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_draggable, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TodoHolder holder, int position) {
        holder.todo.setText(todos.get(position).getTask());
        holder.dragButton.setTouchHelper(touchHelper);
        holder.dragButton.setViewHolder(holder);
        holder.checkbox.setOnClickListener((v) -> {
            int pos = holder.getAdapterPosition();
            todos.get(pos).setDone(!todos.get(pos).isDone());
        });
        holder.checkbox.setChecked(todos.get(position).isDone());
        updateTodos();
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    public static class TodoHolder extends RecyclerView.ViewHolder {
        private final DragButton dragButton;
        private final CheckBox checkbox;
        private final EditText todo;

        public TodoHolder(@NonNull View itemView) {
            super(itemView);
            dragButton = itemView.findViewById(R.id.dragButton);
            checkbox = itemView.findViewById(R.id.checkbox);
            todo = itemView.findViewById(R.id.todo_field);
        }
    }

    public void updateTodos() {
        for(int i = 0; i < getItemCount(); i++) {
            TodoHolder holder = (TodoHolder) recyclerView.findViewHolderForAdapterPosition(i);
            if(holder == null) continue;
            System.out.println(i + " " + holder.checkbox.isChecked());
            todos.set(i, new Todo(String.valueOf(holder.todo.getText()), holder.checkbox.isChecked()));
        }
    }
}
