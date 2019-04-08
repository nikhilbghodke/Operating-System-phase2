package com.company;

import java.io.IOException;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {
        // write your code here
        OS ubuntu =new OS("Input.txt","output.txt");
        ubuntu.LOAD();
        try {
            ubuntu.output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
