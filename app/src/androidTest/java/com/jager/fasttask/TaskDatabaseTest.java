package com.jager.fasttask;

import android.content.Context;
import android.database.Cursor;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.jager.fasttask.Database.TaskListDatabase;
import com.jager.fasttask.Database.TaskListContract.TaskEntry;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TaskDatabaseTest{
    private final Context mockApplicationContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    private final TaskListDatabase taskListDatabase;
    private final Date testingTimestamp = new Date();

    @Before
    public void testSetup(){
      TaskListDatabase.setDATABASE_NAME(null);
      taskListDatabase = TaskListDatabase.getManagementInstance(mockApplicationContext);
    }

    @Test
    public void shouldCreateDatabaseProperly(){
      String[] columnNames = { "category", "name", "description", "color", "expirationDate", "creationDate", "completed" };
      Cursor taskListCursor = taskListDatabase.getDBCursor();
      assertTrue("Database should be created according to the contract", Arrays.equals(columnNames, taskListCursor.getColumnNames()));
    }

    @Test
    public void shouldAddTaskProperly(){
      Task exampleTask = new Task("Wash Dishes", "Scrub dished with sponge and use soap", testingTimestamp, "Everyday");
      taskListDatabase.insertTask(exampleTask);
      List<Task> retrievedTasks = taskListDatabase.getAllTasks();
      assertTrue("retrievedTasksList should contain at least one task after insertion", retrievedTasks.size() > 0);
      assertTrue("on retrieval constructed task object should not be malformed", retrievedTasks.get(0).equals(exampleTask));
    }

    @Test
    public void filterTasksByCategory(){
      boolean sameCategory = true;
      Task exampleTask = new Task("Wash Dishes", "Scrub dished with sponge and use soap", testingTimestamp, "Everyday");
      taskListDatabase.insertTask(exampleTask);
      List<Task> filteredTasks = taskListDatabase.getTaskFromFilter(TaskEntry.COLUMN_NAME_CATEGORY, "Everyday");
      assertTrue("filteredTasksList should contain at least one task after insertion", filteredTasks.size() > 0);
      assertTrue("on retrieval constructed task object should not be malformed", filteredTasks.get(0).equals(exampleTask));

      for(Task task : filteredTasks){
        if(task.getCategory() != "Everyday"){
          sameCategory = false;
          break;
        }
        continue;
      }

      assertTrue("filtered tasks are all of the same category", sameCategory);
    }
    
    @Test
    public void canUpdateTask(){
      Task exampleTask = new Task("Wash Dishes", "Scrub dished with sponge and use soap", testingTimestamp, "Everyday");
      taskListDatabase.insertTask(exampleTask);
      List<Task> retrievedTasks = taskListDatabase.getAllTasks();
      retrievedTasks.get(0).setTaskName("Clean room");
      retrievedTasks.get(0).setTaskDescription("Wash bedding and clean carpets");
      taskListDatabase.updateTask(retrievedTasks.get(0));
      List<Task> updatedTasks = taskListDatabase.getAllTasks();
      assertTrue("retrievedTasksList should contain at least one task after insertion", updatedTasks.size() > 0);
      assertTrue("on retrieval task object should be different than from on insertion", !updatedTasks.get(0).equals(exampleTask));
    }

    @Test
    public void shouldTrackExpiration(){
      Task exampleTask = new Task("Wash Dishes", "Scrub dished with sponge and use soap", testingTimestamp, "Everyday");
      exampleTask.setExpirationDate(new Date(1143093600)); // Date of some significance :)
      assertTrue("task objects should return true when they have passed their expiration date", exampleTask.hasExpired());
    }

}
