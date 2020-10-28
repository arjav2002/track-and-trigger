package com.oopcows.trackandtrigger.database;

import androidx.room.TypeConverter;

import com.oopcows.trackandtrigger.helpers.Profession;

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
}
