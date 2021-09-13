package com.example.login.Data;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;

public class PostingData {
    public static final String PREFERENCES_NAME = "posting db";

    private static final String DEFAULT_VALUE_STRING = "";
    private static final boolean DEFAULT_VALUE_BOOLEAN = false;
    private static final int DEFAULT_VALUE_INT = -1;

    private static SharedPreferences getPreferences(Context context)
    {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }
    public static void setArray(Context context, JSONArray jsondata)
    {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("PostingData",jsondata.toString());
        editor.commit();
    }
    public static String getArray(Context context)
    {
        SharedPreferences prefs = getPreferences(context);
        String value = prefs.getString("PostingData","empty");
        return value;
    }

}
