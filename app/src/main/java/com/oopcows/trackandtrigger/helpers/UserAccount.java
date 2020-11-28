package com.oopcows.trackandtrigger.helpers;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import org.jetbrains.annotations.NotNull;

import static com.oopcows.trackandtrigger.helpers.CowConstants.GMAILID_COLUMN_NAME;
import static com.oopcows.trackandtrigger.helpers.CowConstants.PHNO_COLUMN_NAME;
import static com.oopcows.trackandtrigger.helpers.CowConstants.PROF_COLUMN_NAME;
import static com.oopcows.trackandtrigger.helpers.CowConstants.USERNAME_COLUMN_NAME;
import static com.oopcows.trackandtrigger.helpers.CowConstants.USERS_TABLE_NAME;

@Entity(tableName = USERS_TABLE_NAME)
public class UserAccount implements Parcelable {

    @ColumnInfo(name = USERNAME_COLUMN_NAME)
    @PrimaryKey
    @NonNull
    private String username;

    @ColumnInfo(name = GMAILID_COLUMN_NAME)
    @NonNull
    private String gmailId;

    @ColumnInfo(name = PHNO_COLUMN_NAME)
    @NonNull
    private String phno;

    @ColumnInfo(name = PROF_COLUMN_NAME)
    @NonNull
    private Profession profession;

    public UserAccount(String username, String gmailId, String phno, Profession profession) {
        this.username = username;
        this.gmailId = gmailId;
        this.phno = phno;
        this.profession = profession;
    }

    @Ignore
    public UserAccount(Parcel in) {
        username = in.readString();
        gmailId = in.readString();
        phno = in.readString();
        profession = Profession.valueOf(in.readString());
    }

    @NotNull
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