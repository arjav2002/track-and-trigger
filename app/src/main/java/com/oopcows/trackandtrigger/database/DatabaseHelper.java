package com.oopcows.trackandtrigger.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.oopcows.trackandtrigger.helpers.CowConstants.MAX_TASKS;

public class DatabaseHelper implements Runnable {

    private volatile BlockingQueue<Runnable> tasks;
    private volatile boolean running;
    private Thread thread;

    public DatabaseHelper() {
        running = false;
        tasks = new LinkedBlockingQueue<Runnable>(MAX_TASKS);
        thread = new Thread(this);
    }

    public synchronized void start() {
        running = true;
        thread.start();
    }

    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(running) {
            Runnable toRun;
            while((toRun = tasks.poll()) != null) {
                toRun.run();
            }
        }
    }

    public void add(Runnable r) {
        tasks.add(r);
    }

}
