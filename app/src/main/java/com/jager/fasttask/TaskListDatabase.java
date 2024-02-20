package com.jager.fasttask;

import android.content.ContentValues;
import android.content.Context;
import com.jager.fasttask.TaskListContract.TaskEntry;
import com.jager.fasttask.CategoryListContract.CategoryEntry;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class TaskListDatabase extends SQLiteOpenHelper {
    private static final String SQL_CREATE_TASK_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + TaskEntry.TABLE_NAME + " (" +
                    TaskEntry._ID + " INTEGER PRIMARY KEY," +
                    TaskEntry.COLUMN_NAME_CATEGORY_ID + " INTEGER," +
                    TaskEntry.COLUMN_NAME_TASKNAME + " TEXT, " +
                    TaskEntry.COLUMN_NAME_DESCRIPTION + " TEXT, " +
                    TaskEntry.COLUMN_NAME_COLOR + " TEXT, " +
                    TaskEntry.COLUMN_NAME_EXPIRATION + " TEXT, " +
                    TaskEntry.COLUMN_NAME_CREATION + " TEXT, " +
                    TaskEntry.COLUMN_NAME_COMPLETION + " TEXT );";
    private static final String SQL_CREATE_CATEGORIES_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + CategoryEntry.TABLE_NAME + " (" +
                    CategoryEntry._ID + "INTEGER PRIMARY KEY," +
                    CategoryEntry.COLUMN_NAME_CATEGORY_NAME + "TEXT );";
    private static final String SQL_DELETE_TASK_ENTRIES =
            "DROP TABLE IF EXISTS " + TaskEntry.TABLE_NAME;

    private static final String SQL_DELETE_CATEGORY_ENTRIES =
            "DROP TABLE IF EXISTS " + CategoryEntry.TABLE_NAME;
    private static final String DATABASE_NAME = "TaskList.db";
    private static final int DATABASE_VERSION = 1;
    private static TaskListDatabase managementInstance = null;

    private TaskListDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static TaskListDatabase getManagementInstance(Context context){
        if(managementInstance == null){
           managementInstance = new TaskListDatabase(context);
        }
        return managementInstance;
    }

    public void insertTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues taskValues = new ContentValues();
        taskValues.put(TaskEntry.COLUMN_NAME_TASKNAME, task.getTaskName());
        taskValues.put(TaskEntry.COLUMN_NAME_DESCRIPTION, task.getTaskDescription());
        taskValues.put(TaskEntry.COLUMN_NAME_COLOR, task.getColor());
        if(task.getExpirationDate() != null){
            taskValues.put(TaskEntry.COLUMN_NAME_EXPIRATION, task.getExpirationDate().getTime());
        }
        db.insert(TaskEntry.TABLE_NAME, null, taskValues);
        ContentValues categoryValues = new ContentValues();
        categoryValues.put(CategoryEntry.COLUMN_NAME_CATEGORY_NAME, task.getCategory());
        db.insert(CategoryEntry.TABLE_NAME, null, categoryValues);
    }

    public void deleteTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TaskEntry.TABLE_NAME, TaskEntry.COLUMN_NAME_CREATION+"=?", new String[]{String.valueOf(task.getCreationDate().getTime())});
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_TASK_ENTRIES);
        db.execSQL(SQL_CREATE_CATEGORIES_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(SQL_DELETE_TASK_ENTRIES);
        db.execSQL(SQL_DELETE_CATEGORY_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }

}
