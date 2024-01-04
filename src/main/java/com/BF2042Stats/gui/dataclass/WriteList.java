package com.BF2042Stats.gui.dataclass;

public class WriteList {
    private String s1,message;
    public WriteList(String s1,String message){
        this.s1 = s1;
        this.message = message;
    }
    public String getS1(){
        return s1;
    }

    @Override
    public String toString() {
        return message;
    }
}
