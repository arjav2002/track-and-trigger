package com.oopcows.trackandtrigger.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;

import com.oopcows.trackandtrigger.R;
import com.oopcows.trackandtrigger.databinding.ActivityTodoListBinding;
import com.oopcows.trackandtrigger.helpers.Todo;
import com.oopcows.trackandtrigger.helpers.TodoList;

import java.util.zip.Inflater;

import static com.oopcows.trackandtrigger.helpers.CowConstants.TODO_LIST_INTENT_KEY;

public class TodoListActivity extends AppCompatActivity {

    private TodoList todoList;
    private ActivityTodoListBinding binding;
    private TodoAdapter todoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTodoListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        todoList = (TodoList) intent.getExtras().get(TODO_LIST_INTENT_KEY);
        binding.headingField.setText(todoList.getHeading());
        todoAdapter = new TodoAdapter(binding.todosLayout, todoList.getTodos());
        binding.todosLayout.setLayoutManager(new LinearLayoutManager(this));
        binding.todosLayout.setAdapter(todoAdapter);
        binding.addTodo.setOnClickListener((v) -> {
            todoAdapter.updateTodos();
            todoList.addTodo(new Todo("", false));
            todoAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onBackPressed() {
        todoAdapter.updateTodos();
        todoList.setHeading(String.valueOf(binding.headingField.getText()));
        Intent intent = new Intent();
        intent.putExtra(TODO_LIST_INTENT_KEY, todoList);
        setResult(RESULT_OK, intent);
        finish();
    }
}