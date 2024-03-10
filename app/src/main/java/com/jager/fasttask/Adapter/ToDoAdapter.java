package com.jager.fasttask.Adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jager.fasttask.Database.TaskListDatabase;
import com.jager.fasttask.MainActivity;
import com.jager.fasttask.Fragment.NewTaskFragment;
import com.jager.fasttask.R;
import com.jager.fasttask.Task;

import java.util.ArrayList;
import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private List<Task> taskList = new ArrayList<Task>(0);
    private final MainActivity activity;
    private final TaskListDatabase databaseHelper;

    public ToDoAdapter(MainActivity mainActivity, TaskListDatabase mainDatabase){
        this.activity = mainActivity;
        this.databaseHelper = mainDatabase;
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
        holder.checkBox.setPaintFlags(task.getIsComplete() ? holder.checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG : holder.checkBox.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        holder.categoryName.setText(task.getCategory());
        holder.categoryName.setPaintFlags(task.getIsComplete() ? holder.checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG : holder.checkBox.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        holder.checkBox.setChecked(task.getIsComplete());
        holder.taskDescription.setText(task.getTaskDescription());
        holder.taskDescription.setPaintFlags(task.getIsComplete() ? holder.checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG : holder.checkBox.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        String createdOn = "Started: "+Task.getFormattedDate(task.getCreationDate());
        holder.taskStart.setText(createdOn);
        if(task.getExpirationDate() == null){
            holder.taskExpire.setVisibility(View.GONE);
            holder.addToCalendar.setVisibility(View.GONE);
        }else{
            holder.taskExpire.setVisibility(View.VISIBLE);
            holder.addToCalendar.setVisibility(View.VISIBLE);
            String expiresOn = "Expires: "+Task.getFormattedDate(task.getExpirationDate());
            holder.taskExpire.setText(expiresOn);
            holder.addToCalendar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_INSERT)
                            .setData(Events.CONTENT_URI)
                            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, task.getExpirationDate().getTime())
                            .putExtra(Events.TITLE, task.getTaskName())
                            .putExtra(Events.DESCRIPTION, task.getTaskDescription())
                            .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.getApplicationContext().startActivity(intent);
                }
            });
        }
        if(!task.getIsComplete()){
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        task.setComplete(true);
                        databaseHelper.updateTask(task);
                        notifyDataSetChanged();
                    }
                }
            });
        }else{
            holder.checkBox.setEnabled(false);
        }
        holder.expandTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.toggleIsExpanded();
                if(task.getIsExpanded()){
                    holder.expandTaskButton.setImageResource(R.drawable.baseline_expand_less_24);
                    holder.expandedTask.setVisibility(View.VISIBLE);
                    notifyDataSetChanged();
                }else{
                    holder.expandTaskButton.setImageResource(R.drawable.baseline_expand_more_24);
                    holder.expandedTask.setVisibility(View.GONE);
                    notifyDataSetChanged();
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
        if(targetTask.getExpirationDate() != null){
            taskInfoBundle.putLong("taskExpiration", targetTask.getExpirationDate().getTime());
        }

        NewTaskFragment editPopup = new NewTaskFragment(databaseHelper, this);
        editPopup.setArguments(taskInfoBundle);
        editPopup.show(activity.getSupportFragmentManager(), editPopup.getTag());
    }

    public List<Task> getTaskList(){
        return taskList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        ImageView categoryFlag;
        TextView categoryName;
        ImageButton expandTaskButton;
        TextView taskDescription;
        TextView taskStart;
        TextView taskExpire;
        RelativeLayout expandedTask;
        ImageButton addToCalendar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.taskcheckbox);
            categoryName = itemView.findViewById(R.id.categoryname);
            categoryFlag = itemView.findViewById(R.id.categoryflag);
            expandTaskButton = itemView.findViewById(R.id.taskexpandbutton);
            expandedTask = itemView.findViewById(R.id.expandedView);
            taskDescription = itemView.findViewById(R.id.taskcarddescription);
            taskStart = itemView.findViewById(R.id.startingDate);
            taskExpire = itemView.findViewById(R.id.expireDate);
            addToCalendar = itemView.findViewById(R.id.addToCalendar);
        }
    }
}
