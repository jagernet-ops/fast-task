package com.jager.fasttask;

import java.util.Date;

public class Task {
    private final String taskName;
    private final String taskDescription;
    private final String color;
    private final Date creationDate;
    private final Date completionDate;
    private final Date expirationDate;
    public Task(String name, String description, Date createdOn){
        this.taskName = name;
        this.taskDescription = description;
        this.creationDate = createdOn;
        this.color = DefaultColors.WHITE.getColor();
        this.completionDate = null;
        this.expirationDate = null;
    }
    public Task(String name, String description, Date createdOn, DefaultColors color){
        this.taskName = name;
        this.taskDescription = description;
        this.creationDate = createdOn;
        this.color = color.getColor();
        this.completionDate = null;
        this.expirationDate = null;
    }
    public Task(String name, String description, Date createdOn, String customColor){
        this.taskName = name;
        this.taskDescription = description;
        this.creationDate = createdOn;
        this.color = customColor;
        this.completionDate = null;
        this.expirationDate = null;
    }

    public Task(String name, String description, Date createdOn, Date expiration){
        this.taskName = name;
        this.taskDescription = description;
        this.color = DefaultColors.WHITE.getColor();
        this.creationDate = createdOn;
        this.expirationDate = expiration;
        this.completionDate = null;
    }

    public Task(String name, String description, Date createdOn, Date expiration, Date completedOn){
        this.taskName = name;
        this.taskDescription = description;
        this.color = DefaultColors.WHITE.getColor();
        this.creationDate = createdOn;
        this.expirationDate = expiration;
        this.completionDate = completedOn;
    }

}
