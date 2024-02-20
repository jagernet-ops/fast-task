package com.jager.fasttask;

import java.util.Date;

public class Task {
    private final String taskName;
    private final String taskDescription;
    private final String color;
    private final Date creationDate;
    private final String category;
    private final Date completionDate;
    private final Date expirationDate;
    public Task(String name, String description, Date createdOn, String category){
        this.taskName = name;
        this.taskDescription = description;
        this.creationDate = createdOn;
        this.color = DefaultColors.BLACK.getColor();
        this.completionDate = null;
        this.expirationDate = null;
        this.category = category;
    }
    public Task(String name, String description, Date createdOn, DefaultColors color, String category){
        this.taskName = name;
        this.taskDescription = description;
        this.creationDate = createdOn;
        this.color = color.getColor();
        this.completionDate = null;
        this.expirationDate = null;
        this.category = category;
    }
    public Task(String name, String description, Date createdOn, String customColor, String category){
        this.taskName = name;
        this.taskDescription = description;
        this.creationDate = createdOn;
        this.color = customColor;
        this.completionDate = null;
        this.expirationDate = null;
        this.category = category;
    }

    public Task(String name, String description, Date createdOn, Date expiration, String category){
        this.taskName = name;
        this.taskDescription = description;
        this.color = DefaultColors.BLACK.getColor();
        this.creationDate = createdOn;
        this.expirationDate = expiration;
        this.completionDate = null;
        this.category = category;
    }

    public Task(String name, String description, Date createdOn, Date expiration, Date completedOn, String category){
        this.taskName = name;
        this.taskDescription = description;
        this.color = DefaultColors.BLACK.getColor();
        this.creationDate = createdOn;
        this.expirationDate = expiration;
        this.completionDate = completedOn;
        this.category = category;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public String getCategory() {
        return category;
    }
}
