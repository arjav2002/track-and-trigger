package com.oopcows.trackandtrigger.database;

import android.content.Context;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.room.Room;

import com.oopcows.trackandtrigger.helpers.Category;
import com.oopcows.trackandtrigger.helpers.Todo;
import com.oopcows.trackandtrigger.helpers.TodoList;
import com.oopcows.trackandtrigger.helpers.UserAccount;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.oopcows.trackandtrigger.helpers.CowConstants.MAX_TASKS;

public class DatabaseHelper implements Runnable {

    private volatile Runnable task;
    private volatile boolean running;
    private final Thread thread;
    private final UserDao userDao;
    private final TodoListDao todoListDao;
    private final CategoryDao categoryDao;
    private volatile Object result;
    private static DatabaseHelper instance;

    public synchronized static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        running = false;
        thread = new Thread(this);
        userDao = AppDatabase.getInstance(context).getUserDao();
        todoListDao = AppDatabase.getInstance(context).getTodoListDao();
        categoryDao = AppDatabase.getInstance(context).getCategoryDao();
    }

    private void start() {
        running = true;
        thread.start();
    }

    @Override
    public void run() {
        while(running) {
            if(task != null) {
                System.out.println("Task starting");
                task.run();
                System.out.println("Task completed");
                task = null;
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public synchronized Category getCategory(final String categoryName) {
        if(!running) start();
        task = new Runnable() {
            @Override
            public void run() {
                result = categoryDao.getCategory(categoryName);
            }
        };
        while(task != null);
        return (Category) result;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public synchronized void insertCategory(Category category) {
        if(!running) start();
        task = new Runnable() {
            @Override
            public void run() {
                categoryDao.insertCategory(category);
            }
        };
        while(task != null);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public synchronized void updateCategory(Category category) {
        if(!running) start();
        task = new Runnable() {
            @Override
            public void run() {
                categoryDao.updateCategory(category);
            }
        };
        while(task != null);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public synchronized void deleteCategory(Category category) {
        if(!running) start();
        task = new Runnable() {
            @Override
            public void run() {
                categoryDao.deleteCategory(category);
            }
        };
        while(task != null);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public synchronized List<Category> getCategories()  {
        if(!running) start();
        task = new Runnable() {
            @Override
            public void run() {
                result = todoListDao.getTodoLists();
            }
        };
        while(task != null);
        return (List<Category>) result;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public synchronized TodoList getTodoList(final String heading) {
        if(!running) start();
        task = new Runnable() {
            @Override
            public void run() {
                result = todoListDao.getTodoList(heading);
            }
        };
        while(task != null);
        return (TodoList) result;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public synchronized void insertTodoList(TodoList todoList) {
        if(!running) start();
        task = new Runnable() {
            @Override
            public void run() {
                todoListDao.insertTodoList(todoList);
            }
        };
        while(task != null);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public synchronized void updateTodoList(TodoList todoList) {
        if(!running) start();
        task = new Runnable() {
            @Override
            public void run() {
                todoListDao.updateTodoList(todoList);
            }
        };
        while(task != null);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public synchronized void deleteUser(TodoList todoList) {
        if(!running) start();
        task = new Runnable() {
            @Override
            public void run() {
                todoListDao.deleteTodoList(todoList);
            }
        };
        while(task != null);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public synchronized List<TodoList> getTodoLists()  {
        if(!running) start();
        task = new Runnable() {
            @Override
            public void run() {
                result = todoListDao.getTodoLists();
            }
        };
        while(task != null);
        return (List<TodoList>) result;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public synchronized UserAccount getUser(final String username) {
        if(!running) start();
        task = new Runnable() {
            @Override
            public void run() {
                result = userDao.getUser(username);
            }
        };
        while(task != null);
        return (UserAccount) result;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public synchronized void insertUser(UserAccount uc) {
        if(!running) start();
        task = new Runnable() {
            @Override
            public void run() {
                userDao.insertUser(uc);
            }
        };
        while(task != null);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public synchronized void updateUser(UserAccount uc) {
        if(!running) start();
        task = new Runnable() {
            @Override
            public void run() {
                userDao.updateUser(uc);
            }
        };
        while(task != null);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public synchronized void deleteUser(UserAccount uc) {
        if(!running) start();
        task = new Runnable() {
            @Override
            public void run() {
                userDao.deleteUser(uc);
            }
        };
        while(task != null);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public synchronized List<UserAccount> getUserList()  {
        if(!running) start();
        task = new Runnable() {
            @Override
            public void run() {
                result = userDao.getUserList();
            }
        };
        while(task != null);
        return (List<UserAccount>) result;
    }

}
