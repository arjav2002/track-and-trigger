
package com.oopcows.trackandtrigger.helpers;

import android.os.Parcel;
import android.os.Parcelable;

public class Todo implements Parcelable {

    private String task;
    private boolean done;

    public Todo(String task, boolean done) {
        this.task = task;
        this.done = done;
    }

    protected Todo(Parcel in) {
        task = in.readString();
        done = in.readByte() != 0;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(task);
        parcel.writeByte((byte) (done ? 1 : 0));
    }
}