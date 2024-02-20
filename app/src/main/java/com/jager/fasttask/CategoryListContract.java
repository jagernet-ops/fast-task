package com.jager.fasttask;

import android.provider.BaseColumns;

public class CategoryListContract {
    private CategoryListContract() {}
    public static class CategoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "categories";
        public static final String COLUMN_NAME_CATEGORY_NAME = "categoryName";

    }
}
