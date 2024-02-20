package com.jager.fasttask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.os.Bundle;
import com.jager.fasttask.TaskListContract.TaskEntry;

import android.util.Log;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TaskListDatabase dbHelper = TaskListDatabase.getManagementInstance(getApplicationContext());
        SQLiteDatabase myWritableDB = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_TASKNAME, "Clean Garbage");
        values.put(TaskEntry.COLUMN_NAME_DESCRIPTION, "Throw away trash from room, and wash carpeting.");
        long newRowId = myWritableDB.insert(TaskEntry.TABLE_NAME, null, values);
        SQLiteDatabase myReadableDB = dbHelper.getReadableDatabase();
        String[] titleAndDescription = { TaskEntry.COLUMN_NAME_TASKNAME, TaskEntry.COLUMN_NAME_DESCRIPTION };
        String[] args = {String.valueOf(newRowId)};
        Cursor taskInfo = myReadableDB.query(TaskEntry.TABLE_NAME, titleAndDescription, TaskEntry._ID+" = ?", args, null, null, null);
        taskInfo.moveToNext();
        int nameColumn = taskInfo.getColumnIndex(TaskEntry.COLUMN_NAME_TASKNAME);
        int description = taskInfo.getColumnIndex(TaskEntry.COLUMN_NAME_DESCRIPTION);
        Log.d("Name: ", taskInfo.getString(nameColumn));
        Log.d("Description: ", taskInfo.getString(description));
        taskInfo.close();
        setContentView(R.layout.activity_main);
    }
}