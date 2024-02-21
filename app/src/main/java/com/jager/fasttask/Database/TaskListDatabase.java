package com.jager.fasttask.Database;

import android.content.ContentValues;
import android.content.Context;

import com.jager.fasttask.Task;
import com.jager.fasttask.Database.TaskListContract.TaskEntry;
import com.jager.fasttask.Database.CategoryListContract.CategoryEntry;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        taskValues.put(TaskEntry.COLUMN_NAME_CREATION, task.getCreationDate().getTime());
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
        db.delete(TaskEntry.TABLE_NAME, TaskEntry._ID+"=?", new String[]{String.valueOf(task.getId())});
    }

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            Cursor allTasks = db.query(TaskEntry.TABLE_NAME, null, null, null, null, null, null);
            if (allTasks != null) {
                if (allTasks.moveToFirst()) {
                    do {
                        Cursor categoryCursor = db.query(CategoryEntry.TABLE_NAME, null, CategoryEntry._ID + "=?", new String[]{String.valueOf(allTasks.getString(2))}, null, null, null);
                        categoryCursor.moveToFirst();
                        Task newTask = new Task(allTasks.getString(3), allTasks.getString(4), new Date(Long.parseLong(allTasks.getString(7))), categoryCursor.getString(1));
                        newTask.setId(allTasks.getInt(1));
                        taskList.add(newTask);
                    } while (allTasks.moveToNext());
                }
            }
        }finally {
            db.endTransaction();
        }
        return taskList;
    }

    public void updateTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updatedTaskValues = new ContentValues();
        updatedTaskValues.put(TaskEntry.COLUMN_NAME_TASKNAME, task.getTaskName());
        updatedTaskValues.put(TaskEntry.COLUMN_NAME_DESCRIPTION, task.getTaskDescription());
        updatedTaskValues.put(TaskEntry.COLUMN_NAME_COLOR, task.getColor());
        updatedTaskValues.put(TaskEntry.COLUMN_NAME_CREATION, task.getCreationDate().getTime());
        db.update(TaskEntry.TABLE_NAME, updatedTaskValues, TaskEntry._ID+"=?", new String[]{String.valueOf(task.getId())});
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
