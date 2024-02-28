package com.jager.fasttask;

import androidx.appcompat.app.AppCompatActivity;
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
    private RecyclerView toDoRecycler;
    private FloatingActionButton addTodoButton;
    private List<Task> renderedTaskList;
    private ToDoAdapter toDoAdapter;
    private MaterialToolbar filterButton;
    private TaskListDatabase taskDatabaseHelper;
    private Resources getResources;
    private boolean isFiltering = false;
    private MainActivity thisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getResources = getResources();
        taskDatabaseHelper = TaskListDatabase.getManagementInstance(getApplicationContext());
        toDoRecycler = findViewById(R.id.recyclerview);
        addTodoButton = findViewById(R.id.fabButton);
        filterButton = findViewById(R.id.topAppBar);
        filterButton.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFiltering = true;
                FilterTaskFragment.getInstance(toDoAdapter, taskDatabaseHelper, thisActivity).show(getSupportFragmentManager(), FilterTaskFragment.getInstance(toDoAdapter, taskDatabaseHelper, thisActivity).getTag());

            }
        });
        taskDatabaseHelper.getTaskFromFilter(TaskListContract.TaskEntry.COLUMN_NAME_CATEGORY, "Fruit");
        renderedTaskList = new ArrayList<>();
        toDoAdapter = new ToDoAdapter(this);
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
                NewTaskFragment.getInstance(renderedTaskList, toDoAdapter).show(getSupportFragmentManager(), NewTaskFragment.getInstance(renderedTaskList, toDoAdapter).getTag());
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
