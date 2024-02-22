package com.jager.fasttask;

public enum DefaultColors {
    RED("#FC0303"),
    GREEN("#24FC03"),
    BLACK("#000000"),
    BLUE("#0345FC");
    private final String color;
    DefaultColors(String hexColor){
        this.color = hexColor;
    }

    public String getColor(){
        return this.color;
    }
}
