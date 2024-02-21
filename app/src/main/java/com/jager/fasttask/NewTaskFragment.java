package com.jager.fasttask;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jager.fasttask.Database.TaskListDatabase;

import java.util.Date;

public class NewTaskFragment extends BottomSheetDialogFragment {
    private static final String TAG = "NewTaskFragment";
    private final TaskListDatabase databaseHelper = TaskListDatabase.getManagementInstance(getActivity());

    private String color;
    private EditText taskName;
    private EditText taskDescription;
    private EditText taskCategory;
    private EditText taskExpiration;
    private Button colorPicker;
    private Button discardTask;
    private Button saveTask;

    public static NewTaskFragment getInstance(){
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
        boolean[] textBoxesFilled = { false, false, false };
        Bundle incomingBundle = getArguments();
        if(incomingBundle != null){
            updateTask = true;
            Task constructedTask = new Task(incomingBundle.getString("taskName"), incomingBundle.getString("taskDescription"), new Date(incomingBundle.getLong("taskCreation")), incomingBundle.getString("taskCategory"));
            taskName.setText(constructedTask.getTaskName());
            taskDescription.setText(constructedTask.getTaskDescription());
            taskCategory.setText(constructedTask.getCategory());
        }
        if(taskName.length() > 0 || taskDescription.length() > 0 || taskCategory.length() > 0){
            saveTask.setEnabled(false);
        }
        taskName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    saveTask.setEnabled(false);
                    saveTask.setBackgroundColor(Color.GRAY);
                }else if(textBoxesFilled[0] && textBoxesFilled[1] && textBoxesFilled[2]){
                    saveTask.setEnabled(true);
                    saveTask.setBackgroundColor(Color.parseColor(DefaultColors.GREEN.getColor()));
                }else{
                    textBoxesFilled[0] = true;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        taskDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    saveTask.setEnabled(false);
                    saveTask.setBackgroundColor(Color.GRAY);
                }else if(textBoxesFilled[0] && textBoxesFilled[1] && textBoxesFilled[2]){
                    saveTask.setEnabled(true);
                    saveTask.setBackgroundColor(Color.parseColor(DefaultColors.GREEN.getColor()));
                }else{
                    textBoxesFilled[1] = true;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        taskCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    saveTask.setEnabled(false);
                    saveTask.setBackgroundColor(Color.GRAY);
                }else if(textBoxesFilled[0] && textBoxesFilled[1] && textBoxesFilled[2]){
                    saveTask.setEnabled(true);
                    saveTask.setBackgroundColor(Color.parseColor(DefaultColors.GREEN.getColor()));
                }else{
                    textBoxesFilled[2] = true;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        boolean finalUpdateTask = updateTask;
        saveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finalUpdateTask){
                    String updatedName = taskName.getText().toString();
                    String updatedDescription = taskDescription.getText().toString();
                    String updatedCategory = taskCategory.getText().toString();
                    Task updatedTask = new Task(updatedName, updatedDescription, new Date(incomingBundle.getLong("taskCreation")), updatedCategory);
                    updatedTask.setId(incomingBundle.getInt("id"));
                    databaseHelper.updateTask(updatedTask);
                }else{
                    String newTaskName = taskName.getText().toString();
                    String newTaskDescription = taskDescription.getText().toString();
                    String newTaskCategory = taskCategory.getText().toString();
                    Task newTask = new Task(newTaskName, newTaskDescription, new Date(), newTaskCategory);
                    databaseHelper.insertTask(newTask);
                }
                dismiss();
            }
        });
        discardTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
