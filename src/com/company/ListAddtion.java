package com.company;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Scanner;

public class ListAddtion {
    static LinkedList<Integer> add(LinkedList<Integer> l1,LinkedList<Integer> l2)
    {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the length of the first list");
        int length=sc.nextInt();
        for(int i=0;i<length;i++) {
            System.out.println("Enter the "+(i+1)+"th number");
            l1.add(sc.nextInt());
        }
        System.out.println("Enter the length of second list");
        length=sc.nextInt();
        for(int i=0;i<length;i++) {
            System.out.println("Enter the "+(i+1)+"th number");
            l2.add(sc.nextInt());
        }
        BigInteger num1=new BigInteger("0");
        BigInteger num2=new BigInteger("0");
        BigInteger ten=new BigInteger("10");
        //compute the value of both the lists first
        for(Integer a:l1) {
            num1=num1.multiply(ten).add(new BigInteger(a.toString()));

        }

        //System.out.println(num1.toString());
        for(Integer a:l2) {
            num2=num2.multiply(ten).add(new BigInteger(a.toString()));
        }

        //then simple addtion
        num1=num1.add(num2);

        //convert the Number to string for easy access of decimal places and put them in list back
        String temp = num1.toString();
        LinkedList<Integer> ans = new LinkedList<>();
        for (int i = 0; i < temp.length(); i++)
            ans.add(temp.charAt(i) - '0');

        return ans;
    }
}
