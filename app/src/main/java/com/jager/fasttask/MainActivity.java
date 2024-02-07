package com.jager.fasttask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TaskListDBHelper dbHelper = TaskListDBHelper.getManagementInstance(getApplicationContext());
        SQLiteDatabase myDB = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskListContract.TaskEntry.COLUMN_NAME_TASKNAME, "Clean Garbage");
        values.put(TaskListContract.TaskEntry.COLUMN_NAME_DESCRIPTION, "Throw away trash from room, and wash carpeting.");
        long newRowId = myDB.insert(TaskListContract.TaskEntry.TABLE_NAME, null, values);
        Log.d("MyApp", String.valueOf(newRowId));
        setContentView(R.layout.activity_main);
    }
}