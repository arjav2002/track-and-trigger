package com.oopcows.trackandtrigger.dashboard.todolists;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.oopcows.trackandtrigger.databinding.ActivityTodoListBinding;
import com.oopcows.trackandtrigger.helpers.TodoList;

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
        todoAdapter = new TodoAdapter(binding.todosLayout, new LinearLayoutManager(this), todoList.getTodos());
    }

    @Override
    public void onBackPressed() {
        todoList.setHeading(String.valueOf(binding.headingField.getText()));
        Intent intent = new Intent();
        intent.putExtra(TODO_LIST_INTENT_KEY, todoList);
        setResult(RESULT_OK, intent);
        finish();
    }
}