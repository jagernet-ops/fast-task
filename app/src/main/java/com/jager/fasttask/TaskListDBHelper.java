package com.jager.fasttask;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class TaskListDBHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TaskListContract.TaskEntry.TABLE_NAME + " (" +
                    TaskListContract.TaskEntry._ID + " INTEGER PRIMARY KEY," +
                    TaskListContract.TaskEntry.COLUMN_NAME_CATEGORY_ID + " INTEGER," +
                    TaskListContract.TaskEntry.COLUMN_NAME_TASKNAME + " TEXT, " +
                    TaskListContract.TaskEntry.COLUMN_NAME_DESCRIPTION + " TEXT, " +
                    TaskListContract.TaskEntry.COLUMN_NAME_COLOR + " TEXT, " +
                    TaskListContract.TaskEntry.COLUMN_NAME_EXPIRATION + " TEXT, " +
                    TaskListContract.TaskEntry.COLUMN_NAME_CREATION + " TEXT, " +
                    TaskListContract.TaskEntry.COLUMN_NAME_COMPLETION + " TEXT );";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TaskListContract.TaskEntry.TABLE_NAME;
    private static final String DATABASE_NAME = "TaskList.db";
    private static final int DATABASE_VERSION = 1;
    private static TaskListDBHelper managementInstance = null;

    private TaskListDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static TaskListDBHelper getManagementInstance(Context context){
        if(managementInstance == null){
           managementInstance = new TaskListDBHelper(context);
        }
        return managementInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }

}
