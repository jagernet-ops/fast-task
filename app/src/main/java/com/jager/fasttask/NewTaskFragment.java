package com.jager.fasttask;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.colorpicker.ColorPickerDialog;
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog;
import com.github.dhaval2404.colorpicker.listener.ColorListener;
import com.github.dhaval2404.colorpicker.model.ColorShape;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jager.fasttask.Adapter.ToDoAdapter;
import com.jager.fasttask.Database.TaskListDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

public class NewTaskFragment extends BottomSheetDialogFragment {
    private static final String TAG = "NewTaskFragment";
    private final TaskListDatabase databaseHelper = TaskListDatabase.getManagementInstance(getActivity());

    private String taskColor = "#000000";
    private EditText taskName;
    private EditText taskDescription;
    private EditText taskCategory;
    private EditText taskExpiration;
    private Button colorPicker;
    private Button discardTask;
    private Button saveTask;
    private static List<Task> taskList;
    private static ToDoAdapter mainToDoAdapter;

    public static NewTaskFragment getInstance(List<Task> tasks, ToDoAdapter toDoAdapter){
        taskList = tasks;
        mainToDoAdapter = toDoAdapter;
        return new NewTaskFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_task_gen, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        taskName = view.findViewById(R.id.taskname);
        taskDescription = view.findViewById(R.id.taskdescription);
        taskCategory = view.findViewById(R.id.taskcategory);
        taskExpiration = view.findViewById(R.id.taskexpiration);
        colorPicker = view.findViewById(R.id.colorselectbutton);
        discardTask = view.findViewById(R.id.discardtaskbutton);
        saveTask = view.findViewById(R.id.savetaskbutton);
        boolean updateTask = false;
        Bundle incomingBundle = getArguments();
        if(incomingBundle != null){
            updateTask = true;
            Task constructedTask = new Task(incomingBundle.getString("taskName"), incomingBundle.getString("taskDescription"), new Date(incomingBundle.getLong("taskCreation")), incomingBundle.getString("taskCategory"));
            taskName.setText(constructedTask.getTaskName());
            taskDescription.setText(constructedTask.getTaskDescription());
            taskCategory.setText(constructedTask.getCategory());
        }
        taskName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveTask.setEnabled(!s.toString().equals(""));
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
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
                        String updatedName = taskName.getText().toString();
                        String updatedDescription = taskDescription.getText().toString();
                        String updatedCategory = taskCategory.getText().toString();
                        Task updatedTask = new Task(updatedName, updatedDescription, new Date(incomingBundle.getLong("taskCreation")), updatedCategory);
                        updatedTask.setId(incomingBundle.getInt("id"));
                        updatedTask.setColor(taskColor);
                        databaseHelper.updateTask(updatedTask);
                    }else{
                        String newTaskName = taskName.getText().toString();
                        String newTaskDescription = taskDescription.getText().toString();
                        String newTaskCategory = taskCategory.getText().toString();
                        Task newTask = new Task(newTaskName, newTaskDescription, new Date(), newTaskCategory);
                        newTask.setColor(taskColor);
                        databaseHelper.insertTask(newTask);
                    }
                }
                dismiss();
            }
        });
        discardTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(incomingBundle != null){
                    databaseHelper.deleteTask(incomingBundle.getInt("id"));
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
