package com.jager.fasttask.Fragment;

import android.app.Activity;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.dhaval2404.colorpicker.ColorPickerDialog;
import com.github.dhaval2404.colorpicker.listener.ColorListener;
import com.github.dhaval2404.colorpicker.model.ColorShape;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jager.fasttask.Adapter.ToDoAdapter;
import com.jager.fasttask.Database.TaskListDatabase;
import com.jager.fasttask.MainActivity;
import com.jager.fasttask.OnTaskFragmentCloseListener;
import com.jager.fasttask.R;
import com.jager.fasttask.Task;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewTaskFragment extends BottomSheetDialogFragment {
    private static final String TAG = "NewTaskFragment";
    private final TaskListDatabase databaseHelper;

    private String taskColor = "#000000";
    private EditText taskName;
    private EditText taskDescription;
    private EditText taskCategory;
    private EditText taskExpiration;
    private ToDoAdapter mainToDoAdapter;

    public NewTaskFragment(TaskListDatabase mainDatabase, ToDoAdapter todoAdapter){
        databaseHelper = mainDatabase;
        mainToDoAdapter = todoAdapter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_task_gen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        taskName = view.findViewById(R.id.taskname);
        taskDescription = view.findViewById(R.id.taskdescription);
        taskCategory = view.findViewById(R.id.taskcategory);
        taskExpiration = view.findViewById(R.id.taskexpiration);
        Button colorPicker = view.findViewById(R.id.colorselectbutton);
        Button discardTask = view.findViewById(R.id.discardtaskbutton);
        Button saveTask = view.findViewById(R.id.savetaskbutton);
        boolean updateTask = false;
        Bundle incomingBundle = getArguments();
        if(incomingBundle != null){
            updateTask = true;
            Task constructedTask = new Task(incomingBundle.getString("taskName"), incomingBundle.getString("taskDescription"), new Date(incomingBundle.getLong("taskCreation")), incomingBundle.getString("taskCategory"));
            taskName.setText(constructedTask.getTaskName());
            taskDescription.setText(constructedTask.getTaskDescription());
            taskCategory.setText(constructedTask.getCategory());
            taskColor = incomingBundle.getString("taskColor");
        }
        colorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ColorPickerDialog
                        .Builder(view.getContext())
                        .setTitle("Pick Color")
                        .setColorShape(ColorShape.SQAURE)
                        .setDefaultColor(com.github.dhaval2404.colorpicker.R.color.black)
                        .setColorListener(new ColorListener() {
                            @Override
                            public void onColorSelected(int color, @NotNull String colorHex) {
                                // Handle Color Selection
                                taskColor = colorHex.toUpperCase();
                            }
                        })
                        .show();
            }
        });
        boolean finalUpdateTask = updateTask;
        saveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!taskName.getText().toString().equals("")){
                    if(finalUpdateTask){
                        Date updatedTaskExpirationDate = null;
                        if(!taskExpiration.getText().toString().equals("")){
                            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
                            try {
                                updatedTaskExpirationDate = dateFormat.parse(taskExpiration.getText().toString());
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        String updatedName = taskName.getText().toString();
                        String updatedDescription = taskDescription.getText().toString();
                        String updatedCategory = taskCategory.getText().toString();
                        Task updatedTask = new Task(updatedName, updatedDescription, new Date(incomingBundle.getLong("taskCreation")), updatedCategory);
                        updatedTask.setId(incomingBundle.getInt("id"));
                        updatedTask.setExpirationDate(updatedTaskExpirationDate);
                        updatedTask.setColor(taskColor);
                        databaseHelper.updateTask(updatedTask);
                    }else{
                        Date newTaskExpirationDate = null;
                        if(!taskExpiration.getText().toString().equals("")){
                            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
                            try {
                                newTaskExpirationDate = dateFormat.parse(taskExpiration.getText().toString());
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        String newTaskName = taskName.getText().toString();
                        String newTaskDescription = taskDescription.getText().toString();
                        String newTaskCategory = taskCategory.getText().toString();
                        Task newTask = new Task(newTaskName, newTaskDescription, new Date(), newTaskCategory);
                        newTask.setExpirationDate(newTaskExpirationDate);
                        newTask.setColor(taskColor);
                        databaseHelper.insertTask(newTask);
                    }
                }
                databaseHelper.markExpiredTasksComplete();
                dismiss();
            }
        });
        discardTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(incomingBundle != null){
                    databaseHelper.deleteTask(incomingBundle.getInt("id"));
                }
                if(mainToDoAdapter != null){
                    mainToDoAdapter.notifyDataSetChanged();
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if(activity instanceof OnTaskFragmentCloseListener){
            ((OnTaskFragmentCloseListener)activity).onDialogClose(dialog);
        }
    }
}
