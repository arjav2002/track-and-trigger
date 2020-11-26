package com.oopcows.trackandtrigger.helpers;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.LinkedList;

import static com.oopcows.trackandtrigger.helpers.CowConstants.TODO_LISTS_TABLE_NAME;
import static com.oopcows.trackandtrigger.helpers.CowConstants.TODO_LIST_HEADING_NAME;
import static com.oopcows.trackandtrigger.helpers.CowConstants.TODO_LIST_TODOS_NAME;

@Entity(tableName = TODO_LISTS_TABLE_NAME)
public class TodoList implements Parcelable {

    @ColumnInfo(name = TODO_LIST_HEADING_NAME)
    @PrimaryKey
    @NonNull
    private String heading;

    @ColumnInfo(name = TODO_LIST_TODOS_NAME)
    @NonNull
    private LinkedList<Todo> todos;

    @Ignore
    public TodoList() {
        todos = new LinkedList<Todo>();
    }

    public TodoList(String heading, LinkedList<Todo> todos) {
        this.heading = heading;
        this.todos = todos;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public Todo getTodo(int i) {
        return todos.get(i);
    }

    public LinkedList<Todo> getTodos() {
        return todos;
    }

    public void addTodo(Todo todo) {
        todos.add(todo);
    }

    public void removeTodo(Todo todo) {
        todos.remove(todo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(heading);
        Todo[] todos = new Todo[this.todos.size()];
        for(int j = 0; j < todos.length; j++) {
            todos[j] = this.todos.get(j);
        }
        parcel.writeArray(todos);
    }

    public static final Parcelable.Creator<TodoList> CREATOR = new Parcelable.Creator<TodoList>() {

        public TodoList createFromParcel(Parcel in) {
            TodoList tl = new TodoList();
            tl.setHeading(in.readString());
            Object[] inTodos = in.readArray(Todo.class.getClassLoader());
            for(int i = 0; i < inTodos.length; i++) {
                tl.addTodo((Todo) inTodos[i]);
            }
            return tl;
        }

        public TodoList[] newArray(int size) {
            return new TodoList[size];
        }
    };
}
