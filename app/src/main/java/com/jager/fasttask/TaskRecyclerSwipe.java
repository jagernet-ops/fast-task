package com.jager.fasttask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.jager.fasttask.Adapter.ToDoAdapter;
import com.jager.fasttask.Database.TaskListDatabase;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

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
    private final Resources getResources;
    public TaskRecyclerSwipe(ToDoAdapter mainAdapter, TaskListDatabase mainDatabase, Resources mainActivitytResources) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.mainAdapter = mainAdapter;
        this.databaseHelper = mainDatabase;
        getResources = mainActivitytResources;
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
                    mainAdapter.setTaskList(databaseHelper.getAllTasks());
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

    @Override
    public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
        float swipeBoundary = dX;
        if(swipeBoundary >= 150f){
            swipeBoundary = 150f;
        }else if(swipeBoundary <= -150f){
            swipeBoundary = -150f;
        }
        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addCornerRadius(TypedValue.COMPLEX_UNIT_PT, 8)
                .addSwipeLeftBackgroundColor(getResources.getColor(R.color.green))
                .addSwipeLeftActionIcon(R.drawable.baseline_edit_24)
                .addSwipeRightBackgroundColor(getResources.getColor(R.color.red))
                .addSwipeRightActionIcon(R.drawable.baseline_delete_forever_24_white)
                .create()
                .decorate();
        super.onChildDraw(c, recyclerView, viewHolder, swipeBoundary, dY, actionState, isCurrentlyActive);
    }
}
