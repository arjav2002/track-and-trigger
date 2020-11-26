package com.oopcows.trackandtrigger.database;

import androidx.room.TypeConverter;

import com.oopcows.trackandtrigger.helpers.Profession;
import com.oopcows.trackandtrigger.helpers.Todo;

import java.util.LinkedList;

import io.perfmark.Link;

public class Converters {

    @TypeConverter
    public static String fromProfession(Profession profession) {
        return String.valueOf(profession);
    }

    @TypeConverter
    public static Profession fromString(String str) {
        if(str.equals("")) return null;
        return Profession.valueOf(str);
    }

    @TypeConverter
    public static String fromTodos(LinkedList<Todo> todos) {
        String result="";
        for(Todo todo : todos) {
            result += todo.getTask() + (todo.isDone()? '\1' : '\0');
        }
        return result;
    }

    @TypeConverter
    public static LinkedList<Todo> fromDelimitedString(String delimitedString) {
        LinkedList<Todo> todos = new LinkedList<Todo>();
        String task = "";
        for(int i = 0; i < delimitedString.length(); i++) {
            char ch = delimitedString.charAt(i);
            if(ch == '\1') {
                todos.add(new Todo(task, true));
                task = "";
            }
            else if(ch == '\0') {
                todos.add(new Todo(task, false));
                task = "";
            }
            else {
                task += ch;
            }
        }
        return todos;
    }
}
