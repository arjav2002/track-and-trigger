package com.oopcows.trackandtrigger.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.oopcows.trackandtrigger.R;
import com.oopcows.trackandtrigger.dashboard.categories.CategoryActivity;
import com.oopcows.trackandtrigger.dashboard.todolists.TodoListActivity;
import com.oopcows.trackandtrigger.database.DatabaseHelper;
import com.oopcows.trackandtrigger.helpers.Category;
import com.oopcows.trackandtrigger.helpers.CategoryItem;
import com.oopcows.trackandtrigger.helpers.Profession;
import com.oopcows.trackandtrigger.databinding.ActivityDashboardBinding;
import com.oopcows.trackandtrigger.helpers.TodoList;
import com.oopcows.trackandtrigger.helpers.UserAccount;

import java.util.ArrayList;
import java.util.Arrays;

import static com.oopcows.trackandtrigger.helpers.CowConstants.CATEGORY_INTENT_KEY;
import static com.oopcows.trackandtrigger.helpers.CowConstants.CATEGORY_REQUEST_CODE;
import static com.oopcows.trackandtrigger.helpers.CowConstants.SPECIAL_CATEGORIES_NAME_RESIDS;
import static com.oopcows.trackandtrigger.helpers.CowConstants.TODO_LIST_INTENT_KEY;
import static com.oopcows.trackandtrigger.helpers.CowConstants.TODO_LIST_REQUEST_CODE;
import static com.oopcows.trackandtrigger.helpers.CowConstants.USER_ACCOUNT_INTENT_KEY;

public class DashboardActivity extends AppCompatActivity implements ProfessionChooseFragment.ProfessionFillable {

    private UserAccount userAccount;
    private DatabaseHelper dh;
    private ActivityDashboardBinding binding;
    private View homeMaintenanceButton, kitchenApplianceButton;
    private ArrayList<TodoList> todoLists;
    private ArrayList<Category> categories;
    private int todoListClicked;
    private int categoryClicked;
    private TodoListAdapter todoListAdapter;
    private CategoryAdapter categoryAdapter;
    private Category[] specialCategories;
    // @subs dashboardActivity should be in a sort of shadow while the dialogue is open
    // so that it is not visible until the profession has been chosen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userAccount = getIntent().getExtras().getParcelable(USER_ACCOUNT_INTENT_KEY);
        dh = DatabaseHelper.getInstance(this);

        todoLists = new ArrayList<TodoList>(dh.getTodoLists());
        categories = new ArrayList<Category>();
        todoListClicked = categoryClicked = -1;
        specialCategories = new Category[SPECIAL_CATEGORIES_NAME_RESIDS.length];

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
            // if new account
            // @vraj upload empty "house" category (use R.string.home_maintenance)
            // else if old account, download "house" category
            specialCategories[getSpecialCategoryIndex(R.string.home_maintenance)] = new Category(getString(R.string.home_maintenance));
            homeMaintenanceButton.setOnClickListener((v) -> {
                System.out.println("house ko maintain karna achhi baat hai");
                gotoCategoryActivity(specialCategories[getSpecialCategoryIndex(R.string.home_maintenance)]);
            });

        }
        if(userAccount.getProfession().equals(Profession.homeMaker)) {
            binding.specialButtons.addView(kitchenApplianceButton);
            // @vraj "kitchen" category (use R.string.kitchen_appliances) same as "house" category
            specialCategories[getSpecialCategoryIndex(R.string.kitchen_appliances)] = new Category(getString(R.string.kitchen_appliances));;
            kitchenApplianceButton.setOnClickListener((v) -> {
                gotoCategoryActivity(specialCategories[getSpecialCategoryIndex(R.string.kitchen_appliances)]);
            });
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
        // @vraj "groceries" category (use R.string.groceries) same as "house" category
        specialCategories[getSpecialCategoryIndex(R.string.groceries)] = new Category(getString(R.string.groceries));;
        groceryButton.setOnClickListener((v) -> {
            gotoCategoryActivity(specialCategories[getSpecialCategoryIndex(R.string.groceries)]);
        });

        todoListAdapter = new TodoListAdapter(this, binding.todoListsView, new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL), todoLists);
        categoryAdapter = new CategoryAdapter(this, binding.categoriesView, new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL), categories);

        binding.addTodoList.setOnClickListener((v) -> {
            TodoList todoList = new TodoList();
            todoLists.add(todoList);
            gotoTodoListActivity(todoList);
        });

        binding.addCategory.setOnClickListener((v) -> {
            Category category = new Category("");
            categories.add(category);
            gotoCategoryActivity(category);
        });

        if(userAccount.getProfession() == Profession.nullProfession) {
            displayDialogue();
        }
        else addAppropriateButtons();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TODO_LIST_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                TodoList todoList = (TodoList) data.getExtras().get(TODO_LIST_INTENT_KEY);
                todoLists.set(todoListClicked, todoList);
                todoListAdapter.notifyItemChanged(todoListClicked);
                todoListClicked = -1;
                dh.insertTodoList(todoList);
            }
        }
        else if (requestCode == CATEGORY_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                Category category = (Category) data.getExtras().get(CATEGORY_INTENT_KEY);
                setCategory(category);
                dh.insertCategory(category);
            }
        }
    }

    public void gotoTodoListActivity(TodoList todoList) {
        Intent intent = new Intent(this, TodoListActivity.class);
        intent.putExtra(TODO_LIST_INTENT_KEY, todoList);
        todoListClicked = todoLists.indexOf(todoList);
        startActivityForResult(intent, TODO_LIST_REQUEST_CODE);
    }

    public void gotoCategoryActivity(Category category) {
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra(CATEGORY_INTENT_KEY, category);
        categoryClicked = categories.indexOf(category);
        if(categoryClicked == -1) {
            for (int specialCategoriesNameResid : SPECIAL_CATEGORIES_NAME_RESIDS) {
                categoryClicked--;
                if (category.getCategoryName().equalsIgnoreCase(getString(specialCategoriesNameResid))) {
                    break;
                }
            }
        }
        startActivityForResult(intent, CATEGORY_REQUEST_CODE);
    }

    private void setCategory(Category category) {
        System.out.println(categoryClicked);
        if(categoryClicked < -1) {
            specialCategories[-(categoryClicked+2)] = category;
        }
        else {
            categories.set(categoryClicked, category);
            categoryAdapter.notifyItemChanged(categoryClicked);
        }
        categoryClicked = -1;
    }

    private int getSpecialCategoryIndex(int resId) {
        for(int i = 0; i < specialCategories.length; i++) {
            if(resId == SPECIAL_CATEGORIES_NAME_RESIDS[i]) return i;
        }
        return -1;
    }

}