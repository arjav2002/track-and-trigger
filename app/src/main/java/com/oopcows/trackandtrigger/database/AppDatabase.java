package com.oopcows.trackandtrigger.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.oopcows.trackandtrigger.helpers.TodoList;
import com.oopcows.trackandtrigger.helpers.UserAccount;

import static com.oopcows.trackandtrigger.helpers.CowConstants.DATABASE_NAME;

@Database(version=1, exportSchema = false, entities={UserAccount.class, TodoList.class})
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase db = null; // singleton instance

    public abstract UserDao getUserDao();
    public abstract TodoListDao getTodoListDao();

    protected AppDatabase() {
    }

    public static synchronized AppDatabase getInstance(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
        }

        return db;
    }
}
