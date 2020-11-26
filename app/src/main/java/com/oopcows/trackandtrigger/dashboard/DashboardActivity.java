package com.oopcows.trackandtrigger.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.oopcows.trackandtrigger.database.DatabaseHelper;
import com.oopcows.trackandtrigger.helpers.Profession;
import com.oopcows.trackandtrigger.databinding.ActivityDashboardBinding;
import com.oopcows.trackandtrigger.helpers.Todo;
import com.oopcows.trackandtrigger.helpers.TodoList;
import com.oopcows.trackandtrigger.helpers.UserAccount;

import java.util.ArrayList;
import java.util.LinkedList;

import static com.oopcows.trackandtrigger.helpers.CowConstants.TODO_LIST_INTENT_KEY;
import static com.oopcows.trackandtrigger.helpers.CowConstants.USER_ACCOUNT_INTENT_KEY;

public class DashboardActivity extends AppCompatActivity implements ProfessionChooseFragment.ProfessionFillable {

    private UserAccount userAccount;
    private DatabaseHelper dh;
    private ActivityDashboardBinding binding;
    private View homeMaintenanceButton, kitchenApplianceButton;
    private ArrayList<TodoList> todoLists;
    private int todoListClicked;
    private TodoListAdapter todoListAdapter;
    // @subs dashboardActivity should be in a sort of shadow while the dialogue is open
    // so that it is not visible until the profession has been chosen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userAccount = getIntent().getExtras().getParcelable(USER_ACCOUNT_INTENT_KEY);
        dh = DatabaseHelper.getInstance(this);

        todoLists = new ArrayList<TodoList>(dh.getTodoLists());
        todoListClicked = -1;

        createUI();
    }

    private void displayDialogue() {
        FragmentManager fm = getSupportFragmentManager();
        ProfessionChooseFragment.newInstance().show(fm, null);
    }

    @Override
    public void fillDetails(Profession profession) {
        userAccount = new UserAccount(userAccount.getUsername(), userAccount.getGmailId(), userAccount.getPhno(), profession);
        dh.updateUser(userAccount);
        addAppropriateButtons();
    }

    private void addAppropriateButtons() {
        if(!userAccount.getProfession().equals(Profession.jobSeeker)) {
            binding.specialButtons.addView(homeMaintenanceButton);
        }
        if(userAccount.getProfession().equals(Profession.homeMaker)) {
            binding.specialButtons.addView(kitchenApplianceButton);
        }
    }

    private void createUI() {
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        View groceryButton = binding.groceryListButton;
        homeMaintenanceButton = binding.mainatenanceListButton;
        kitchenApplianceButton = binding.kitchenAppliancesButton;
        binding.specialButtons.removeAllViews();
        binding.specialButtons.addView(groceryButton);

        todoListAdapter = new TodoListAdapter(this, todoLists);
        binding.myRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        binding.myRecyclerView.setAdapter(todoListAdapter);

        binding.addTodoList.setOnClickListener((v) -> {
            TodoList todoList = new TodoList();
            todoLists.add(todoList);
            gotoTodoListActivity(todoList);
        });

        if(userAccount.getProfession() == Profession.nullProfession) {
            displayDialogue();
        }
        else addAppropriateButtons();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                TodoList todoList = (TodoList) data.getExtras().get(TODO_LIST_INTENT_KEY);
                todoLists.set(todoListClicked, todoList);
                todoListAdapter.notifyItemChanged(todoListClicked);
                todoListClicked = -1;
                dh.insertTodoList(todoList);
            }
        }
    }

    public void gotoTodoListActivity(TodoList todoList) {
        Intent intent = new Intent(this, TodoListActivity.class);
        intent.putExtra(TODO_LIST_INTENT_KEY, todoList);
        todoListClicked = todoLists.indexOf(todoList);
        startActivityForResult(intent, 1);
    }

}