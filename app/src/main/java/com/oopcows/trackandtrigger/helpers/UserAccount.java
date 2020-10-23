package com.oopcows.trackandtrigger.helpers;

import android.os.Parcel;
import android.os.Parcelable;

public class UserAccount implements Parcelable {

    private String username;
    private String password;
    private String gmailId;
    private String phno;

    public UserAccount(String username, String password, String gmailId, String phno) {
        this.username = username;
        this.password = password;
        this.gmailId = gmailId;
        this.phno = phno;
    }

    public UserAccount(Parcel in) {
        username = in.readString();
        password = in.readString();
        gmailId = in.readString();
        phno = in.readString();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getGmailId() {
        return gmailId;
    }

    public String getPhno() {
        return phno;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(password);
        parcel.writeString(gmailId);
        parcel.writeString(phno);
    }

    public static final Parcelable.Creator<UserAccount> CREATOR = new Parcelable.Creator<UserAccount>() {

        public UserAccount createFromParcel(Parcel in) {
            return new UserAccount(in);
        }

        public UserAccount[] newArray(int size) {
            return new UserAccount[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof UserAccount) {
            UserAccount toCompare = (UserAccount) obj;
            return (this.username.equalsIgnoreCase(toCompare.getUsername()));
        }

        return false;
    }

    @Override
    public int hashCode() {
        return (this.getUsername()).hashCode();
    }

}
