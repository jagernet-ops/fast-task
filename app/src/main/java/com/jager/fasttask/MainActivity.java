package com.jager.fasttask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jager.fasttask.Adapter.ToDoAdapter;
import com.jager.fasttask.Database.TaskListContract;
import com.jager.fasttask.Database.TaskListDatabase;
import com.jager.fasttask.Fragment.FilterTaskFragment;
import com.jager.fasttask.Fragment.NewTaskFragment;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnTaskFragmentCloseListener{
    private List<Task> renderedTaskList;
    private ToDoAdapter toDoAdapter;
    private TaskListDatabase taskDatabaseHelper;
    private boolean isFiltering = false;
    private final MainActivity thisActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);
        Resources getResources = getResources();
        taskDatabaseHelper = TaskListDatabase.getManagementInstance(getApplicationContext());
        RecyclerView toDoRecycler = toDoRecycler = findViewById(R.id.recyclerview);
        FloatingActionButton addTodoButton = addTodoButton = findViewById(R.id.fabButton);
        MaterialToolbar filterButton = filterButton = findViewById(R.id.topAppBar);
        filterButton.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFiltering = true;
                new FilterTaskFragment(toDoAdapter, taskDatabaseHelper, thisActivity).show(getSupportFragmentManager(), new FilterTaskFragment(toDoAdapter, taskDatabaseHelper, thisActivity).getTag());

            }
        });
        taskDatabaseHelper.getTaskFromFilter(TaskListContract.TaskEntry.COLUMN_NAME_CATEGORY, "Fruit");
        renderedTaskList = new ArrayList<>();
        toDoAdapter = new ToDoAdapter(this, taskDatabaseHelper);
        toDoRecycler.setLayoutManager(new LinearLayoutManager(this));
        toDoRecycler.setAdapter(toDoAdapter);
        taskDatabaseHelper.markExpiredTasksComplete();
        renderedTaskList = taskDatabaseHelper.getAllTasks();
        Collections.reverse(renderedTaskList);
        toDoAdapter.setTaskList(renderedTaskList);
        toDoAdapter.notifyDataSetChanged();
        addTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFiltering = false;
                new NewTaskFragment(taskDatabaseHelper).show(getSupportFragmentManager(), new NewTaskFragment(taskDatabaseHelper).getTag());
                renderedTaskList = taskDatabaseHelper.getAllTasks();
                Collections.reverse(renderedTaskList);
                toDoAdapter.setTaskList(renderedTaskList);
                toDoAdapter.notifyDataSetChanged();
            }
        });
        ItemTouchHelper taskTouchHelper = new ItemTouchHelper(new TaskRecyclerSwipe(toDoAdapter, taskDatabaseHelper, getResources));
        taskTouchHelper.attachToRecyclerView(toDoRecycler);
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        if(!isFiltering) {
            renderedTaskList = TaskListDatabase.getManagementInstance(getApplicationContext()).getAllTasks();
            Collections.reverse(renderedTaskList);
            toDoAdapter.setTaskList(renderedTaskList);
            toDoAdapter.notifyDataSetChanged();
        }
    }
}
