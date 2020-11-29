
package com.oopcows.trackandtrigger.helpers;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.room.Ignore;

import com.oopcows.trackandtrigger.dashboard.DashboardActivity;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class Todo implements Parcelable {

    private String task;
    private boolean done;
    private int year, month, day, hour, minute;

    @Ignore
    private Intent intent;

    @Ignore
    private int eventId;

    @Ignore
    public Todo(String task, boolean done, int year, int month, int day, int hour, int minute, Intent intent, int eventId) {
        this.task = task;
        this.done = done;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.intent = intent;
        this.eventId = eventId;
    }

    public Todo(String task, boolean done, int year, int month, int day, int hour, int minute) {
        this.task = task;
        this.done = done;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        eventId = (int)(Math.random()*2000); // surely theyll never add so many tasks right?
    }

    public Todo(String task, boolean done, String s) {
        this.task = task;
        this.done = done;
        int[] arr = getTimeFromString(s);
        this.year = arr[0];
        this.month = arr[1];
        this.day = arr[2];
        this.hour = arr[3];
        this.minute = arr[4];
    }

    @Ignore
    public Todo(String task, boolean done, String str, Intent intent, int eventId) {
        this.task = task;
        this.done = done;
        int[] arr = getTimeFromString(str);
        this.year = arr[0];
        this.month = arr[1];
        this.day = arr[2];
        this.hour = arr[3];
        this.minute = arr[4];
        this.intent = intent;
        eventId = (int)(Math.random()*2000); // surely theyll never add so many tasks right?
    }

    protected Todo(Parcel in) {
        task = in.readString();
        done = in.readByte() != 0;
        year = in.readInt();
        month = in.readInt();
        day = in.readInt();
        hour = in.readInt();
        minute = in.readInt();
        eventId = in.readInt();
        intent = in.readParcelable(Intent.class.getClassLoader());
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

    public String getTimeString() { return year + " " + month + " " + day + " " + hour + " "  + minute; }


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
        parcel.writeInt(eventId);
        parcel.writeParcelable(intent, i);
    }

    public static int[] getTimeFromString(String time) {
        int[] arr = {0, 0, 0, 0, 0};
        System.out.println(time);
        String[] arr2 = time.trim().split(" ");
        for(int j = 0; j < arr2.length; j++) arr[j] = Integer.parseInt(arr2[j]);
        return arr;
    }

    public static int eventID = 0;
    private static final int PERMISSION_REQUEST_CODE = 1;

    public void scheduleNotif(DashboardActivity dashboardActivity, UserAccount userAccount) { ;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        System.out.println(cal.getTime());
        cal.add(Calendar.HOUR, -1);

        System.out.println(cal.getTime());

        if(intent != null)
        PendingIntent.getBroadcast(
                dashboardActivity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT).cancel();

        intent = new Intent(dashboardActivity, CustomAlarmReceiver.class);

        intent.putExtra("alarm_message", "Hello " + userAccount.getUsername() + ", you have one pending todo: " + task);
        intent.putExtra("number", userAccount.getPhno());

        PendingIntent pendingAlarm = PendingIntent.getBroadcast(
                dashboardActivity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) dashboardActivity.getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingAlarm);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (dashboardActivity.checkSelfPermission(Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.SEND_SMS};

                ActivityCompat.requestPermissions(dashboardActivity,new String[] { Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);

            }
        }
    }

    public Intent getIntent() { return intent; }

    public int getEventId() { return eventId; }
}