package com.oopcows.trackandtrigger.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.oopcows.trackandtrigger.helpers.TodoList;
import com.oopcows.trackandtrigger.helpers.UserAccount;

import java.util.List;

import static com.oopcows.trackandtrigger.helpers.CowConstants.TODO_LISTS_TABLE_NAME;
import static com.oopcows.trackandtrigger.helpers.CowConstants.TODO_LIST_HEADING_NAME;

@Dao
public interface TodoListDao {

    @Query("SELECT * from " + TODO_LISTS_TABLE_NAME)
    List<TodoList> getTodoLists();

    @Query("SELECT * FROM " + TODO_LISTS_TABLE_NAME + " WHERE " + TODO_LIST_HEADING_NAME + " = :heading limit 1")
    TodoList getTodoList(String heading);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTodoList(TodoList todoList);

    @Update
    void updateTodoList(TodoList todoList);

    @Delete
    void deleteTodoList(TodoList todoList);

}
