package com.oopcows.trackandtrigger.database;

import android.content.Context;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;

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

}
