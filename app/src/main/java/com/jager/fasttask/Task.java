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
    public Task(String name, String description, Date createdOn, String category){
        this.taskName = name;
        this.taskDescription = description;
        this.creationDate = createdOn;
        this.color = "#000000";
        this.expirationDate = null;
        this.category = category;
    }

    public static String getFormattedDate(Date targetDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        return dateFormat.format(targetDate);
    }
    public void setId(int id){
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String hexColor){
        this.color = hexColor;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }
    public void setExpirationDate(Date expirationDate){
        this.expirationDate = expirationDate;
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


    public void toggleIsExpanded(){
        isExpanded = !isExpanded;
    }
}
