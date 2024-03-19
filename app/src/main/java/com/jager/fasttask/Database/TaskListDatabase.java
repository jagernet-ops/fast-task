package com.jager.fasttask.Database;

import android.content.ContentValues;
import android.content.Context;

import com.jager.fasttask.Task;
import com.jager.fasttask.Database.TaskListContract.TaskEntry;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskListDatabase extends SQLiteOpenHelper {
    private static final String SQL_CREATE_TASK_ENTRIES =
            "CREATE TABLE " + TaskEntry.TABLE_NAME + " (" +
                    TaskEntry._ID + " INTEGER PRIMARY KEY," +
                    TaskEntry.COLUMN_NAME_CATEGORY + " TEXT," +
                    TaskEntry.COLUMN_NAME_TASK_NAME + " TEXT, " +
                    TaskEntry.COLUMN_NAME_DESCRIPTION + " TEXT, " +
                    TaskEntry.COLUMN_NAME_COLOR + " TEXT, " +
                    TaskEntry.COLUMN_NAME_EXPIRATION + " INTEGER, " +
                    TaskEntry.COLUMN_NAME_CREATION + " INTEGER, " +
                    TaskEntry.COLUMN_NAME_COMPLETION + " INTEGER );";
    private static final String SQL_DELETE_TASK_ENTRIES =
            "DROP TABLE IF EXISTS " + TaskEntry.TABLE_NAME;

    private static String DATABASE_NAME = "TaskList.db";
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

    public static void setDATABASE_NAME(String dATABASE_NAME) {
      DATABASE_NAME = dATABASE_NAME;
    }

    public void insertTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues taskValues = new ContentValues();
        taskValues.put(TaskEntry.COLUMN_NAME_TASK_NAME, task.getTaskName());
        taskValues.put(TaskEntry.COLUMN_NAME_DESCRIPTION, task.getTaskDescription());
        taskValues.put(TaskEntry.COLUMN_NAME_COLOR, task.getColor());
        taskValues.put(TaskEntry.COLUMN_NAME_CREATION, String.valueOf(task.getCreationDate().getTime()));
        if(task.getExpirationDate() != null){
            taskValues.put(TaskEntry.COLUMN_NAME_EXPIRATION, task.getExpirationDate().getTime());
        }
        if(task.getCategory() != null){
            taskValues.put(TaskEntry.COLUMN_NAME_CATEGORY, task.getCategory());
        }
        taskValues.put(TaskEntry.COLUMN_NAME_COMPLETION, 0);
        db.insert(TaskEntry.TABLE_NAME, null, taskValues);
    }

    public void deleteTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TaskEntry.TABLE_NAME, TaskEntry._ID+"=?", new String[]{String.valueOf(task.getId())});
    }

    public void deleteTask(int taskID){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TaskEntry.TABLE_NAME, TaskEntry._ID+"=?", new String[]{String.valueOf(taskID)});
    }

    public void markExpiredTasksComplete(){
        List<Task> allTasks = getAllTasks();
        for(Task task : allTasks){
            if(task.getExpirationDate() != null && task.hasExpired()){
                task.setComplete(true);
                updateTask(task);
            }
        }
    }

    public List<Task> getTaskByExpiry(){
        long filterTimestamp = new Date().getTime();
        List<Task> sortedTaskList = getAllTasks();
        List<Task> completedTasks = new ArrayList<>();
        if(sortedTaskList.size() > 0) {
            for (int i = 0; i < sortedTaskList.size(); i++) {
                if (sortedTaskList.get(i).getIsComplete()) {
                    completedTasks.add(sortedTaskList.get(i));
                    sortedTaskList.remove(i);
                    i--;
                    continue;
                }
                if (sortedTaskList.get(i).getExpirationDate() != null && sortedTaskList.get(i).getExpirationDate().getTime() > filterTimestamp) {
                    Task currentTask = sortedTaskList.get(i);
                    int j = i - 1;
                    while (j >= 0 && sortedTaskList.get(j).getExpirationDate().getTime() > currentTask.getExpirationDate().getTime()) {
                        sortedTaskList.set(j + 1, sortedTaskList.get(j));
                        j--;
                    }
                    sortedTaskList.set(j + 1, currentTask);
                }
            }
        }
        if(completedTasks.size() > 0){
            sortedTaskList.addAll(completedTasks);
        }
        return sortedTaskList;
    }

    public List<Task> getTaskFromFilter(String columnName, String key){
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            Cursor allTasks = db.query(TaskEntry.TABLE_NAME, null, columnName+"=?",new String[]{key}, null, null, null);
            if (allTasks != null) {
                if(allTasks.moveToFirst()) {
                    do {
                        Task newTask = new Task(allTasks.getString(2), allTasks.getString(3), new Date(Long.parseLong(allTasks.getString(6))), allTasks.getString(1));
                        newTask.setColor(allTasks.getString(4));
                        newTask.setId(allTasks.getInt(0));
                        newTask.setComplete(allTasks.getInt(7) != 0);
                        if (allTasks.getLong(5) != 0) {
                            newTask.setExpirationDate(new Date(allTasks.getLong(5)));
                        }
                        taskList.add(newTask);
                    } while (allTasks.moveToNext());
                    allTasks.close();
                }
            }
        }finally {
            db.endTransaction();
        }
        if(taskList.size() > 0){
            for(Task task : taskList){
                Log.d("Task", task.getTaskName());
            }
        }
        return taskList;
    }

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            Cursor allTasks = db.query(TaskEntry.TABLE_NAME, null, null, null, null, null, null);
            if (allTasks != null && allTasks.getCount() > 0) {
                if (allTasks.moveToFirst()) {
                    do {
                        Task newTask = new Task(allTasks.getString(2), allTasks.getString(3), new Date(Long.parseLong(allTasks.getString(6))), allTasks.getString(1));
                        newTask.setColor(allTasks.getString(4));
                        newTask.setId(allTasks.getInt(0));
                        newTask.setComplete(allTasks.getInt(7) != 0);
                        if(allTasks.getLong(5) != 0){
                            newTask.setExpirationDate(new Date(allTasks.getLong(5)));
                        }
                        taskList.add(newTask);
                    } while (allTasks.moveToNext());
                    allTasks.close();
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
        updatedTaskValues.put(TaskEntry.COLUMN_NAME_TASK_NAME, task.getTaskName());
        updatedTaskValues.put(TaskEntry.COLUMN_NAME_DESCRIPTION, task.getTaskDescription());
        updatedTaskValues.put(TaskEntry.COLUMN_NAME_COLOR, task.getColor());
        updatedTaskValues.put(TaskEntry.COLUMN_NAME_CREATION, task.getCreationDate().getTime());
        updatedTaskValues.put(TaskEntry.COLUMN_NAME_CATEGORY, task.getCategory());
        if(task.getExpirationDate() != null){
            updatedTaskValues.put(TaskEntry.COLUMN_NAME_EXPIRATION, task.getExpirationDate().getTime());
            if(task.getExpirationDate().getTime() > new Date().getTime()){
                task.setComplete(false);
            }
        }
        updatedTaskValues.put(TaskEntry.COLUMN_NAME_COMPLETION, task.getIsComplete() ? 1 : 0 );
        db.update(TaskEntry.TABLE_NAME, updatedTaskValues, TaskEntry._ID+"=?", new String[]{String.valueOf(task.getId())});
    }

    public Cursor getDBCursor(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(TaskEntry.TABLE_NAME, null, null, null, null, null, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_TASK_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(SQL_DELETE_TASK_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }

}
