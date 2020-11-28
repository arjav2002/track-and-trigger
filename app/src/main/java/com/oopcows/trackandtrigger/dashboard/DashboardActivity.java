package com.oopcows.trackandtrigger.dashboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

import com.google.android.material.navigation.NavigationView;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.SimpleOnSearchActionListener;
import com.oopcows.trackandtrigger.R;
import com.oopcows.trackandtrigger.dashboard.categories.CategoryActivity;
import com.oopcows.trackandtrigger.dashboard.todolists.TodoListActivity;
import com.oopcows.trackandtrigger.database.DatabaseHelper;
import com.oopcows.trackandtrigger.helpers.Category;
import com.oopcows.trackandtrigger.helpers.Profession;
import com.oopcows.trackandtrigger.databinding.ActivityDashboardBinding;
import com.oopcows.trackandtrigger.helpers.TodoList;
import com.oopcows.trackandtrigger.helpers.UserAccount;

import java.util.ArrayList;

import static com.oopcows.trackandtrigger.helpers.CowConstants.CATEGORY_INTENT_KEY;
import static com.oopcows.trackandtrigger.helpers.CowConstants.CATEGORY_REQUEST_CODE;
import static com.oopcows.trackandtrigger.helpers.CowConstants.IS_NEW_ACCOUNT_INTENT_KEY;
import static com.oopcows.trackandtrigger.helpers.CowConstants.SPECIAL_CATEGORIES_NAME_RESIDS;
import static com.oopcows.trackandtrigger.helpers.CowConstants.TODO_LIST_INTENT_KEY;
import static com.oopcows.trackandtrigger.helpers.CowConstants.TODO_LIST_REQUEST_CODE;
import static com.oopcows.trackandtrigger.helpers.CowConstants.USER_ACCOUNT_INTENT_KEY;

public class DashboardActivity extends AppCompatActivity implements ProfessionChooseFragment.ProfessionFillable {

    private UserAccount userAccount;
    private DatabaseHelper dh;
    private ActivityDashboardBinding binding;
    private MaterialSearchBar searchBar;
    private View homeMaintenanceButton, kitchenApplianceButton;
    private ArrayList<TodoList> todoLists;
    private ArrayList<Category> categories;
    private int todoListClicked;
    private int categoryClicked;
    private TodoListAdapter todoListAdapter;
    private CategoryAdapter categoryAdapter;
    private Category[] specialCategories;
    private boolean isNewAccount;
    private String searchString;
    private DrawerLayout drawerLayout;
    private RelativeLayout leftRL;
    // @subs dashboardActivity should be in a sort of shadow while the dialogue is open
    // so that it is not visible until the profession has been chosen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userAccount = getIntent().getExtras().getParcelable(USER_ACCOUNT_INTENT_KEY);
        isNewAccount = getIntent().getBooleanExtra(IS_NEW_ACCOUNT_INTENT_KEY, false);
        dh = DatabaseHelper.getInstance(this);

        todoLists = new ArrayList<TodoList>();
        categories = new ArrayList<Category>();
        todoListClicked = categoryClicked = -1;
        specialCategories = new Category[SPECIAL_CATEGORIES_NAME_RESIDS.length];
        searchString = "";

        createUI();

