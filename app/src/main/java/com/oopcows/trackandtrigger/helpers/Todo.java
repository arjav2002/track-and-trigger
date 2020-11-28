
package com.oopcows.trackandtrigger.helpers;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.type.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Todo implements Parcelable {

    private String task;
    private boolean done;
    private int year, month, day, hour, minute;

    public Todo(String task, boolean done, int year, int month, int day, int hour, int minute) {
        this.task = task;
        this.done = done;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public Todo(String task, boolean done, String str) {
        this.task = task;
        this.done = done;
        int arr[] = {0, 0, 0, 0, 0};
        String[] arr2 = str.split(" ");
        for(int j = 0; j < arr2.length; j++) arr[j] = Integer.parseInt(arr2[j]);
        this.year = arr[0];
        this.month = arr[1];
        this.day = arr[2];
        this.hour = arr[3];
        this.minute = arr[4];
    }

    protected Todo(Parcel in) {
        task = in.readString();
        done = in.readByte() != 0;
        year = in.readInt();
        month = in.readInt();
        day = in.readInt();
        hour = in.readInt();
        minute = in.readInt();
    }

    public static final Creator<Todo> CREATOR = new Creator<Todo>() {
        @Override
        public Todo createFromParcel(Parcel in) {
            return new Todo(in);
        }

        @Override
        public Todo[] newArray(int size) {
            return new Todo[size];
        }
    };

    public String getTask() {
        return task;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getString() { return year + " " + month + " " + day + " " + hour + " "  + minute; }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(task);
        parcel.writeByte((byte) (done ? 1 : 0));
        parcel.writeInt(year);
        parcel.writeInt(month);
        parcel.writeInt(day);
        parcel.writeInt(hour);
        parcel.writeInt(minute);
    }
}