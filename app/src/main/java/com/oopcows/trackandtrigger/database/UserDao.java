package com.oopcows.trackandtrigger.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.oopcows.trackandtrigger.helpers.UserAccount;

import java.util.List;

import static com.oopcows.trackandtrigger.helpers.CowConstants.USERNAME_COLUMN_NAME;
import static com.oopcows.trackandtrigger.helpers.CowConstants.USERS_TABLE_NAME;

@Dao
public interface UserDao {

    @Query("SELECT * from " + USERS_TABLE_NAME)
    List<UserAccount> getUserList();

    @Query("SELECT * FROM " + USERS_TABLE_NAME + " WHERE " + USERNAME_COLUMN_NAME + " = :username limit 1")
    UserAccount getUser(String username);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserAccount uc);

    @Update
    void updateUser(UserAccount uc);

    @Delete
    void deleteUser(UserAccount uc);
}
