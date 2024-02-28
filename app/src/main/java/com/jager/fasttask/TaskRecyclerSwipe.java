package com.jager.fasttask;

import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.jager.fasttask.Adapter.ToDoAdapter;
import com.jager.fasttask.Database.TaskListDatabase;

public class TaskRecyclerSwipe extends ItemTouchHelper.SimpleCallback {

    /**
     * Creates a Callback for the given drag and swipe allowance. These values serve as
     * defaults
     * and if you want to customize behavior per ViewHolder, you can override
     * {@link #getSwipeDirs(RecyclerView, ViewHolder)}
     * and / or {@link #getDragDirs(RecyclerView, ViewHolder)}.
     *
     * @param dragDirs  Binary OR of direction flags in which the Views can be dragged. Must be
     *                  composed of {@link #LEFT}, {@link #RIGHT}, {@link #START}, {@link
     *                  #END},
     *                  {@link #UP} and {@link #DOWN}.
     * @param swipeDirs Binary OR of direction flags in which the Views can be swiped. Must be
     *                  composed of {@link #LEFT}, {@link #RIGHT}, {@link #START}, {@link
     *                  #END},
     *                  {@link #UP} and {@link #DOWN}.
     */
    private final ToDoAdapter mainAdapter;
    private final TaskListDatabase databaseHelper;
    public TaskRecyclerSwipe(ToDoAdapter mainAdapter, TaskListDatabase mainDatabase) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.mainAdapter = mainAdapter;
        this.databaseHelper = mainDatabase;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int swipedTaskPosition = viewHolder.getAdapterPosition();
        if(direction == ItemTouchHelper.RIGHT){
            AlertDialog.Builder deleteTaskBuilder = new AlertDialog.Builder(viewHolder.itemView.getContext());
            deleteTaskBuilder.setTitle("Delete Task");
            deleteTaskBuilder.setMessage("This action cannot be undone.");
            deleteTaskBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    databaseHelper.deleteTask(mainAdapter.getTaskList().get(swipedTaskPosition).getId());
                    mainAdapter.getTaskList().remove(swipedTaskPosition);
                    mainAdapter.notifyDataSetChanged();
                }
            });
            deleteTaskBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mainAdapter.notifyItemChanged(swipedTaskPosition);
                }
            });
            deleteTaskBuilder.create();
            deleteTaskBuilder.show();
        }else if(direction == ItemTouchHelper.LEFT){
            mainAdapter.editTaskInformation(swipedTaskPosition);
        }
    }
}
