package com.shollmann.weathy.db;

import android.content.Context;

public class DbHelper extends DbHelperTemplate {
    private final static int DB_VERSION = 1;
    private final static String DB_NAME = "weathyMsgDatabase";
    private final static String TABLE_NAME = "objects";
    private final static String COLUMN_KEY = "objectKey";
    private final static String COLUMN_DATA = "objectData";
    private final static String COLUMN_DATE = "objectDate";
    private final static boolean AUTO_PURGE = false;

    public DbHelper(Context context) {
        super(context, DB_NAME, DB_VERSION, TABLE_NAME, COLUMN_KEY, COLUMN_DATA, COLUMN_DATE, AUTO_PURGE);
    }
}