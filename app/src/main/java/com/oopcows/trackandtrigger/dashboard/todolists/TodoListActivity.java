package com.oopcows.trackandtrigger.dashboard.todolists;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.google.type.DateTime;
import com.oopcows.trackandtrigger.MainActivity;
import com.oopcows.trackandtrigger.databinding.ActivityTodoListBinding;
import com.oopcows.trackandtrigger.helpers.TodoList;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.sql.Time;
import java.util.Calendar;

import static com.oopcows.trackandtrigger.helpers.CowConstants.TODO_LIST_INTENT_KEY;

public class TodoListActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {

    private TodoList todoList;
    private TodoAdapter todoAdapter;
    private ActivityTodoListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTodoListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        todoList = (TodoList) intent.getExtras().get(TODO_LIST_INTENT_KEY);
        binding.headingField.setText(todoList.getHeading());
        todoAdapter = new TodoAdapter(this, binding.todosLayout, new LinearLayoutManager(this), todoList.getTodos());
    }

    @Override
    public void onBackPressed() {
        todoList.setHeading(String.valueOf(binding.headingField.getText()));
        Intent intent = new Intent();
        intent.putExtra(TODO_LIST_INTENT_KEY, todoList);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        todoAdapter.setDate(year, monthOfYear, dayOfMonth);

        DateTime time = DateTime.getDefaultInstance();
        TimePickerDialog dpd = TimePickerDialog.newInstance(
                this, true);
        dpd.show(getSupportFragmentManager(), "TimepickerDialog");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        todoAdapter.setTime(hourOfDay, minute);
        todoAdapter.finishSettingDateTime();
    }

    public void setDateTime() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );
        dpd.show(getSupportFragmentManager(), "Datepickerdialog");
    }
}