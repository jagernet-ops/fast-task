package com.jager.fasttask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Task {
    private int id;
    private boolean isComplete = false;
    private String taskName = "";
    private String taskDescription = "";
    private String color = "";
    private boolean isExpanded = false;
    private Date creationDate = null;
    private String category = "";
    private Date expirationDate = null;
    public Task(String name, String description, Date createdOn, String taskCategory){
        taskName = name;
        taskDescription = description;
        creationDate = createdOn;
        color = "#000000";
        expirationDate = null;
        category = taskCategory;
    }

    public static String getFormattedDate(Date targetDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        return dateFormat.format(targetDate);
    }
    public void setId(int newId){
        id = newId;
    }

    public void setTaskName(String name){
      taskName = name;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskDescription(String description){
      taskDescription = description;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String hexColor){
        color = hexColor;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }
    public void setExpirationDate(Date expiration){
        expirationDate = expiration;
    }

    public void setCategory(String newCategory){
      category = newCategory;
    }

    public String getCategory() {
        return category;
    }

    public int getId() {
        return id;
    }

    public boolean getIsComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public boolean getIsExpanded(){
        return isExpanded;
    }

    public boolean hasExpired(){
      if(expirationDate == null){
        return false;
      }
      return expirationDate.getTime() < new Date().getTime();
    }

    public void toggleIsExpanded(){
        isExpanded = !isExpanded;
    }
}
