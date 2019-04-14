package com.company;

public class PCB {
    public int TTL;
    public int TLL;
    public int TLC;
    public int TTC;

    public PCB(int line,int time){
        TLC=0;
        TTC=0;
        TTL=time;
        TLL=line;
    }
}
