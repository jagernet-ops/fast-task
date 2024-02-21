package com.jager.fasttask.Adapter;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jager.fasttask.Database.TaskListDatabase;
import com.jager.fasttask.MainActivity;
import com.jager.fasttask.NewTaskFragment;
import com.jager.fasttask.R;
import com.jager.fasttask.Task;
import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private List<Task> taskList;
    private final MainActivity activity;
    private final TaskListDatabase databaseHelper;

    public ToDoAdapter(MainActivity mainActivity){
        this.activity = mainActivity;
        this.databaseHelper = TaskListDatabase.getManagementInstance(mainActivity.getApplicationContext());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Task task = taskList.get(position);
        holder.categoryFlag.setColorFilter(Color.parseColor(task.getColor()));
        holder.checkBox.setText(task.getTaskName());
        holder.categoryName.setText(task.getTaskDescription());
        holder.checkBox.setChecked(task.isComplete());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    task.setComplete(true);
                    taskList.remove(task);
                    databaseHelper.deleteTask(task);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void setTaskList(List<Task> newTasks){
        this.taskList = newTasks;
        notifyDataSetChanged();
    }

    public void editTaskInformation(int position){
        Task targetTask = taskList.get(position);
        Bundle taskInfoBundle = new Bundle();
        taskInfoBundle.putInt("id", targetTask.getId());
        taskInfoBundle.putString("taskName", targetTask.getTaskName());
        taskInfoBundle.putString("taskDescription", targetTask.getTaskDescription());
        taskInfoBundle.putString("taskCategory", targetTask.getCategory());
        taskInfoBundle.putString("taskColor", targetTask.getColor());
        taskInfoBundle.putLong("taskCreation", targetTask.getCreationDate().getTime());
        taskInfoBundle.putLong("taskExpiration", targetTask.getExpirationDate().getTime());

        NewTaskFragment editPopup = new NewTaskFragment();
        editPopup.setArguments(taskInfoBundle);
        editPopup.show(activity.getSupportFragmentManager(), editPopup.getTag());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        ImageView categoryFlag;
        TextView categoryName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.taskcheckbox);
            categoryName = itemView.findViewById(R.id.categoryname);
            categoryFlag = itemView.findViewById(R.id.categoryflag);
        }
    }
}
