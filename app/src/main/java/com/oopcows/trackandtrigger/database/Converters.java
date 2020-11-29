package com.oopcows.trackandtrigger.database;

import androidx.room.TypeConverter;

import com.oopcows.trackandtrigger.helpers.CategoryItem;
import com.oopcows.trackandtrigger.helpers.Profession;
import com.oopcows.trackandtrigger.helpers.Todo;

import java.util.ArrayList;

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
    public static String fromTodos(ArrayList<Todo> todos) {
        StringBuilder result= new StringBuilder();
        for(Todo todo : todos) {
            result.append(todo.getTask()).append(todo).append('\1').append(todo.isDone() ? '\1' : '\2').append(todo.getTimeString()).append('\3');
        }
        return result.toString();
    }

    @TypeConverter
    public static ArrayList<Todo> fromDelimitedString(String delimitedString) {
        ArrayList<Todo> todos = new ArrayList<Todo>();
        StringBuilder task = new StringBuilder();
        boolean isDone = false;
        StringBuilder dateTime = new StringBuilder();
        boolean readingDatetime = false;
        for(int i = 0; i < delimitedString.length(); i++) {
            char ch = delimitedString.charAt(i);
            if(ch == '\1') {
                readingDatetime = true;
            }
            else if(ch == '\2') {
                isDone = true;
                readingDatetime = true;
            }
            else if(ch == '\3') {
                todos.add(new Todo(task.toString(), isDone, dateTime.toString()));
                task = new StringBuilder();
                readingDatetime = false;
            }
            else {
                if(readingDatetime) {
                    dateTime.append(ch);
                }
                else {
                    task.append(ch);
                }
            }
        }
        return todos;
    }

    @TypeConverter
    public static String getData(ArrayList<CategoryItem> items) {
        StringBuilder data = new StringBuilder();
        for(CategoryItem item : items) {
            data.append(item.getItemName()).append('\1').append(item.getImgPath()).append('\1').append(item.getQuantity()).append('\1').append(item.getDownPath()).append('\1');
        }
        return data.toString();
    }

    @TypeConverter
    public static ArrayList<CategoryItem> getFromData(String data) {
        ArrayList<CategoryItem> items = new ArrayList<CategoryItem>();
        int delimiters = 0;
        StringBuilder itemName = new StringBuilder();
        StringBuilder imgPath = new StringBuilder();
        StringBuilder quantity = new StringBuilder();
        StringBuilder downPath = new StringBuilder();
        for(int i = 0; i < data.length(); i++) {
            char ch = data.charAt(i);
            if(ch == '\1') delimiters++;
            else if(delimiters == 0) {
                itemName.append(ch);
            }
            else if(delimiters == 1) {
                imgPath.append(ch);
            }
            else if(delimiters == 2) {
                quantity.append(ch);
            }
            else if(delimiters == 3) {
                downPath.append(ch);
            }
            else if(delimiters==4) {
                items.add(new CategoryItem(itemName.toString(), imgPath.toString(), Integer.parseInt(quantity.toString()), downPath.toString()));
                itemName = new StringBuilder();
                imgPath = new StringBuilder();
                quantity = new StringBuilder();
                delimiters = 0;
            }
        }
        return items;
    }
}
