package com.jager.fasttask.Database;
import android.provider.BaseColumns;

public class TaskListContract {
    private TaskListContract() {}
    public static class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_NAME_CATEGORY_ID = "categoryId";
        public static final String COLUMN_NAME_TASKNAME = "name";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_COLOR = "color";
        public static final String COLUMN_NAME_EXPIRATION = "expirationDate";
        public static final String COLUMN_NAME_CREATION = "creationDate";
        public static final String COLUMN_NAME_COMPLETION = "completionDate";
    }
}


