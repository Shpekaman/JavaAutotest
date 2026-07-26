package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println(TestMethods.isEven(2));
        System.out.println(TestMethods.isEven(1));
        System.out.println(TestMethods.checkAccess(2));
        System.out.println(TestMethods.checkAccess(22));
        System.out.println(TestMethods.isPositive(2));
        System.out.println(TestMethods.isPositive(-2));
        System.out.println(TestMethods.getGrade(15));
        System.out.println(TestMethods.getGrade(50));
        System.out.println(TestMethods.blastOff(3));
        System.out.println(TestMethods.blastOff(6));
        System.out.println(TestMethods.sumToN(6));
        System.out.println(TestMethods.sumToN(17));
        System.out.println(TestMethods.hasBug(new String[]{"Hello","Bug","World"}));
        System.out.println(TestMethods.hasBug(new String[]{"Hello","Bubu","World"}));
        System.out.println(TestMethods.getEvenInRange(1,10));
        System.out.println(TestMethods.findMax(new int[]{1,6,10,100}));
        System.out.println(Arrays.toString(TestMethods.reverse(new String[]{"Hello", "Bubu", "World"})));
        System.out.println(TestMethods.calcAverage(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)));
        System.out.println(TestMethods.removeSpecificName(List.of("Alice", "Bob", "Charlie"), "Bob"));
    }


}



