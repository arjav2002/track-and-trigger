package com.oopcows.trackandtrigger.helpers;

import com.oopcows.trackandtrigger.R;

public class CowConstants {

    public static final String USER_ACCOUNT_INTENT_KEY = UserAccount.class.getCanonicalName();
    public static final String TODO_LIST_INTENT_KEY = TodoList.class.getCanonicalName();
    public static final String CATEGORY_INTENT_KEY = Category.class.getCanonicalName();
    public static final String IS_NEW_ACCOUNT_INTENT_KEY = "isNewAccount";
    public static final int[] SPECIAL_CATEGORIES_NAME_RESIDS = {R.string.groceries, R.string.home_maintenance, R.string.kitchen_appliances};
    public static final int[] CATEGORY_DRAWABLE_RESIDS = {R.drawable.ic_grocery, R.drawable.ic_maintenance, R.drawable.ic_kitchen};
    public static final int G_SIGN_IN = 1;
    public static final int TIME_TAKEN_TO_RESEND_OTP = 60;
    public static final int OTP_LENGTH = 6;
    public static final String COUNTRY_CODE = "+91";
    public static final String DATABASE_NAME = "appdb.db";
    public static final String USERS_TABLE_NAME = "users";
    public static final String TODO_LISTS_TABLE_NAME = "todolists";
    public static final String TODO_LIST_HEADING_NAME = "heading";
    public static final String TODO_LIST_TODOS_NAME = "todos";
    public static final String CATEGORIES_TABLE_NAME = "categories";
    public static final String CATEGORY_NAME_COLUMN = "categoryname";
    public static final String CATEGORY_DATA_COLUMN = "categorydata";
    public static final int MAX_TASKS = 10;
    public static final String USERNAME_COLUMN_NAME = "username";
    public static final String GMAILID_COLUMN_NAME = "gmail_id";
    public static final String PHNO_COLUMN_NAME = "phno";
    public static final String PROF_COLUMN_NAME = "profession";
    public static final String OTP = "OTP";
    public static final String MAIL = "Mail";
    public static final String PAST_USERS = "PastUsers";
    public static final String USER_NAMES = "UserNames";

    public static final int TODO_VIEW_HOLDER = 0;
    public static final int ADD_VIEW_HOLDER = 1;
    public static final int TODO_LIST_REQUEST_CODE = 1;
    public static final int CATEGORY_REQUEST_CODE = 2;
    public static final int TAKE_PHOTO_REQUEST_CODE = 1;
    public static final int CHOOSE_PICTURE_REQUEST_CODE = 2;
    public static final int NORMAL_VIEW_HOLDER = 1;
    public static final int SEARCH_RESULT_VIEW_HOLDER = 2;
}