        downloadAndSortCategories();
        downloadTodoLists();
        clearDatabase();
        writeToDatabase();
    }

    @Override
    protected void onStart() {
        super.onStart();

        todoListAdapter = new TodoListAdapter(this, binding.todoListsView, new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL), todoLists);
        categoryAdapter = new CategoryAdapter(this, binding.categoriesView, new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL), categories);

        if (categoryAdapter.getItemCount() == 0) {
            binding.categoriesView.setVisibility(View.GONE);
            binding.emptyCategory.setVisibility(View.VISIBLE);
        } else {
            binding.categoriesView.setVisibility(View.VISIBLE);
            binding.emptyCategory.setVisibility(View.GONE);
        }

        if (todoListAdapter.getItemCount() == 0) {
            binding.todoListsView.setVisibility(View.GONE);
            binding.emptyTodo.setVisibility(View.VISIBLE);
        } else {
            binding.todoListsView.setVisibility(View.VISIBLE);
            binding.emptyTodo.setVisibility(View.GONE);
        }
    }

    private void displayDialogue() {
        FragmentManager fm = getSupportFragmentManager();
        ProfessionChooseFragment.newInstance().show(fm, null);
    }

    @Override
    public void fillDetails(Profession profession) {
        userAccount = new UserAccount(userAccount.getUsername(), userAccount.getGmailId(), userAccount.getPhno(), profession);
        dh.updateUser(userAccount);
        addSpecialCategoryButtons();
    }

    private void addSpecialCategoryButtons() {
        if(userAccount.getProfession() == Profession.nullProfession) {
            displayDialogue();
        }
        else {
            addSpecialCategory(R.string.groceries);
            if(!userAccount.getProfession().equals(Profession.jobSeeker)) {
                addSpecialCategory(R.string.home_maintenance);
            }
            if(userAccount.getProfession().equals(Profession.homeMaker)) {
                addSpecialCategory(R.string.kitchen_appliances);
            }
            categoryAdapter.notifyDataSetChanged();
        }
    }

    private void createUI() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        leftRL = (RelativeLayout) binding.whatYouWantInLeftDrawer;
        drawerLayout = (DrawerLayout) binding.drawerLayout;

        binding.menuButton.setOnClickListener((v) -> {
            drawerLayout.openDrawer(leftRL);
        });

        searchBar = binding.searchBar;
        searchBar.setSpeechMode(true);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(String.valueOf(charSequence).isEmpty()) {
                    binding.addCategory.setVisibility(View.VISIBLE);
                    binding.addTodoList.setVisibility(View.VISIBLE);
                }
                else {
                    binding.addCategory.setVisibility(View.GONE);
                    binding.addTodoList.setVisibility(View.GONE);
                }
                searchString = String.valueOf(charSequence);
                categoryAdapter.searchString(searchString);
                todoListAdapter.searchString(searchString);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        addSpecialCategoryButtons();

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
                uploadTodoList(todoList);
            }
        }
        else if (requestCode == CATEGORY_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                Category category = (Category) data.getExtras().get(CATEGORY_INTENT_KEY);
                setCategory(category);
                dh.insertCategory(category);
                uploadCategory(category);
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

    public int getSpecialCategoryIndex(String categoryName) {
        for(int i = 0; i < specialCategories.length; i++) {
            if(categoryName.equalsIgnoreCase(getString(SPECIAL_CATEGORIES_NAME_RESIDS[i]))) return i;
        }
        return -1;
    }

    private ArrayList<Category> downloadCategories() {
        ArrayList<Category> downloadedCategories = new ArrayList<Category>();
        // @vraj download all categories
        return downloadedCategories;
    }

    private void downloadTodoLists() {
        // @vraj fill up todoLists arraylist
    }

    private void uploadCategory(Category category) {
        // @vraj fill pls
    }

    private void uploadTodoList(TodoList todoList) {
        // @vraj upload a todoList
    }

    private void downloadAndSortCategories() {
        ArrayList<Category> downloadedCategories = downloadCategories();
        for(Category c : downloadedCategories) {
            String name = c.getCategoryName();
            int specialCategoryIndex = getSpecialCategoryIndex(name);
            if(specialCategoryIndex == -1) {
                categories.add(c);
            }
            else {
                specialCategories[specialCategoryIndex] = c;
            }
        }
    }

    private void clearDatabase() {
        ArrayList<Category> categories = new ArrayList<Category>(dh.getCategories());
        ArrayList<TodoList> todoLists = new ArrayList<TodoList>(dh.getTodoLists());
        for(Category c : categories) {
            dh.deleteCategory(c);
        }
        for(TodoList t : todoLists) {
            dh.deleteTodoList(t);
        }
    }

    private void writeToDatabase() {
        for (Category c : categories) {
            dh.insertCategory(c);
        }
        for(Category c : specialCategories) {
            if(c != null) dh.insertCategory(c);
        }
        for(TodoList t : todoLists) {
            dh.insertTodoList(t);
        }
    }

    private void addSpecialCategory(int categoryNameResId) {
        Category c = new Category(getString(categoryNameResId));
        if(isNewAccount) {
            uploadCategory(c);
        }
        specialCategories[getSpecialCategoryIndex(categoryNameResId)] = c;
        categories.add(c);
    }
}