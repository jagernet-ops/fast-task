package com.jager.fasttask;

public enum DefaultColors {
    RED("#fc0303"),
    GREEN("#24fc03"),
    BLACK("000000"),
    BLUE("#0345fc");
    private final String color;
    DefaultColors(String hexColor){
        this.color = hexColor;
    }

    public String getColor(){
        return this.color;
    }
}
