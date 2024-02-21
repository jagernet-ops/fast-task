package com.jager.fasttask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jager.fasttask.Adapter.ToDoAdapter;
import com.jager.fasttask.Database.TaskListContract.TaskEntry;
import com.jager.fasttask.Database.TaskListDatabase;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnTaskFragmentCloseListener{
    private RecyclerView toDoRecycler;
    private FloatingActionButton addTodoButton;
    private List<Task> renderedTaskList;
    private ToDoAdapter toDoAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toDoRecycler = findViewById(R.id.recyclerview);
        addTodoButton = findViewById(R.id.fabButton);
        renderedTaskList = new ArrayList<>();
        toDoAdapter = new ToDoAdapter(this);
        toDoRecycler.setHasFixedSize(true);
        toDoRecycler.setLayoutManager(new LinearLayoutManager(this));
        toDoRecycler.setAdapter(toDoAdapter);
        addTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewTaskFragment.getInstance().show(getSupportFragmentManager(), NewTaskFragment.getInstance().getTag());
            }
        });
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        renderedTaskList = TaskListDatabase.getManagementInstance(getApplicationContext()).getAllTasks();
        Collections.reverse(renderedTaskList);
        toDoAdapter.setTaskList(renderedTaskList);
        toDoAdapter.notifyDataSetChanged();
    }
}