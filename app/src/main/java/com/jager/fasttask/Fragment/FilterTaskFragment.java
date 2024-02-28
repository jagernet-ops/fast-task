package com.jager.fasttask.Fragment;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog;
import com.github.dhaval2404.colorpicker.listener.ColorListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jager.fasttask.Adapter.ToDoAdapter;
import com.jager.fasttask.Database.TaskListContract.TaskEntry;
import com.jager.fasttask.Database.TaskListDatabase;
import com.jager.fasttask.MainActivity;
import com.jager.fasttask.OnTaskFragmentCloseListener;
import com.jager.fasttask.R;
import com.jager.fasttask.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FilterTaskFragment extends BottomSheetDialogFragment {

    private AutoCompleteTextView taskFilter;
    private Button filterColor;
    private Button filterExpiry;
    private static ToDoAdapter toDoAdapter;
    private static TaskListDatabase databaseHelper;
    private static MainActivity mainActivityReference;

    public static FilterTaskFragment getInstance(ToDoAdapter mainAdapter, TaskListDatabase mainDatabase, MainActivity mainActivity){
        toDoAdapter = mainAdapter;
        databaseHelper = mainDatabase;
        mainActivityReference = mainActivity;
        return new FilterTaskFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_task, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        taskFilter = view.findViewById(R.id.filterAutoComplete);
        filterColor = view.findViewById(R.id.filterColor);
        filterExpiry = view.findViewById(R.id.filterExpiry);
        taskFilter.setThreshold(1);
        filterColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(toDoAdapter.getTaskList().size() != 0){
                   String[] colors = databaseHelper.getAllTasks().stream().map(Task::getColor).toArray(String[]::new);
                   new MaterialColorPickerDialog.Builder(requireActivity())
                           .setColors(colors)
                           .setColorListener(new ColorListener() {
                               @Override
                               public void onColorSelected(int i, @NonNull String s) {
                                   toDoAdapter.setTaskList(databaseHelper.getTaskFromFilter(TaskEntry.COLUMN_NAME_COLOR, s));
                                   toDoAdapter.notifyDataSetChanged();
                                   dismiss();
                               }
                           })
                           .show();
               }

            }
        });
        filterExpiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDoAdapter.setTaskList(databaseHelper.getTaskByExpiry());
                toDoAdapter.notifyDataSetChanged();
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
