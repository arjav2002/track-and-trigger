package com.oopcows.trackandtrigger.helpers;

import android.os.Parcel;
import android.os.Parcelable;

public class UserAccount implements Parcelable {

    private String username;
    private String gmailId;
    private String phno;
    private Profession profession;

    public UserAccount(String username, String gmailId, String phno, Profession profession) {
        this.username = username;
        this.gmailId = gmailId;
        this.phno = phno;
        this.profession = profession;
    }

    public UserAccount(Parcel in) {
        username = in.readString();
        gmailId = in.readString();
        phno = in.readString();
        profession = Profession.valueOf(in.readString());
    }

    public String getUsername() {
        return username;
    }

    public String getGmailId() {
        return gmailId;
    }

    public String getPhno() {
        return phno;
    }

    public Profession getProfession() { return profession; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(gmailId);
        parcel.writeString(phno);
        parcel.writeString(profession.name());
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
